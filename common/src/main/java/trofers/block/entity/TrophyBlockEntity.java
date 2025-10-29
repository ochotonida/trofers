package trofers.block.entity;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.block.TrophyBlock;
import trofers.registry.ModBlockEntityTypes;
import trofers.registry.ModDataComponents;
import trofers.registry.ModRegistries;
import trofers.trophy.Trophy;
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
        return ModRegistries.get(ModRegistries.TROPHIES, trophyID);
    }

    public void setTrophy(@Nullable ResourceLocation trophyId, ServerPlayer player) {
        this.trophyID = trophyId;
        resetRewardCooldown(player);
        onContentsChanged();
    }

    public ItemStack getItem() {
        ItemStack stack = new ItemStack(getBlockState().getBlock().asItem());
        stack.applyComponents(this.collectComponents());
        return stack;
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

    public void resetRewardCooldown(@Nullable ServerPlayer player) {
        Trophy trophy = getTrophy();
        if ((player == null || !player.isCreative()) && trophy != null && trophy.effects().rewards().cooldown() > 0) {
            rewardCooldown = trophy.effects().rewards().cooldown();
        } else {
            rewardCooldown = 0;
        }
    }

    public void tick() {
        if (rewardCooldown > 0) {
            rewardCooldown--;
            if (level != null) {
                level.blockEntityChanged(getBlockPos());
            }
        }
    }

    public boolean applyEffect(Player player) {
        Trophy trophy = getTrophy();
        if (trophy == null || level == null) {
            return false;
        }
        EffectInfo.RewardInfo rewards = trophy.effects().rewards();

        trophy.effects().sound().ifPresent(sound -> {
            if (level instanceof ServerLevel serverLevel) {
                Vec3 pos = Vec3.atCenterOf(getBlockPos());
                playSound(serverLevel, sound.soundEvent(), pos, sound.volume(), sound.pitch());
            }
        });

        giveRewards(rewards, player);

        return trophy.effects().sound().isPresent()
                || rewards.lootTable().isPresent() && Trofers.CONFIG.general.enableTrophyLoot
                || rewards.mobEffect().isPresent() && Trofers.CONFIG.general.enableTrophyEffects;
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
            Holder<SoundEvent> holder = Holder.direct(SoundEvent.createVariableRangeEvent(sound));
            player.connection.send(new ClientboundSoundPacket(holder, SoundSource.BLOCKS, pos.x(), pos.y(), pos.z(), volume, pitch, seed));
        }
    }

    private void giveRewards(EffectInfo.RewardInfo rewards, Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        } else if ((!Trofers.CONFIG.general.enableTrophyLoot || rewards.lootTable().isEmpty())
                && (!Trofers.CONFIG.general.enableTrophyEffects || rewards.mobEffect().isEmpty())) {
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

        resetRewardCooldown(serverPlayer);
        rewardLoot(rewards);
        rewardMobEffect(rewards, player);
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

    private void rewardMobEffect(EffectInfo.RewardInfo rewards, Player player) {
        if (Trofers.CONFIG.general.enableTrophyEffects) {
            rewards.mobEffect()
                    .map(EffectInfo.MobEffectInfo::createInstance)
                    .ifPresent(player::addEffect);
        }
    }

    private void rewardLoot(EffectInfo.RewardInfo rewards) {
        if (Trofers.CONFIG.general.enableTrophyLoot) {
            rewards.lootTable().ifPresent(lootTableLocation -> {
                // noinspection ConstantConditions
                LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(lootTableLocation);
                if (lootTable == LootTable.EMPTY) {
                    Trofers.LOGGER.log(Level.ERROR, "Invalid loot table: {}", lootTableLocation);
                    return;
                }
                LootParams parameters = new LootParams.Builder((ServerLevel) level).create(LootContextParamSets.EMPTY);
                lootTable.getRandomItems(parameters).forEach(this::spawnAtLocation);
            });
        }
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
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        loadTrophy(tag);
        rewardCooldown = tag.getInt("RewardCooldown");
    }

    public void loadTrophy(CompoundTag tag) {
        trophyID = null;

        if (tag.contains("Trophy", Tag.TAG_STRING)) {
            try {
                trophyID = ResourceLocation.parse(tag.getString("Trophy"));
            } catch (ResourceLocationException exception) {
                Trofers.LOGGER.error(String.format("Failed to load trophy for block entity at %s", getBlockPos()), exception);
            }

            Trophy trophy = ModRegistries.get(ModRegistries.TROPHIES, trophyID);
            if (trophy == null && ModRegistries.trophies().isPresent()) {
                Trofers.LOGGER.error(String.format("Invalid trophy id for block entity at %s: %s", getBlockPos(), trophyID));
            }
        }

        if (getLevel() != null && getLevel().isClientSide()) {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        if (trophyID != null) {
            tag.putString("Trophy", trophyID.toString());
        }
        if (rewardCooldown > 0) {
            tag.putInt("RewardCooldown", rewardCooldown);
        }
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput dataComponentInput) {
        super.applyImplicitComponents(dataComponentInput);
        this.trophyID = dataComponentInput.getOrDefault(ModDataComponents.TROPHY.get(), null);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(ModDataComponents.TROPHY.get(), this.trophyID);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeComponentsFromTag(CompoundTag compoundTag) {
        compoundTag.remove("trophy");
    }
}
