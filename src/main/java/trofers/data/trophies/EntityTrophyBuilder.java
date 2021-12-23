package trofers.data.trophies;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.common.trophy.*;

import java.util.Collections;

@SuppressWarnings("UnusedReturnValue")
public class EntityTrophyBuilder {

    private final EntityType<?> entityType;
    private final int color;
    private final CompoundTag entityTag = new CompoundTag();
    private Vec3 offset = Vec3.ZERO;
    private Vec3 rotation = Vec3.ZERO;
    private double scale = 0.25;
    private SoundEvent soundEvent;
    private CompoundTag effect = new CompoundTag();
    private int cooldown = 20 * 60 * 8;
    private ResourceLocation lootTable;

    public EntityTrophyBuilder(EntityType<?> entityType, int color) {
        this.entityType = entityType;
        this.color = color;
    }

    public EntityTrophyBuilder offset(double x, double y, double z) {
        offset = new Vec3(x, y, z);
        return this;
    }

    public EntityTrophyBuilder rotate(double x, double y, double z) {
        rotation = new Vec3(x, y, z);
        return this;
    }

    public EntityTrophyBuilder scale(double scale) {
        this.scale = scale;
        return this;
    }

    public CompoundTag getTag() {
        return entityTag;
    }

    public CompoundTag getTag(String id) {
        if (!entityTag.contains(id, Tag.TAG_COMPOUND)) {
            entityTag.put(id, new CompoundTag());
        }
        return entityTag.getCompound(id);
    }

    public EntityTrophyBuilder putHandItem(Item item) {
        getTag().put("HandItems", new ListTag());
        getTag().getList("HandItems", Tag.TAG_COMPOUND).add(new ItemStack(item).save(new CompoundTag()));
        getTag().getList("HandItems", Tag.TAG_COMPOUND).add(new CompoundTag());
        return this;
    }

    public EntityTrophyBuilder sound(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        return this;
    }

    public EntityTrophyBuilder effect(MobEffect effect, int time) {
        return effect(effect, time, 0);
    }

    public EntityTrophyBuilder effect(MobEffect effect, int time, int amplifier) {
        this.effect = new MobEffectInstance(effect, time, amplifier).save(new CompoundTag());
        return this;
    }

    public EntityTrophyBuilder cooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public EntityTrophyBuilder lootTable(String lootTable) {
        return lootTable(new ResourceLocation(lootTable));
    }

    public EntityTrophyBuilder lootTable(ResourceLocation lootTable) {
        this.lootTable = lootTable;
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    public String getModId() {
        return entityType.getRegistryName().getNamespace();
    }

    public Trophy createTrophy() {
        EntityInfo entityInfo = new EntityInfo(entityType, getTag(), false);
        ColorInfo colorInfo = new ColorInfo(0x606060, color);
        EffectInfo effectInfo = new EffectInfo(
                new EffectInfo.SoundInfo(getSoundEvent(), 1, 1),
                new EffectInfo.RewardInfo(getLootTable(), effect, cooldown)
        );

        return new Trophy(
                createId(),
                createTrophyName(),
                Collections.emptyList(),
                createDisplayInfo(),
                Animation.STATIC,
                ItemStack.EMPTY,
                entityInfo,
                colorInfo,
                effectInfo,
                false
        );
    }

    @SuppressWarnings("ConstantConditions")
    protected ResourceLocation createId() {
        String folder = "";
        if (!entityType.getRegistryName().getNamespace().equals("minecraft")) {
            folder = entityType.getRegistryName().getNamespace() + "/";
        }
        return new ResourceLocation(Trofers.MODID, folder + entityType.getRegistryName().getPath());
    }

    protected Component createTrophyName() {
        return ComponentUtils.mergeStyles(
                new TranslatableComponent(
                        "trophy.trofers.composed",
                        entityType.getDescription()
                ),
                Style.EMPTY.withColor(TextColor.fromRgb(color))
        );
    }

    protected DisplayInfo createDisplayInfo() {
        return new DisplayInfo(
                (float) offset.x, (float) offset.y, (float) offset.z,
                (float) rotation.x, (float) rotation.y, (float) rotation.z,
                (float) scale
        );
    }

    protected SoundEvent getSoundEvent() {
        if (soundEvent == null) {
            ResourceLocation soundId = getDefaultSoundEventId();
            if (!ForgeRegistries.SOUND_EVENTS.containsKey(soundId)) {
                throw new IllegalStateException("No ambient sound for entity " + entityType.getRegistryName());
            }
            soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundId);
        }
        return soundEvent;
    }

    @SuppressWarnings("ConstantConditions")
    protected ResourceLocation getDefaultSoundEventId() {
        ResourceLocation entityId = entityType.getRegistryName();
        return new ResourceLocation(entityId.getNamespace(), "entity.%s.ambient".formatted(entityId.getPath()));
    }

    @SuppressWarnings("ConstantConditions")
    protected ResourceLocation getLootTable() {
        if (!effect.isEmpty() || lootTable != null) {
            return lootTable;
        }
        ResourceLocation entityId = entityType.getRegistryName();
        String modId = entityId.getNamespace().equals("minecraft") ? "" : entityId.getNamespace() + "/";
        return new ResourceLocation(Trofers.MODID, String.format("trophies/%s", modId + entityType.getRegistryName().getPath()));
    }
}
