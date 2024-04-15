package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import trofers.data.ModCodecs;

import java.util.List;
import java.util.function.Function;

public record ColorInfo(int base, int accent) {

    public static final ColorInfo NONE = new ColorInfo(0xFFFFFF, 0xFFFFFF);

    private static final Codec<Integer> HEX_COLOR_CODEC = Codec.STRING.comapFlatMap(
            string -> {
                try {
                    // See TextColor.CODEC
                    int i = Integer.parseInt(string.substring(1), 16);
                    return i >= 0 && i <= 0xFFFFFF
                            ? DataResult.success(i)
                            : DataResult.error(() -> "Color value out of range: " + string);
                } catch (NumberFormatException var2) {
                    return DataResult.error(() -> "Invalid color value: " + string);
                }
            },
            color -> String.format("#%06X", color)
    );

    private static final Codec<Integer> RGB_COLOR_CODEC = ModCodecs.list(Codec.INT.comapFlatMap(
            i -> i >= 0 && i <= 255
                    ? DataResult.success(i)
                    : DataResult.error(() -> "RGB-component value out of range [0, 255]: " + i),
            Function.identity()
    )).comapFlatMap(
            list -> Util.fixedSize(list, 3)
                    .map(colors -> colors.get(0) << 16 | colors.get(1) << 8 | colors.get(2)),
            color -> List.of((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF)
    );

    private static final Codec<Integer> COLOR_CODEC = ModCodecs.withAlternative(HEX_COLOR_CODEC, RGB_COLOR_CODEC);

    public static final Codec<ColorInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ModCodecs.defaultField("base", 0xFFFFFF, COLOR_CODEC).forGetter(ColorInfo::base),
            ModCodecs.defaultField("accent", 0xFFFFFF, COLOR_CODEC).forGetter(ColorInfo::accent)
    ).apply(instance, ColorInfo::new));
}
