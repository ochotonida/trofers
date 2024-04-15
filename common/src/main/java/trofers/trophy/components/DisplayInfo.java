package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;
import trofers.data.ModCodecs;

public record DisplayInfo(Vec3 offset, Vec3 rotation, double scale) {

    public static final DisplayInfo NONE = new DisplayInfo(Vec3.ZERO, Vec3.ZERO, 1);

    public static final Codec<DisplayInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ModCodecs.defaultField("offset", Vec3.ZERO, Vec3.CODEC).forGetter(DisplayInfo::offset),
            ModCodecs.defaultField("rotation", Vec3.ZERO, Vec3.CODEC).forGetter(DisplayInfo::rotation),
            ModCodecs.defaultField("scale", 0D, Codec.DOUBLE).forGetter(DisplayInfo::scale)
    ).apply(instance, DisplayInfo::new));
}
