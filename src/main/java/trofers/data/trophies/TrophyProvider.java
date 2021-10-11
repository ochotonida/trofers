package trofers.data.trophies;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.common.trophy.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TrophyProvider {

    protected Map<EntityType<?>, Integer> getColors() {
        return new HashMap<>();
    }

    protected abstract List<EntityType<?>> getEntities();

    public List<Trophy> createTrophies() {
        Map<EntityType<?>, Integer> colors = getColors();
        Map<EntityType<?>, DisplayInfo> displayInfos = getDisplayInfos();
        Map<EntityType<?>, CompoundTag> entityData = getEntityData();
        Map<EntityType<?>, SoundEvent> soundEvents = getSoundEvents();
        Map<EntityType<?>, CompoundTag> potionEffects = getPotionEffects();
        Map<EntityType<?>, Integer> cooldowns = getCooldowns();
        Map<EntityType<?>, ResourceLocation> lootTables = getLootTables();

        // noinspection ConstantConditions
        return getEntities().stream().map(
                type -> new Trophy(
                        new ResourceLocation(Trofers.MODID, type.getRegistryName().getPath()),
                        createTrophyName(type, colors.get(type)),
                        Collections.emptyList(),
                        displayInfos.get(type),
                        Animation.STATIC,
                        ItemStack.EMPTY,
                        new EntityInfo(type, entityData.get(type), false),
                        new ColorInfo(0x606060, colors.get(type)),
                        new EffectInfo(
                                new EffectInfo.SoundInfo(soundEvents.get(type), 1, 1),
                                new EffectInfo.RewardInfo(
                                        potionEffects.get(type).isEmpty() ? lootTables.get(type) : null,
                                        potionEffects.get(type),
                                        cooldowns.get(type)
                                )
                        ),
                        false
                )
        ).collect(Collectors.toList());
    }

    private Component createTrophyName(EntityType<?> entityType, int color) {
        return ComponentUtils.mergeStyles(
                new TranslatableComponent(
                        "trophy.trofers.composed",
                        entityType.getDescription()
                ),
                Style.EMPTY.withColor(TextColor.fromRgb(color))
        );
    }

    protected Map<EntityType<?>, DisplayInfo> getDisplayInfos() {
        Map<EntityType<?>, DisplayInfo> result = new HashMap<>();

        getEntities().forEach(
                type -> result.put(type, new DisplayInfo(0, 0, 0, 0, 0, 0, 0.25F))
        );

        return result;
    }

    protected Map<EntityType<?>, CompoundTag> getEntityData() {
        Map<EntityType<?>, CompoundTag> result = new HashMap<>();

        getEntities().forEach(
                type -> result.put(type, new CompoundTag())
        );

        return result;
    }

    protected Map<EntityType<?>, SoundEvent> getSoundEvents() {
        Map<EntityType<?>, SoundEvent> result = new HashMap<>();

        getEntities().forEach(type -> {
            // noinspection ConstantConditions
            ResourceLocation soundLocation = new ResourceLocation(type.getRegistryName().getNamespace(), String.format("entity.%s.ambient", type.getRegistryName().getPath()));
            if (ForgeRegistries.SOUND_EVENTS.containsKey(soundLocation)) {
                result.put(type, ForgeRegistries.SOUND_EVENTS.getValue(soundLocation));
            } else {
                Trofers.LOGGER.error(String.format("no sound found for %s", type.getRegistryName()));
            }
        });

        return result;
    }

    protected Map<EntityType<?>, CompoundTag> getPotionEffects() {
        Map<EntityType<?>, CompoundTag> result = new HashMap<>();

        getEntities().forEach(
                type -> result.put(type, new CompoundTag())
        );

        return result;
    }

    protected Map<EntityType<?>, Integer> getCooldowns() {
        Map<EntityType<?>, Integer> result = new HashMap<>();

        getEntities().forEach(
                type -> result.put(type, 20 * 60 * 8)
        );

        return result;
    }

    protected Map<EntityType<?>, ResourceLocation> getLootTables() {
        Map<EntityType<?>, ResourceLocation> result = new HashMap<>();

        // noinspection ConstantConditions
        getEntities().forEach(
                type -> result.put(type, new ResourceLocation(Trofers.MODID, String.format("trophies/%s", type.getRegistryName().getPath())))
        );

        return result;
    }

    protected void putHandItem(CompoundTag tag, Item item) {
        tag.put("HandItems", new ListTag());
        tag.getList("HandItems", Constants.NBT.TAG_COMPOUND).add(new ItemStack(item).save(new CompoundTag()));
        tag.getList("HandItems", Constants.NBT.TAG_COMPOUND).add(new CompoundTag());
    }
}
