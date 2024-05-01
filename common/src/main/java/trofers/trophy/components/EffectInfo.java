package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.storage.loot.LootTable;
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

    public record RewardInfo(Optional<ResourceKey<LootTable>> lootTable, Optional<MobEffectInfo> mobEffect, int cooldown) {

        public static final RewardInfo NONE = new RewardInfo(Optional.empty(), Optional.empty(), 0);

        public static final Codec<RewardInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ModCodecs.optionalField("loot_table", ResourceKey.codec(Registries.LOOT_TABLE)).forGetter(RewardInfo::lootTable),
                ModCodecs.optionalField("mob_effect", MobEffectInfo.CODEC).forGetter(RewardInfo::mobEffect),
                ModCodecs.defaultField("cooldown", 0, ExtraCodecs.NON_NEGATIVE_INT).forGetter(RewardInfo::cooldown)
        ).apply(instance, RewardInfo::new));
    }

    public record MobEffectInfo(MobEffect mobEffect, byte amplifier, int duration, boolean ambient, boolean showParticles, boolean showIcon) {

        private static final Codec<MobEffectInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ModCodecs.requiredField("id", BuiltInRegistries.MOB_EFFECT.byNameCodec()).forGetter(MobEffectInfo::mobEffect),
                ModCodecs.defaultField("amplifier", (byte) 0, ModCodecs.rangedInt(0, 127)
                        .xmap(Integer::byteValue, Byte::intValue)
                ).forGetter(MobEffectInfo::amplifier),
                ModCodecs.requiredField("duration", ExtraCodecs.POSITIVE_INT).forGetter(MobEffectInfo::duration),
                ModCodecs.defaultField("ambient", false, Codec.BOOL).forGetter(MobEffectInfo::ambient),
                ModCodecs.defaultField("show_particles", true, Codec.BOOL).forGetter(MobEffectInfo::showParticles),
                ModCodecs.defaultField("show_icon", true, Codec.BOOL).forGetter(MobEffectInfo::showIcon)
        ).apply(instance, MobEffectInfo::new));

        public MobEffectInstance createInstance() {
            return new MobEffectInstance(Holder.direct(mobEffect), amplifier, duration, ambient, showParticles, showIcon);
        }
    }
}
