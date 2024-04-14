package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Optional;
import java.util.function.Function;

public record EffectInfo(Optional<SoundInfo> sound, RewardInfo rewards) {

    public static final EffectInfo NONE = new EffectInfo(Optional.empty(), RewardInfo.NONE);

    public static final Codec<EffectInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoundInfo.CODEC.optionalFieldOf("sound").forGetter(EffectInfo::sound),
            RewardInfo.CODEC.optionalFieldOf("rewards", RewardInfo.NONE).forGetter(EffectInfo::rewards)
    ).apply(instance, EffectInfo::new));

    public record SoundInfo(ResourceLocation soundEvent, float volume, float pitch) {

        public static final Codec<SoundInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(SoundInfo::soundEvent),
                Codec.FLOAT.optionalFieldOf("volume", 1F).forGetter(SoundInfo::volume),
                Codec.FLOAT.optionalFieldOf("pitch", 1F).forGetter(SoundInfo::pitch)
        ).apply(instance, SoundInfo::new));
    }

    public record RewardInfo(Optional<ResourceLocation> lootTable, Optional<MobEffectInfo> mobEffect, int cooldown) {

        public static final RewardInfo NONE = new RewardInfo(Optional.empty(), Optional.empty(), 0);

        public static final Codec<RewardInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("loot_table").forGetter(RewardInfo::lootTable),
                MobEffectInfo.CODEC.optionalFieldOf("mob_effect").forGetter(RewardInfo::mobEffect),
                ExtraCodecs.POSITIVE_INT.fieldOf("cooldown").forGetter(RewardInfo::cooldown)
        ).apply(instance, RewardInfo::new));
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

        public MobEffectInstance createInstance() {
            return new MobEffectInstance(mobEffect, amplifier, duration, ambient, showParticles, showIcon);
        }
    }
}
