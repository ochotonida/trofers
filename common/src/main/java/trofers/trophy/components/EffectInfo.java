package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import trofers.data.ModCodecs;

import java.util.Optional;

public record EffectInfo(Optional<SoundInfo> sound, RewardInfo rewards) {

    public static final EffectInfo NONE = new EffectInfo(Optional.empty(), RewardInfo.NONE);

    public static final Codec<EffectInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ModCodecs.optionalField("sound", SoundInfo.CODEC).forGetter(EffectInfo::sound),
            ModCodecs.defaultField("rewards", RewardInfo.NONE, RewardInfo.CODEC).forGetter(EffectInfo::rewards)
    ).apply(instance, EffectInfo::new));

    public record SoundInfo(ResourceLocation soundEvent, float volume, float pitch) {

        public static final Codec<SoundInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ModCodecs.requiredField("id", ResourceLocation.CODEC).forGetter(SoundInfo::soundEvent),
                ModCodecs.defaultField("volume", 1F, Codec.FLOAT).forGetter(SoundInfo::volume),
                ModCodecs.defaultField("pitch", 1F, Codec.FLOAT).forGetter(SoundInfo::pitch)
        ).apply(instance, SoundInfo::new));
    }

    public record RewardInfo(Optional<ResourceLocation> lootTable, Optional<MobEffectInfo> mobEffect, int cooldown) {

        public static final RewardInfo NONE = new RewardInfo(Optional.empty(), Optional.empty(), 0);

        public static final Codec<RewardInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ModCodecs.optionalField("loot_table", ResourceLocation.CODEC).forGetter(RewardInfo::lootTable),
                ModCodecs.optionalField("mob_effect", MobEffectInfo.CODEC).forGetter(RewardInfo::mobEffect),
                ModCodecs.defaultField("cooldown", 0, ExtraCodecs.NON_NEGATIVE_INT).forGetter(RewardInfo::cooldown)
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
                ModCodecs.requiredField("id", MOB_EFFECT_CODEC).forGetter(MobEffectInfo::mobEffect),
                ModCodecs.defaultField("amplifier", (byte) 0,
                        Codec.INT.comapFlatMap(
                                amplifier -> amplifier >= 0 && amplifier <= 127
                                        ? DataResult.success(amplifier.byteValue())
                                        : DataResult.error(() -> "Amplifier out of range [0, 127]: " + amplifier),
                                Byte::intValue
                        )
                ).forGetter(MobEffectInfo::amplifier),
                ModCodecs.requiredField("duration", ExtraCodecs.POSITIVE_INT).forGetter(MobEffectInfo::duration),
                ModCodecs.defaultField("ambient", false, Codec.BOOL).forGetter(MobEffectInfo::ambient),
                ModCodecs.defaultField("show_particles", true, Codec.BOOL).forGetter(MobEffectInfo::showParticles),
                ModCodecs.defaultField("show_icon", true, Codec.BOOL).forGetter(MobEffectInfo::showIcon)
        ).apply(instance, MobEffectInfo::new));

        public MobEffectInstance createInstance() {
            return new MobEffectInstance(mobEffect, amplifier, duration, ambient, showParticles, showIcon);
        }
    }
}
