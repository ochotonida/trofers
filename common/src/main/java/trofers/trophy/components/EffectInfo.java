package trofers.trophy.components;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Nullable;
import trofers.util.JsonHelper;

import java.util.Optional;
import java.util.function.Function;

public record EffectInfo(@Nullable SoundInfo sound, RewardInfo rewards) {

    public static final EffectInfo NONE = new EffectInfo(null, RewardInfo.NONE);

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeBoolean(sound() != null);
        if (sound() != null) {
            sound().toNetwork(buffer);
        }
        rewards().toNetwork(buffer);
    }

    public static EffectInfo fromNetwork(FriendlyByteBuf buffer) {
        SoundInfo sound = null;
        if (buffer.readBoolean()) {
            sound = SoundInfo.fromNetwork(buffer);
        }
        RewardInfo rewards = RewardInfo.fromNetwork(buffer);
        return new EffectInfo(
                sound,
                rewards
        );
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        if (sound() != null) {
            object.add("sound", sound().toJson());
        }
        if (!rewards().equals(RewardInfo.NONE)) {
            object.add("rewards", rewards().toJson());
        }
        return object;
    }

    public static EffectInfo fromJson(JsonObject object) {
        SoundInfo sound = null;
        if (object.has("sound")) {
            sound = SoundInfo.fromJson(GsonHelper.getAsJsonObject(object, "sound"));
        }
        RewardInfo rewards = RewardInfo.NONE;
        if (object.has("rewards")) {
            rewards = RewardInfo.fromJson(GsonHelper.getAsJsonObject(object, "rewards"));
        }
        return new EffectInfo(sound, rewards);
    }

    public record SoundInfo(ResourceLocation soundEvent, float volume, float pitch) {

        private void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeResourceLocation(soundEvent());
            buffer.writeFloat(volume());
            buffer.writeFloat(pitch());
        }

        private static SoundInfo fromNetwork(FriendlyByteBuf buffer) {
            return new SoundInfo(
                    buffer.readResourceLocation(),
                    buffer.readFloat(),
                    buffer.readFloat()
            );
        }

        private JsonObject toJson() {
            JsonObject result = new JsonObject();
            result.addProperty("sound_event", soundEvent().toString());
            if (volume() != 1) {
                result.addProperty("volume", volume());
            }
            if (pitch() != 1) {
                result.addProperty("pitch", pitch());
            }
            return result;
        }

        private static SoundInfo fromJson(JsonObject object) {
            ResourceLocation soundEvent = new ResourceLocation(GsonHelper.getAsString(object, "sound_event"));
            float volume = JsonHelper.readOptionalFloat(object, "volume", 1);
            float pitch = JsonHelper.readOptionalFloat(object, "pitch", 1);

            return new SoundInfo(soundEvent, volume, pitch);
        }
    }

    public record RewardInfo(Optional<ResourceLocation> lootTable, CompoundTag statusEffect, int cooldown) {

        public static RewardInfo NONE = new RewardInfo(Optional.empty(), new CompoundTag(), 0);

        @Nullable
        public MobEffectInstance createMobEffect() {
            if (!statusEffect().isEmpty()) {
                return MobEffectInstance.load(statusEffect());
            }
            return null;
        }

        private void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeBoolean(lootTable().isPresent());
            if (lootTable().isPresent()) {
                buffer.writeResourceLocation(lootTable().get());
            }
            buffer.writeNbt(statusEffect());
            buffer.writeInt(cooldown());
        }

        private static RewardInfo fromNetwork(FriendlyByteBuf buffer) {
            Optional<ResourceLocation> lootTable = Optional.empty();
            if (buffer.readBoolean()) {
                lootTable = Optional.of(buffer.readResourceLocation());
            }
            CompoundTag statusEffect = buffer.readNbt();
            int cooldown = buffer.readInt();
            return new RewardInfo(lootTable, statusEffect, cooldown);
        }

        private JsonObject toJson() {
            JsonObject result = new JsonObject();
            if (lootTable().isPresent()) {
                result.addProperty("loot_table", lootTable().get().toString());
            }
            if (!statusEffect().isEmpty()) {
                MobEffectInstance effect = MobEffectInstance.load(statusEffect());
                if (effect != null) {
                    JsonObject statusEffect = new JsonObject();
                    result.add("status_effect", statusEffect);
                    // noinspection ConstantConditions
                    statusEffect.addProperty("effect", BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect()).toString());
                    statusEffect.addProperty("duration", effect.getDuration());
                    if (effect.getAmplifier() != 0) {
                        statusEffect.addProperty("amplifier", effect.getAmplifier());
                    }
                }
            }
            if (cooldown() != 0) {
                result.addProperty("cooldown", cooldown());
            }
            return result;
        }

        private static RewardInfo fromJson(JsonObject object) {
            Optional<ResourceLocation> lootTable = Optional.empty();
            if (object.has("loot_table")) {
                lootTable = Optional.of(new ResourceLocation(GsonHelper.getAsString(object, "loot_table")));
            }
            CompoundTag statusEffect = new CompoundTag();
            if (object.has("status_effect")) {
                JsonObject effectObject = GsonHelper.getAsJsonObject(object, "status_effect");
                ResourceLocation effectID = new ResourceLocation(GsonHelper.getAsString(effectObject, "effect"));
                if (!BuiltInRegistries.MOB_EFFECT.containsKey(effectID)) {
                    throw new JsonParseException(String.format("Unknown effect: %s", effectID));
                }
                MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(effectID);
                int duration = GsonHelper.getAsInt(effectObject, "duration");
                int amplifier = 0;
                if (effectObject.has("amplifier")) {
                    amplifier = GsonHelper.getAsInt(effectObject, "amplifier");
                }

                // noinspection ConstantConditions
                statusEffect = new MobEffectInstance(effect, duration, amplifier).save(new CompoundTag());
            }
            int cooldown = 0;
            if (object.has("cooldown")) {
                cooldown = GsonHelper.getAsInt(object, "cooldown");
            }
            return new RewardInfo(lootTable, statusEffect, cooldown);
        }
    }

    public record MobEffectInfo(MobEffect mobEffect, byte amplifier, int duration, boolean ambient, boolean showParticles, boolean showIcon) {

        private static final Codec<MobEffect> MOB_EFFECT_CODEC = ResourceLocation.CODEC.comapFlatMap(id ->
                        BuiltInRegistries.MOB_EFFECT.containsKey(id)
                                ? DataResult.success(BuiltInRegistries.MOB_EFFECT.get(id))
                                : DataResult.error(() -> String.format("Unknown mob effect %s", id)),
                BuiltInRegistries.MOB_EFFECT::getKey
        );

        private static final Codec<MobEffectInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                MOB_EFFECT_CODEC.fieldOf("id").forGetter(MobEffectInfo::mobEffect),
                Codec.BYTE.comapFlatMap(amplifier -> amplifier >= 0
                        ? DataResult.success(amplifier)
                        : DataResult.error(() -> "Amplifier cannot be negative"),
                        Function.identity()
                ).optionalFieldOf("amplifier", (byte) 0).forGetter(MobEffectInfo::amplifier),
                Codec.INT.fieldOf("duration").forGetter(MobEffectInfo::duration),
                Codec.BOOL.optionalFieldOf("ambient", false).forGetter(MobEffectInfo::ambient),
                Codec.BOOL.optionalFieldOf("show_particles", true).forGetter(MobEffectInfo::showParticles),
                Codec.BOOL.optionalFieldOf("show_icon", true).forGetter(MobEffectInfo::showIcon)
        ).apply(instance, MobEffectInfo::new));

        private CompoundTag asTag() {
            CompoundTag result = new CompoundTag();
            //noinspection ConstantConditions
            result.putString("id", BuiltInRegistries.MOB_EFFECT.getKey(mobEffect).toString());
            result.putByte("amplifier", amplifier);
            result.putInt("duration", duration);
            result.putBoolean("ambient", ambient);
            result.putBoolean("show_particles", showParticles);
            result.putBoolean("show_icon", showIcon);
            return result;
        }

        private MobEffectInstance createMobEffect() {
            return new MobEffectInstance(mobEffect, amplifier, duration, ambient, showParticles, showIcon);
        }
    }
}
