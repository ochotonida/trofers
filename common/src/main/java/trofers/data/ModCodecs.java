package trofers.data;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class ModCodecs {

    // See IGlobalLootModifier
    public static Codec<LootItemCondition[]> LOOT_CONDITIONS_CODEC = LootItemConditions.CODEC
            .listOf()
            .xmap(list -> list.toArray(LootItemCondition[]::new), List::of);



    public static <T> MapCodec<T> requiredField(String name, Codec<T> codec) {
        return fieldContext(name, codec).fieldOf(name);
    }

    public static <T> MapCodec<T> defaultField(String name, T defaultValue, Codec<T> codec) {
        return ExtraCodecs.strictOptionalField(fieldContext(name, codec), name, defaultValue);
    }

    public static <T> MapCodec<Optional<T>> optionalField(String name, Codec<T> codec) {
        return ExtraCodecs.strictOptionalField(fieldContext(name, codec), name);
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
        return mapError(ExtraCodecs.either(codec, codec2).xmap(
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
