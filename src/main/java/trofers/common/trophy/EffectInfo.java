package trofers.common.trophy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public final class EffectInfo {

    public static final EffectInfo NONE = new EffectInfo(null, RewardInfo.NONE);
    @Nullable
    private final SoundInfo sound;
    private final RewardInfo rewards;

    public EffectInfo(@Nullable SoundInfo sound, RewardInfo rewards) {
        this.sound = sound;
        this.rewards = rewards;
    }

    protected void toNetwork(PacketBuffer buffer) {
        buffer.writeBoolean(sound() != null);
        if (sound() != null) {
            sound().toNetwork(buffer);
        }
        rewards().toNetwork(buffer);
    }

    protected static EffectInfo fromNetwork(PacketBuffer buffer) {
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
        if (!rewards().statusEffect().isEmpty() || rewards().lootTable() != null) {
            object.add("rewards", rewards().toJson());
        }
        return object;
    }

    protected static EffectInfo fromJson(JsonObject object) {
        SoundInfo sound = null;
        if (object.has("sound")) {
            sound = SoundInfo.fromJson(JSONUtils.getAsJsonObject(object, "sound"));
        }
        RewardInfo rewards = RewardInfo.NONE;
        if (object.has("rewards")) {
            rewards = RewardInfo.fromJson(JSONUtils.getAsJsonObject(object, "rewards"));
        }
        return new EffectInfo(sound, rewards);
    }

    @Nullable
    public SoundInfo sound() {
        return sound;
    }

    public RewardInfo rewards() {
        return rewards;
    }

    public static final class SoundInfo {
        private final SoundEvent soundEvent;
        private final float volume;
        private final float pitch;

        public SoundInfo(SoundEvent soundEvent, float volume, float pitch) {
            this.soundEvent = soundEvent;
            this.volume = volume;
            this.pitch = pitch;
        }

        protected void toNetwork(PacketBuffer buffer) {
            // noinspection ConstantConditions
            buffer.writeResourceLocation(soundEvent.getRegistryName());
            buffer.writeFloat(volume());
            buffer.writeFloat(pitch());
        }

        protected static SoundInfo fromNetwork(PacketBuffer buffer) {
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
            ResourceLocation soundEventID = new ResourceLocation(JSONUtils.getAsString(object, "soundEvent"));
            if (!ForgeRegistries.SOUND_EVENTS.containsKey(soundEventID)) {
                throw new JsonParseException(String.format("Unknown sound event: %s", soundEventID));
            }
            SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundEventID);

            float volume = Trophy.readOptionalFloat(object, "volume", 1);
            float pitch = Trophy.readOptionalFloat(object, "pitch", 1);

            return new SoundInfo(soundEvent, volume, pitch);
        }

        public SoundEvent soundEvent() {
            return soundEvent;
        }

        public float volume() {
            return volume;
        }

        public float pitch() {
            return pitch;
        }
    }

    public static final class RewardInfo {

        public static RewardInfo NONE = new RewardInfo(null, new CompoundNBT(), 0);
        @Nullable
        private final ResourceLocation lootTable;
        private final CompoundNBT statusEffect;
        private final int cooldown;

        public RewardInfo(@Nullable ResourceLocation lootTable, CompoundNBT potionEffect, int cooldown) {
            this.lootTable = lootTable;
            this.statusEffect = potionEffect;
            this.cooldown = cooldown;
        }

        @Nullable
        public EffectInstance createStatusEffect() {
            if (!statusEffect().isEmpty()) {
                return EffectInstance.load(statusEffect());
            }
            return null;
        }

        protected void toNetwork(PacketBuffer buffer) {
            buffer.writeBoolean(lootTable() != null);
            if (lootTable() != null) {
                buffer.writeResourceLocation(lootTable());
            }
            buffer.writeNbt(statusEffect());
            buffer.writeInt(cooldown());
        }

        protected static RewardInfo fromNetwork(PacketBuffer buffer) {
            ResourceLocation lootTable = null;
            if (buffer.readBoolean()) {
                lootTable = buffer.readResourceLocation();
            }
            CompoundNBT statusEffect = buffer.readNbt();
            int cooldown = buffer.readInt();
            return new RewardInfo(lootTable, statusEffect, cooldown);
        }

        protected JsonObject toJson() {
            JsonObject result = new JsonObject();
            if (lootTable() != null) {
                result.addProperty("lootTable", lootTable().toString());
            }
            if (!statusEffect().isEmpty()) {
                EffectInstance effect = EffectInstance.load(statusEffect());
                // noinspection ConstantConditions
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
                lootTable = new ResourceLocation(JSONUtils.getAsString(object, "lootTable"));
            }
            CompoundNBT statusEffect = new CompoundNBT();
            if (object.has("statusEffect")) {
                JsonObject effectObject = JSONUtils.getAsJsonObject(object, "statusEffect");
                ResourceLocation effectID = new ResourceLocation(JSONUtils.getAsString(effectObject, "effect"));
                if (!ForgeRegistries.POTIONS.containsKey(effectID)) {
                    throw new JsonParseException(String.format("Unknown effect: %s", effectID));
                }
                Effect effect = ForgeRegistries.POTIONS.getValue(effectID);
                int duration = JSONUtils.getAsInt(effectObject, "duration");
                int amplifier = 0;
                if (object.has("amplifier")) {
                    amplifier = JSONUtils.getAsInt(effectObject, "amplifier");
                }

                // noinspection ConstantConditions
                statusEffect = new EffectInstance(effect, duration, amplifier).save(new CompoundNBT());
            }
            int cooldown = 0;
            if (object.has("cooldown")) {
                cooldown = JSONUtils.getAsInt(object, "cooldown");
            }
            return new RewardInfo(lootTable, statusEffect, cooldown);
        }

        @Nullable
        public ResourceLocation lootTable() {
            return lootTable;
        }

        public CompoundNBT statusEffect() {
            return statusEffect;
        }

        public int cooldown() {
            return cooldown;
        }
    }
}
