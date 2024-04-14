package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;

public record DisplayInfo(Vec3 offset, Vec3 rotation, double scale) {

    public static final DisplayInfo NONE = new DisplayInfo(Vec3.ZERO, Vec3.ZERO, 1);

    public static final Codec<DisplayInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(DisplayInfo::offset),
            Vec3.CODEC.optionalFieldOf("rotation", Vec3.ZERO).forGetter(DisplayInfo::rotation),
            Codec.DOUBLE.optionalFieldOf("scale", 0D).forGetter(DisplayInfo::scale)
    ).apply(instance, DisplayInfo::new));
}
