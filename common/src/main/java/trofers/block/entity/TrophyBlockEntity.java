package trofers.block.entity;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.block.TrophyBlock;
import trofers.registry.ModBlockEntityTypes;
import trofers.trophy.Trophy;
import trofers.trophy.TrophyManager;
import trofers.trophy.components.EffectInfo;

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

        if (sound != null && level instanceof ServerLevel serverLevel) {
            Vec3 pos = Vec3.atCenterOf(getBlockPos());
            playSound(serverLevel, sound.soundEvent(), pos, sound.volume(), sound.pitch());
        }

        giveRewards(rewards, player, hand);

        return sound != null
                || rewards.lootTable() != null && Trofers.CONFIG.general.enableTrophyLoot
                || !rewards.statusEffect().isEmpty() && Trofers.CONFIG.general.enableTrophyEffects;
    }

    private static void playSound(ServerLevel level, ResourceLocation sound, Vec3 pos, float volume, float pitch) {
        double maxDistance = Math.pow(volume > 1 ? volume * 16D : 16, 2);
        long seed = level.getRandom().nextLong();

        for (ServerPlayer player : level.players()) {
            double x = pos.x - player.getX();
            double y = pos.y - player.getY();
            double z = pos.z - player.getZ();
            double distance = x * x + y * y + z * z;
            if (distance > maxDistance) {
                continue;
            }

            player.connection.send(new ClientboundCustomSoundPacket(sound, SoundSource.BLOCKS, pos, volume, pitch, seed));
        }
    }

    private void giveRewards(EffectInfo.RewardInfo rewards, Player player, InteractionHand hand) {
        if (player.level.isClientSide()) {
            return;
        } else if ((!Trofers.CONFIG.general.enableTrophyLoot || rewards.lootTable() == null)
                && (!Trofers.CONFIG.general.enableTrophyEffects || rewards.statusEffect().isEmpty())) {
            return;
        }

        if (rewardCooldown > 0) {
            player.displayClientMessage(
                    Component.translatable(
                            String.format("message.%s.reward_cooldown", Trofers.MOD_ID),
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
        if (Trofers.CONFIG.general.enableTrophyEffects) {
            MobEffectInstance potionEffect = rewards.createStatusEffect();
            if (potionEffect != null) {
                player.addEffect(potionEffect);
            }
        }
    }

    private void rewardLoot(EffectInfo.RewardInfo rewards, Player player, InteractionHand hand) {
        if (Trofers.CONFIG.general.enableTrophyLoot) {
            ResourceLocation lootTableLocation = rewards.lootTable();
            if (lootTableLocation != null) {
                // noinspection ConstantConditions
                LootTable lootTable = level.getServer().getLootTables().get(lootTableLocation);
                if (lootTable == LootTable.EMPTY) {
                    Trofers.LOGGER.log(Level.ERROR, "Invalid loot table: {}", lootTableLocation);
                    return;
                }
                LootContext.Builder builder = createLootContext(player, player.getItemInHand(hand));
                LootContext context = builder.create(LootContextParamSets.BLOCK);
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
        saveAdditional(result);
        return result;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag tag) {
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
        if (trophyID != null) {
            tag.putString("Trophy", trophyID.toString());
        }
        if (rewardCooldown > 0) {
            tag.putInt("RewardCooldown", rewardCooldown);
        }
    }
}
