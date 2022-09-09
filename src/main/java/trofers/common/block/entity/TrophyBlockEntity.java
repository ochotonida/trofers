package trofers.common.block.entity;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Level;
import trofers.Trofers;
import trofers.common.block.TrophyBlock;
import trofers.common.config.ModConfig;
import trofers.common.init.ModBlockEntityTypes;
import trofers.common.trophy.EffectInfo;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyManager;

import javax.annotation.Nullable;

public class TrophyBlockEntity extends BlockEntity {

    public static final BlockEntityTicker<TrophyBlockEntity> TICKER = (level, pos, state, blockEntity) -> blockEntity.tick();

    @Nullable
    private ResourceLocation trophyID;

    private int rewardCooldown;

    private float animationOffset;

    public TrophyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.TROPHY.get(), pos, state);
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
        if (block instanceof TrophyBlock trophy) {
            return trophy.getHeight();
        }
        return 0;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos().offset(-1, 0, -1), getBlockPos().offset(1, 16, 1));
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

    public void tick() {
        if (rewardCooldown > 0) {
            rewardCooldown--;
            if (level != null) {
                level.blockEntityChanged(getBlockPos());
            }
        }
    }

    public boolean applyEffect(Player player, InteractionHand hand) {
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
                    SoundSource.BLOCKS,
                    sound.volume(),
                    sound.pitch()
            );
        }

        giveRewards(rewards, player, hand);

        return sound != null
                || rewards.lootTable() != null && ModConfig.common.enableTrophyLoot.get()
                || !rewards.statusEffect().isEmpty() && ModConfig.common.enableTrophyEffects.get();
    }

    private void giveRewards(EffectInfo.RewardInfo rewards, Player player, InteractionHand hand) {
        if (player.level.isClientSide()) {
            return;
        } else if ((!ModConfig.common.enableTrophyLoot.get() || rewards.lootTable() == null)
                && (!ModConfig.common.enableTrophyEffects.get() || rewards.statusEffect().isEmpty())) {
            return;
        }

        if (rewardCooldown > 0) {
            player.displayClientMessage(
                    Component.translatable(
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

    private Component getTime(int ticks) {
        int seconds = (ticks + 20) / 20;
        if (seconds <= 1) {
            return Component.translatable("time.trofers.second");
        } else if (seconds < 60) {
            return Component.translatable("time.trofers.seconds", seconds);
        }

        int minutes = seconds / 60;
        if (minutes <= 1) {
            return Component.translatable("time.trofers.minute");
        } else if (minutes < 60) {
            return Component.translatable("time.trofers.minutes", minutes);
        }

        int hours = minutes / 60;
        if (hours <= 1) {
            return Component.translatable("time.trofers.hour");
        } else {
            return Component.translatable("time.trofers.hours", hours);
        }
    }

    private void rewardPotionEffect(EffectInfo.RewardInfo rewards, Player player) {
        if (ModConfig.common.enableTrophyEffects.get()) {
            MobEffectInstance potionEffect = rewards.createStatusEffect();
            if (potionEffect != null) {
                player.addEffect(potionEffect);
            }
        }
    }

    private void rewardLoot(EffectInfo.RewardInfo rewards, Player player, InteractionHand hand) {
        if (ModConfig.common.enableTrophyLoot.get()) {
            ResourceLocation lootTableLocation = rewards.lootTable();
            if (lootTableLocation != null) {
                // noinspection ConstantConditions
                LootTable lootTable = level.getServer().getLootTables().get(lootTableLocation);
                if (lootTable == LootTable.EMPTY) {
                    Trofers.LOGGER.log(Level.ERROR, "Invalid loot table: {}", lootTableLocation);
                    return;
                }
                LootContext.Builder builder = createLootContext(player, player.getItemInHand(hand));
                LootContext context = builder.create(LootContextParamSets.EMPTY);
                lootTable.getRandomItems(context).forEach(this::spawnAtLocation);
            }
        }
    }

    private LootContext.Builder createLootContext(Player player, ItemStack stack) {
        // noinspection ConstantConditions
        return (new LootContext.Builder((ServerLevel) level))
                .withRandom(level.getRandom())
                .withParameter(LootContextParams.BLOCK_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(getBlockPos()))
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.BLOCK_STATE, getBlockState())
                .withParameter(LootContextParams.TOOL, stack);
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
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
                setChanged();
            } else {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag result = super.getUpdateTag();
        saveTrophy(result);
        return result;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        if (packet.getTag() != null) {
            loadTrophy(packet.getTag());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadTrophy(tag);
        rewardCooldown = tag.getInt("RewardCooldown");
    }

    public void loadTrophy(CompoundTag tag) {
        trophyID = null;

        if (tag.contains("Trophy", Tag.TAG_STRING)) {
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
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        saveTrophy(tag);
        if (rewardCooldown > 0) {
            tag.putInt("RewardCooldown", rewardCooldown);
        }
    }

    public void saveTrophy(CompoundTag tag) {
        if (trophyID != null) {
            tag.putString("Trophy", trophyID.toString());
        }
    }
}
