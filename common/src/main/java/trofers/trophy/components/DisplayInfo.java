package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;

public record DisplayInfo(Vec3 offset, Vec3 rotation, double scale) {

    public static final DisplayInfo NONE = new DisplayInfo(Vec3.ZERO, Vec3.ZERO, 1);

    public static final Codec<DisplayInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(Vec3.CODEC, "offset", Vec3.ZERO).forGetter(DisplayInfo::offset),
            ExtraCodecs.strictOptionalField(Vec3.CODEC, "rotation", Vec3.ZERO).forGetter(DisplayInfo::rotation),
            ExtraCodecs.strictOptionalField(Codec.DOUBLE, "scale", 0D).forGetter(DisplayInfo::scale)
    ).apply(instance, DisplayInfo::new));
}
