package trofers.common.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import trofers.Trofers;
import trofers.common.loot.RandomTrophyChanceCondition;

public class ModLootConditions {

    public static final LootItemConditionType RANDOM_TROPHY_CHANCE = new LootItemConditionType(new RandomTrophyChanceCondition.Serializer());

    @SuppressWarnings("unused")
    public static void register(RegistryEvent<GlobalLootModifierSerializer<?>> event) {
        Registry.register(
                Registry.LOOT_CONDITION_TYPE,
                new ResourceLocation(Trofers.MODID, "random_trophy_chance"),
                ModLootConditions.RANDOM_TROPHY_CHANCE
        );
    }
}
