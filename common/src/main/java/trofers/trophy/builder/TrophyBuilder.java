package trofers.trophy.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.trophy.Trophy;
import trofers.trophy.components.*;
import trofers.util.JsonHelper;

import java.util.*;

@SuppressWarnings({"unchecked", "unused", "UnusedReturnValue"})
public abstract class TrophyBuilder<T extends TrophyBuilder<T>> {

    private Optional<Component> name = Optional.empty();
    private final List<Component> tooltipLines = new ArrayList<>();
    private DisplayInfo displayInfo = DisplayInfo.NONE;
    private Animation animation = Animation.STATIC;
    private ColorInfo colorInfo = ColorInfo.NONE;
    private EffectInfo effectInfo = EffectInfo.NONE;
    private boolean isHidden = false;

    private final Set<String> requiredMods = new HashSet<>();

    public Trophy build(ResourceLocation id) {
        return new Trophy(
                id,
                name,
                tooltipLines,
                displayInfo,
                animation,
                getDisplayItem(),
                getEntityInfo(),
                colorInfo,
                effectInfo,
                isHidden
        );
    }

    protected abstract ItemStack getDisplayItem();

    protected abstract void displayItemToJson(JsonObject result);

    protected abstract Optional<EntityInfo> getEntityInfo();

    protected abstract void entityInfoToJson(JsonObject result);

    public T requiresMod(String modId) {
        if (!modId.equals(Trofers.MOD_ID) && !modId.equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            requiredMods.add(modId);
        }
        return (T) this;
    }

    public T name(@Nullable Component name) {
        this.name = Optional.ofNullable(name);
        return (T) this;
    }

    public T addTooltipLine(Component component) {
        tooltipLines.add(component);
        return (T) this;
    }

    public T offset(double xOffset, double yOffset, double zOffset) {
        displayInfo = new DisplayInfo((float) xOffset, (float) yOffset, (float) zOffset, displayInfo.xRotation(), displayInfo.yRotation(), displayInfo.zRotation(), displayInfo.scale());
        return (T) this;
    }

    public T rotate(double xRotation, double yRotation, double zRotation) {
        displayInfo = new DisplayInfo(displayInfo.xOffset(), displayInfo.yOffset(), displayInfo.zOffset(), (float) xRotation, (float) yRotation, (float) zRotation, displayInfo.scale());
        return (T) this;
    }

    public T scale(double scale) {
        displayInfo = new DisplayInfo(displayInfo.xOffset(), displayInfo.yOffset(), displayInfo.zOffset(), displayInfo.xRotation(), displayInfo.yRotation(), displayInfo.zRotation(), (float) scale);
        return (T) this;
    }

    public T animation(Animation.Type type, float speed) {
        animation = new Animation(type, speed);
        return (T) this;
    }

    public T animation(Animation.Type type) {
        return animation(type, 1);
    }

    public T color(int baseColor, int accentColor) {
        colorInfo = new ColorInfo(baseColor, accentColor);
        return (T) this;
    }

    public T baseColor(int color) {
        return color(color, colorInfo.accent());
    }

    public T accentColor(int color) {
        return color(colorInfo.base(), color);
    }

    private T effectInfo(@Nullable EffectInfo.SoundInfo soundInfo, EffectInfo.RewardInfo rewardInfo) {
        effectInfo = new EffectInfo(soundInfo, rewardInfo);
        return (T) this;
    }

    public T sound(ResourceLocation sound, float volume, float pitch) {
        return effectInfo(new EffectInfo.SoundInfo(sound, volume, pitch), effectInfo.rewards());
    }

    public T sound(ResourceLocation sound) {
        return sound(sound, 1, 1);
    }

    public T sound(SoundEvent sound, float volume, float pitch) {
        return sound(sound.getLocation(), volume, pitch);
    }

    public T sound(SoundEvent soundEvent) {
        return sound(soundEvent, 1, 1);
    }

    private T rewardInfo(@Nullable ResourceLocation lootTable, CompoundTag statusEffect, int cooldown) {
        return effectInfo(effectInfo.sound(), new EffectInfo.RewardInfo(lootTable, statusEffect, cooldown));
    }

    public T lootTable(@Nullable ResourceLocation lootTable) {
        return rewardInfo(lootTable, effectInfo.rewards().statusEffect(), effectInfo.rewards().cooldown());
    }

    public T mobEffect(CompoundTag mobEffect) {
        return rewardInfo(effectInfo.rewards().lootTable(), mobEffect, effectInfo.rewards().cooldown());
    }

    public T mobEffect(MobEffect effect, int timeSeconds, int amplifier) {
        return mobEffect(new MobEffectInstance(effect, timeSeconds * 20, amplifier).save(new CompoundTag()));
    }

    public T mobEffect(MobEffect effect, int timeSeconds) {
        return mobEffect(effect, timeSeconds, 0);
    }

    public T cooldown(int timeSeconds) {
        return rewardInfo(effectInfo.rewards().lootTable(), effectInfo.rewards().statusEffect(), timeSeconds * 20);
    }

    public T setHidden(boolean isHidden) {
        this.isHidden = isHidden;
        return (T) this;
    }

    public T setHidden() {
        return setHidden(true);
    }

    public JsonObject toJson() {
        return toJson(new JsonObject());
    }

    public JsonObject toJson(JsonObject result) {
        JsonHelper.addModLoadedConditions(result, requiredMods.toArray(String[]::new));

        name.ifPresent(component -> result.add("name", Component.Serializer.toJsonTree(component)));

        if (!tooltipLines.isEmpty()) {
            JsonArray tooltip = new JsonArray();
            result.add("tooltip", tooltip);
            for (Component line : tooltipLines) {
                tooltip.add(Component.Serializer.toJsonTree(line));
            }
        }

        if (!displayInfo.equals(DisplayInfo.NONE)) {
            result.add("display", displayInfo.toJson());
        }

        if (!animation.type().equals(Animation.Type.FIXED)) {
            result.add("animation", animation.toJson());
        }

        displayItemToJson(result);
        entityInfoToJson(result);

        if (!colorInfo.equals(ColorInfo.NONE)) {
            result.add("colors", colorInfo.toJson());
        }

        if (!effectInfo.equals(EffectInfo.NONE)) {
            result.add("effects", effectInfo.toJson());
        }

        if (isHidden) {
            result.addProperty("isHidden", true);
        }

        return result;
    }
}
