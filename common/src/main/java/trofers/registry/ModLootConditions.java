package trofers.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import trofers.Trofers;
import trofers.loot.AdvancementDropsEnabledCondition;
import trofers.loot.RandomTrophyChanceCondition;

import java.util.function.Supplier;

public class ModLootConditions {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Trofers.MOD_ID, Registries.LOOT_CONDITION_TYPE);

    public static final RegistrySupplier<LootItemConditionType> RANDOM_TROPHY_CHANCE = register("random_trophy_chance", RandomTrophyChanceCondition.Serializer::new);
    public static final RegistrySupplier<LootItemConditionType> ADVANCEMENT_DROPS_ENABLED = register("advancement_drops_enabled", AdvancementDropsEnabledCondition.Serializer::new);

    private static RegistrySupplier<LootItemConditionType> register(String id, Supplier<Serializer<? extends LootItemCondition>> serializer) {
        return LOOT_CONDITION_TYPES.register(id, () -> new LootItemConditionType(serializer.get()));
    }
}
