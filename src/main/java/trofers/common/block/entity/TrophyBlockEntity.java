package trofers.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.Level;
import trofers.Trofers;
import trofers.common.init.ModBlockEntityTypes;
import trofers.common.trophy.EffectInfo;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyManager;
import trofers.common.block.TrophyBlock;

import javax.annotation.Nullable;

public class TrophyBlockEntity extends TileEntity implements ITickableTileEntity {

    @Nullable
    private ResourceLocation trophyID;

    private int rewardCooldown;

    private float animationOffset;

    public TrophyBlockEntity() {
        super(ModBlockEntityTypes.TROPHY.get());
    }

    @Nullable
    public Trophy getTrophy() {
        return TrophyManager.get(trophyID);
    }

    public void setTrophy(@Nullable Trophy trophy) {
        if (trophy != null) {
            trophyID = trophy.id();
        } else {
            trophyID = null;
        }
        restartRewardCooldown();
        onContentsChanged();
    }

    @Nullable
    public ResourceLocation getTrophyID() {
        return trophyID;
    }

    public float getAnimationOffset() {
        if (animationOffset == 0 && level != null) {
            animationOffset = level.getRandom().nextFloat() * 4000;
        }
        return animationOffset;
    }

    public int getTrophyHeight() {
        Block block = getBlockState().getBlock();
        if (block instanceof TrophyBlock) {
            return ((TrophyBlock) block).getHeight();
        }
        return 0;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos().offset(-1, 0, -1), getBlockPos().offset(1, 16, 1));
    }

    public void restartRewardCooldown() {
        Trophy trophy = getTrophy();
        if (trophy != null && trophy.effects().rewards().cooldown() > 0) {
            rewardCooldown = trophy.effects().rewards().cooldown();
        } else {
            rewardCooldown = 0;
        }
    }

    public void removeCooldown() {
        rewardCooldown = 0;
    }

    @Override
    public void tick() {
        if (rewardCooldown > 0) {
            rewardCooldown--;
            if (level != null) {
                level.blockEntityChanged(getBlockPos(), this);
            }
        }
    }

    public boolean applyEffect(PlayerEntity player, Hand hand) {
        Trophy trophy = getTrophy();
        if (trophy == null || level == null) {
            return false;
        }
        EffectInfo.RewardInfo rewards = trophy.effects().rewards();
        EffectInfo.SoundInfo sound = trophy.effects().sound();

        if (sound != null) {
            player.level.playSound(
                    player,
                    getBlockPos(),
                    sound.soundEvent(),
                    SoundCategory.BLOCKS,
                    sound.volume(),
                    sound.pitch()
            );
        }

        giveRewards(rewards, player, hand);

        return sound != null
                || rewards.lootTable() != null
                || !rewards.statusEffect().isEmpty();
    }

    private void giveRewards(EffectInfo.RewardInfo rewards, PlayerEntity player, Hand hand) {
        if (player.level.isClientSide()) {
            return;
        }
        if (rewardCooldown > 0) {
            player.displayClientMessage(
                    new TranslationTextComponent(
                            String.format("message.%s.reward_cooldown", Trofers.MODID),
                            getTime(rewardCooldown)
                    ), true
            );
            return;
        }

        restartRewardCooldown();
        rewardLoot(rewards, player, hand);
        rewardPotionEffect(rewards, player);
    }

    private ITextComponent getTime(int ticks) {
        int seconds = (ticks + 20) / 20;
        if (seconds <= 1) {
            return new TranslationTextComponent("time.trofers.second");
        } else if (seconds < 60) {
            return new TranslationTextComponent("time.trofers.seconds", seconds);
        }

        int minutes = seconds / 60;
        if (minutes <= 1) {
            return new TranslationTextComponent("time.trofers.minute");
        } else if (minutes < 60) {
            return new TranslationTextComponent("time.trofers.minutes", minutes);
        }

        int hours = minutes / 60;
        if (hours <= 1) {
            return new TranslationTextComponent("time.trofers.hour");
        } else {
            return new TranslationTextComponent("time.trofers.hours", hours);
        }
    }

    private void rewardPotionEffect(EffectInfo.RewardInfo rewards, PlayerEntity player) {
        EffectInstance potionEffect = rewards.createStatusEffect();
        if (potionEffect != null) {
            player.addEffect(potionEffect);
        }
    }

    private void rewardLoot(EffectInfo.RewardInfo rewards, PlayerEntity player, Hand hand) {
        ResourceLocation lootTableLocation = rewards.lootTable();
        if (lootTableLocation != null) {
            // noinspection ConstantConditions
            LootTable lootTable = level.getServer().getLootTables().get(lootTableLocation);
            if (lootTable == LootTable.EMPTY) {
                Trofers.LOGGER.log(Level.ERROR, "Invalid loot table: {}", lootTableLocation);
                return;
            }
            LootContext.Builder builder = createLootContext(player, player.getItemInHand(hand));
            LootContext context = builder.create(LootParameterSets.EMPTY);
            lootTable.getRandomItems(context).forEach(this::spawnAtLocation);
        }
    }

    private LootContext.Builder createLootContext(PlayerEntity player, ItemStack stack) {
        // noinspection ConstantConditions
        return (new LootContext.Builder((ServerWorld) level))
                .withRandom(level.getRandom())
                .withParameter(LootParameters.BLOCK_ENTITY, this)
                .withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(getBlockPos()))
                .withParameter(LootParameters.THIS_ENTITY, player)
                .withParameter(LootParameters.BLOCK_STATE, getBlockState())
                .withParameter(LootParameters.TOOL, stack);
    }

    @Nullable
    public void spawnAtLocation(ItemStack stack) {
        if (!stack.isEmpty() && level != null && !level.isClientSide) {
            ItemEntity item = new ItemEntity(
                    level,
                    getBlockPos().getX() + 0.5,
                    getBlockPos().getY() + getTrophyHeight() / 16D + 0.2,
                    getBlockPos().getZ() + 0.5,
                    stack
            );
            item.setDefaultPickUpDelay();
            level.addFreshEntity(item);
        }
    }


    private void onContentsChanged() {
        if (level != null) {
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                setChanged();
            } else {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.RERENDER_MAIN_THREAD);
            }
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT result = super.getUpdateTag();
        saveTrophy(result);
        return result;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager connection, SUpdateTileEntityPacket packet) {
        loadTrophy(packet.getTag());
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        loadTrophy(tag);
        rewardCooldown = tag.getInt("RewardCooldown");
    }

    public void loadTrophy(CompoundNBT tag) {
        trophyID = null;

        if (tag.contains("Trophy", Constants.NBT.TAG_STRING)) {
            try {
                trophyID = new ResourceLocation(tag.getString("Trophy"));
            } catch (ResourceLocationException exception) {
                Trofers.LOGGER.error(String.format("Failed to load trophy for block entity at %s", getBlockPos()), exception);
            }

            Trophy trophy = TrophyManager.get(trophyID);
            if (trophy == null) {
                Trofers.LOGGER.error(String.format("Invalid trophy id for block entity at %s: %s", getBlockPos(), trophyID));
            }
        }

        if (getLevel() != null && getLevel().isClientSide()) {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.RERENDER_MAIN_THREAD);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        saveTrophy(tag);
        if (rewardCooldown > 0) {
            tag.putInt("RewardCooldown", rewardCooldown);
        }
        return super.save(tag);
    }

    public void saveTrophy(CompoundNBT tag) {
        if (trophyID != null) {
            tag.putString("Trophy", trophyID.toString());
        }
    }
}
