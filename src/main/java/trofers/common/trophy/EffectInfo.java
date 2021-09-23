package trofers.common.trophy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public record EffectInfo(@Nullable SoundInfo sound, RewardInfo rewards) {

    public static final EffectInfo NONE = new EffectInfo(null, RewardInfo.NONE);

    protected void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeBoolean(sound() != null);
        if (sound() != null) {
            sound().toNetwork(buffer);
        }
        rewards().toNetwork(buffer);
    }

    protected static EffectInfo fromNetwork(FriendlyByteBuf buffer) {
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

    protected JsonObject toJson() {
        JsonObject object = new JsonObject();
        if (sound() != null) {
            object.add("sound", sound().toJson());
        }
        if (!rewards().equals(RewardInfo.NONE)) {
            object.add("rewards", rewards().toJson());
        }
        return object;
    }

    protected static EffectInfo fromJson(JsonObject object) {
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

    public static record SoundInfo(SoundEvent soundEvent, float volume, float pitch) {

        protected void toNetwork(FriendlyByteBuf buffer) {
            // noinspection ConstantConditions
            buffer.writeResourceLocation(soundEvent.getRegistryName());
            buffer.writeFloat(volume());
            buffer.writeFloat(pitch());
        }

        protected static SoundInfo fromNetwork(FriendlyByteBuf buffer) {
            return new SoundInfo(
                    ForgeRegistries.SOUND_EVENTS.getValue(buffer.readResourceLocation()),
                    buffer.readFloat(),
                    buffer.readFloat()
            );
        }

        protected JsonObject toJson() {
            JsonObject result = new JsonObject();
            // noinspection ConstantConditions
            result.addProperty("soundEvent", soundEvent().getRegistryName().toString());
            if (volume() != 1) {
                result.addProperty("volume", volume());
            }
            if (pitch() != 1) {
                result.addProperty("pitch", pitch());
            }
            return result;
        }

        protected static SoundInfo fromJson(JsonObject object) {
            ResourceLocation soundEventID = new ResourceLocation(GsonHelper.getAsString(object, "soundEvent"));
            if (!ForgeRegistries.SOUND_EVENTS.containsKey(soundEventID)) {
                throw new JsonParseException(String.format("Unknown sound event: %s", soundEventID));
            }
            SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundEventID);

            float volume = Trophy.readOptionalFloat(object, "volume", 1);
            float pitch = Trophy.readOptionalFloat(object, "pitch", 1);

            return new SoundInfo(soundEvent, volume, pitch);
        }
    }

    public static record RewardInfo(@Nullable ResourceLocation lootTable, CompoundTag statusEffect, int cooldown) {

        public static RewardInfo NONE = new RewardInfo(null, new CompoundTag(), 0);

        @Nullable
        public MobEffectInstance createStatusEffect() {
            if (!statusEffect().isEmpty()) {
                return MobEffectInstance.load(statusEffect());
            }
            return null;
        }

        protected void toNetwork(FriendlyByteBuf buffer) {
            buffer.writeBoolean(lootTable() != null);
            if (lootTable() != null) {
                buffer.writeResourceLocation(lootTable());
            }
            buffer.writeNbt(statusEffect());
            buffer.writeInt(cooldown());
        }

        protected static RewardInfo fromNetwork(FriendlyByteBuf buffer) {
            ResourceLocation lootTable = null;
            if (buffer.readBoolean()) {
                lootTable = buffer.readResourceLocation();
            }
            CompoundTag statusEffect = buffer.readNbt();
            int cooldown = buffer.readInt();
            return new RewardInfo(lootTable, statusEffect, cooldown);
        }

        protected JsonObject toJson() {
            JsonObject result = new JsonObject();
            if (lootTable() != null) {
                result.addProperty("lootTable", lootTable().toString());
            }
            if (!statusEffect().isEmpty()) {
                MobEffectInstance effect = MobEffectInstance.load(statusEffect());
                if (effect != null) {
                    JsonObject statusEffect = new JsonObject();
                    result.add("statusEffect", statusEffect);
                    // noinspection ConstantConditions
                    statusEffect.addProperty("effect", effect.getEffect().getRegistryName().toString());
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

        protected static RewardInfo fromJson(JsonObject object) {
            ResourceLocation lootTable = null;
            if (object.has("lootTable")) {
                lootTable = new ResourceLocation(GsonHelper.getAsString(object, "lootTable"));
            }
            CompoundTag statusEffect = new CompoundTag();
            if (object.has("statusEffect")) {
                JsonObject effectObject = GsonHelper.getAsJsonObject(object, "statusEffect");
                ResourceLocation effectID = new ResourceLocation(GsonHelper.getAsString(effectObject, "effect"));
                if (!ForgeRegistries.MOB_EFFECTS.containsKey(effectID)) {
                    throw new JsonParseException(String.format("Unknown effect: %s", effectID));
                }
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(effectID);
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
}
