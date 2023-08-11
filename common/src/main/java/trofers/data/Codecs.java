package trofers.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import trofers.Trofers;

public class Codecs {

    public static final Gson FUNCTION_GSON = Deserializers.createFunctionSerializer().create();

    // See IGlobalLootModifier
    public static Codec<LootItemCondition[]> LOOT_CONDITIONS_CODEC = Codec.PASSTHROUGH.flatXmap(
            dynamic -> {
                try {
                    LootItemCondition[] conditions = FUNCTION_GSON.fromJson(getJson(dynamic), LootItemCondition[].class);
                    return DataResult.success(conditions);
                } catch (JsonSyntaxException e) {
                    Trofers.LOGGER.warn("Unable to decode loot conditions", e);
                    return DataResult.error(e::getMessage);
                }
            },
            conditions -> {
                try {
                    JsonElement element = FUNCTION_GSON.toJsonTree(conditions);
                    return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, element));
                } catch (JsonSyntaxException e) {
                    Trofers.LOGGER.warn("Unable to encode loot conditions", e);
                    return DataResult.error(e::getMessage);
                }
            }
    );

    @SuppressWarnings("unchecked")
    static <U> JsonElement getJson(Dynamic<?> dynamic) {
        Dynamic<U> typed = (Dynamic<U>) dynamic;
        return typed.getValue() instanceof JsonElement
                ? (JsonElement) typed.getValue()
                : typed.getOps().convertTo(JsonOps.INSTANCE, typed.getValue());
    }
}
