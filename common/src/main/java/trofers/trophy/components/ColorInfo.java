package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record ColorInfo(int base, int accent) {

    public static final ColorInfo NONE = new ColorInfo(0xFFFFFF, 0xFFFFFF);

    private static final Codec<Integer> HEX_COLOR_CODEC = Codec.STRING.comapFlatMap(
            string -> string.startsWith("#") && string.length() == 7
                    ? DataResult.success(Integer.parseInt(string.substring(1), 16))
                    : DataResult.error(() -> "Couldn't parse color string '%s', expected '#rrggbb'"),
            color -> String.format("#%06X", color)
    );

    private static final Codec<Integer> RGB_COLOR_CODEC = Codec.BYTE
            .xmap(Byte::intValue, Integer::byteValue).listOf().comapFlatMap(
                    list -> Util.fixedSize(list, 3)
                            .map(colors -> colors.get(0) << 16 | colors.get(1) << 8 | colors.get(2)),
                    color -> List.of((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF)
            );

    private static final Codec<Integer> COLOR_CODEC = ExtraCodecs.withAlternative(HEX_COLOR_CODEC, RGB_COLOR_CODEC);

    public static final Codec<ColorInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            COLOR_CODEC.optionalFieldOf("base", 0xFFFFFF).forGetter(ColorInfo::base),
            COLOR_CODEC.optionalFieldOf("accent", 0xFFFFFF).forGetter(ColorInfo::accent)
    ).apply(instance, ColorInfo::new));
}
