package trofers.data.trophies;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.*;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.common.trophy.*;

import java.util.*;

public abstract class TrophyBuilder {

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

        List<Trophy> trophies = new ArrayList<>();

        for (EntityType<?> type : getEntities()) {
            // noinspection ConstantConditions
            ResourceLocation id = new ResourceLocation(Trofers.MODID, folder + type.getRegistryName().getPath());
            ITextComponent name = createTrophyName(type, colors.get(type));
            List<ITextComponent> tooltip = Collections.emptyList();
            DisplayInfo displayInfo = displayInfos.get(type);
            Animation animation = Animation.STATIC;
            ItemStack item = ItemStack.EMPTY;
            EntityInfo entityInfo = new EntityInfo(type, entityData.get(type), false);
            ColorInfo colorInfo = new ColorInfo(0x606060, colors.get(type));
            CompoundNBT potionEffect = potionEffects.get(type);
            ResourceLocation lootTable = lootTables.get(type);
            EffectInfo effectInfo = new EffectInfo(
                    new EffectInfo.SoundInfo(soundEvents.get(type), 1, 1),
                    new EffectInfo.RewardInfo(lootTable, potionEffect, cooldowns.get(type))
            );

            trophies.add(new Trophy(id, name, tooltip, displayInfo, animation, item, entityInfo, colorInfo, effectInfo, false));
        }

        return trophies;
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

    public String getModId() {
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
            ResourceLocation soundLocation = getSoundEvent(type);
            if (ForgeRegistries.SOUND_EVENTS.containsKey(soundLocation)) {
                result.put(type, ForgeRegistries.SOUND_EVENTS.getValue(soundLocation));
            } else {
                Trofers.LOGGER.error(String.format("no sound found for %s", type.getRegistryName()));
            }
        });

        return result;
    }

    protected ResourceLocation getSoundEvent(EntityType<?> type) {
        // noinspection ConstantConditions
        return new ResourceLocation(type.getRegistryName().getNamespace(), String.format("entity.%s.ambient", type.getRegistryName().getPath()));
    }

    protected Map<EntityType<?>, CompoundNBT> getPotionEffects() {
        Map<EntityType<?>, CompoundNBT> result = new HashMap<>();

        getEntities().forEach(
                type -> result.put(type, new CompoundNBT())
        );

        return result;
    }

    protected CompoundNBT createEffect(Effect effect, int time, int amplifier) {
        return new EffectInstance(effect, time, amplifier).save(new CompoundNBT());
    }

    protected CompoundNBT createEffect(Effect effect, int time) {
        return createEffect(effect, time, 0);
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

        getPotionEffects().forEach((type, compoundTag) -> {
            if (compoundTag.isEmpty()) {
                // noinspection ConstantConditions
                result.put(type, new ResourceLocation(Trofers.MODID, String.format("trophies/%s", type.getRegistryName().getPath())));
            }
        });

        return result;
    }

    protected void putHandItem(CompoundNBT tag, Item item) {
        tag.put("HandItems", new ListNBT());
        tag.getList("HandItems", Constants.NBT.TAG_COMPOUND).add(new ItemStack(item).save(new CompoundNBT()));
        tag.getList("HandItems", Constants.NBT.TAG_COMPOUND).add(new CompoundNBT());
    }
}
