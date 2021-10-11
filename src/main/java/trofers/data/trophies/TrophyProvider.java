package trofers.data.trophies;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.*;
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
        Map<EntityType<?>, CompoundNBT> entityData = getEntityData();
        Map<EntityType<?>, SoundEvent> soundEvents = getSoundEvents();
        Map<EntityType<?>, CompoundNBT> potionEffects = getPotionEffects();
        Map<EntityType<?>, Integer> cooldowns = getCooldowns();
        Map<EntityType<?>, ResourceLocation> lootTables = getLootTables();

        String folder = Trofers.MODID.equals(getModId()) ? "" : getModId() + "/";

        // noinspection ConstantConditions
        return getEntities().stream().map(
                type -> new Trophy(
                        new ResourceLocation(Trofers.MODID, folder + type.getRegistryName().getPath()),
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

    private ITextComponent createTrophyName(EntityType<?> entityType, int color) {
        return TextComponentUtils.mergeStyles(
                new TranslationTextComponent(
                        "trophy.trofers.composed",
                        entityType.getDescription()
                ),
                Style.EMPTY.withColor(Color.fromRgb(color))
        );
    }

    protected String getModId() {
        return Trofers.MODID;
    }

    protected Map<EntityType<?>, DisplayInfo> getDisplayInfos() {
        Map<EntityType<?>, DisplayInfo> result = new HashMap<>();

        getEntities().forEach(
                type -> result.put(type, new DisplayInfo(0, 0, 0, 0, 0, 0, 0.25F))
        );

        return result;
    }

    protected Map<EntityType<?>, CompoundNBT> getEntityData() {
        Map<EntityType<?>, CompoundNBT> result = new HashMap<>();

        getEntities().forEach(
                type -> result.put(type, new CompoundNBT())
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

    protected Map<EntityType<?>, CompoundNBT> getPotionEffects() {
        Map<EntityType<?>, CompoundNBT> result = new HashMap<>();

        getEntities().forEach(
                type -> result.put(type, new CompoundNBT())
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

    protected void putHandItem(CompoundNBT tag, Item item) {
        tag.put("HandItems", new ListNBT());
        tag.getList("HandItems", Constants.NBT.TAG_COMPOUND).add(new ItemStack(item).save(new CompoundNBT()));
        tag.getList("HandItems", Constants.NBT.TAG_COMPOUND).add(new CompoundNBT());
    }
}
