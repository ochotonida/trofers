package trofers.common.init;

import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import trofers.Trofers;
import trofers.common.loot.RandomTrophyChanceCondition;

public class ModLootConditions {

    public static final LootConditionType RANDOM_TROPHY_CHANCE = new LootConditionType(new RandomTrophyChanceCondition.Serializer());

    public static void register(RegistryEvent<GlobalLootModifierSerializer<?>> event) {
        Registry.register(
                Registry.LOOT_CONDITION_TYPE,
                new ResourceLocation(Trofers.MODID, "random_trophy_chance"),
                ModLootConditions.RANDOM_TROPHY_CHANCE
        );
    }
}
