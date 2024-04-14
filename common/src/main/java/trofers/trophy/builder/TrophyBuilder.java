package trofers.trophy.builder;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
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

    public Trophy build() {
        return new Trophy(
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

    protected abstract Optional<EntityInfo> getEntityInfo();

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
        displayInfo = new DisplayInfo(new Vec3(xOffset, yOffset, zOffset), displayInfo.rotation(), displayInfo.scale());
        return (T) this;
    }

    public T rotate(double xRotation, double yRotation, double zRotation) {
        displayInfo = new DisplayInfo(displayInfo.offset(), new Vec3(xRotation, yRotation, zRotation), displayInfo.scale());
        return (T) this;
    }

    public T scale(double scale) {
        displayInfo = new DisplayInfo(displayInfo.offset(), displayInfo.rotation(), scale);
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

    private T effectInfo(Optional<EffectInfo.SoundInfo> soundInfo, EffectInfo.RewardInfo rewardInfo) {
        effectInfo = new EffectInfo(soundInfo, rewardInfo);
        return (T) this;
    }

    public T sound(ResourceLocation sound, float volume, float pitch) {
        return effectInfo(Optional.of(new EffectInfo.SoundInfo(sound, volume, pitch)), effectInfo.rewards());
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

    private T rewardInfo(Optional<ResourceLocation> lootTable, Optional<EffectInfo.MobEffectInfo> mobEffect, int cooldown) {
        return effectInfo(effectInfo.sound(), new EffectInfo.RewardInfo(lootTable, mobEffect, cooldown));
    }

    public T lootTable(Optional<ResourceLocation> lootTable) {
        return rewardInfo(lootTable, effectInfo.rewards().mobEffect(), effectInfo.rewards().cooldown());
    }

    public T mobEffect(EffectInfo.MobEffectInfo mobEffect) {
        return rewardInfo(effectInfo.rewards().lootTable(), Optional.of(mobEffect), effectInfo.rewards().cooldown());
    }

    public T mobEffect(MobEffect effect, int timeSeconds, int amplifier) {
        return mobEffect(new EffectInfo.MobEffectInfo(effect, (byte) amplifier, timeSeconds * 20, false, true, true));
    }

    public T mobEffect(MobEffect effect, int timeSeconds) {
        return mobEffect(effect, timeSeconds, 0);
    }

    public T cooldown(int timeSeconds) {
        return rewardInfo(effectInfo.rewards().lootTable(), effectInfo.rewards().mobEffect(), timeSeconds * 20);
    }

    public T setHidden(boolean isHidden) {
        this.isHidden = isHidden;
        return (T) this;
    }

    public T setHidden() {
        return setHidden(true);
    }

    public JsonObject toJson() {
        JsonObject result = Trophy.CODEC.encodeStart(JsonOps.INSTANCE, build())
                .getOrThrow(false, error -> {})
                .getAsJsonObject();

        JsonHelper.addModLoadedConditions(result, requiredMods.toArray(String[]::new));

        return result;
    }
}
