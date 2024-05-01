package trofers.data;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ModCodecs {

    public static final Codec<ItemStack> ITEM_STACK = RecordCodecBuilder.create(instance -> instance.group(
            requiredField("id", BuiltInRegistries.ITEM.holderByNameCodec()).forGetter(ItemStack::getItemHolder),
            defaultField("count", 1, rangedInt(1, 64)).forGetter(ItemStack::getCount),
            defaultField("components", DataComponentPatch.EMPTY, DataComponentPatch.CODEC).forGetter(ItemStack::getComponentsPatch)
    ).apply(instance, ItemStack::new));

    public static Codec<Integer> rangedInt(int min, int max) {
        return Codec.INT.comapFlatMap(
                i -> i >= min && i <= max
                        ? DataResult.success(i)
                        : DataResult.error(() -> "Integer of range [%s, %s]: %s".formatted(min, max, i)),
                Function.identity()
        );
    }

    public static <T> MapCodec<T> requiredField(String name, Codec<T> codec) {
        return fieldContext(name, codec).fieldOf(name);
    }

    public static <T> MapCodec<T> defaultField(String name, T defaultValue, Codec<T> codec) {
        return fieldContext(name, codec).optionalFieldOf(name, defaultValue);
    }

    public static <T> MapCodec<Optional<T>> optionalField(String name, Codec<T> codec) {
        return fieldContext(name, codec).optionalFieldOf(name);
    }

    public static <T> Codec<T> fieldContext(String fieldName, Codec<T> codec) {
        return prefixError(codec, "In field \"%s\": ".formatted(fieldName));
    }

    public static <T> Codec<List<T>> list(Codec<T> codec) {
        return prefixError(codec, "In list: ").listOf();
    }

    public static <T> Codec<T> prefixError(Codec<T> codec, String prefix) {
        return mapError(codec, error -> prefix + error);
    }

    public static <T> Codec<T> withAlternative(Codec<T> codec, Codec<? extends T> codec2) {
        return mapError(Codec.either(codec, codec2).xmap(
                either -> either.map(object -> object, object -> object), Either::left
        ), "Both alternatives failed: [%s]"::formatted);
    }

    public static <T> Codec<T> mapError(Codec<T> codec, UnaryOperator<String> context) {
        return new Codec<>() {
            @Override
            public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
                DataResult<Pair<T, T1>> result = codec.decode(ops, input);
                return result.mapError(context);
            }

            @Override
            public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
                return codec.encode(input, ops, prefix);
            }
        };
    }
}
