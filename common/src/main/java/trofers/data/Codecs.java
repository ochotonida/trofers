package trofers.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.List;

public class Codecs {

    // See IGlobalLootModifier
    public static Codec<LootItemCondition[]> LOOT_CONDITIONS_CODEC = LootItemConditions.CODEC.listOf().xmap(list -> list.toArray(LootItemCondition[]::new), List::of);

    @SuppressWarnings("unchecked")
    static <U> JsonElement getJson(Dynamic<?> dynamic) {
        Dynamic<U> typed = (Dynamic<U>) dynamic;
        return typed.getValue() instanceof JsonElement
                ? (JsonElement) typed.getValue()
                : typed.getOps().convertTo(JsonOps.INSTANCE, typed.getValue());
    }
}
