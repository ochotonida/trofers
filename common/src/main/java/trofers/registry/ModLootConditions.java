package trofers.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import trofers.Trofers;
import trofers.loot.RandomTrophyChanceCondition;

public class ModLootConditions {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Trofers.MOD_ID, Registry.LOOT_ITEM_REGISTRY);

    public static final RegistrySupplier<LootItemConditionType> RANDOM_TROPHY_CHANCE = LOOT_CONDITION_TYPES.register("random_trophy_chance", () -> new LootItemConditionType(new RandomTrophyChanceCondition.Serializer()));
}
