package trofers.data;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.List;

public class Codecs {

    // See IGlobalLootModifier
    public static Codec<LootItemCondition[]> LOOT_CONDITIONS_CODEC = LootItemConditions.CODEC.listOf().xmap(list -> list.toArray(LootItemCondition[]::new), List::of);

}
