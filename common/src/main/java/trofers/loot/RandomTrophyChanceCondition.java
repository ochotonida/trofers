package trofers.loot;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import trofers.Trofers;
import trofers.registry.ModLootConditions;

public class RandomTrophyChanceCondition implements LootItemCondition {

    private static final RandomTrophyChanceCondition INSTANCE = new RandomTrophyChanceCondition();
    public static final Codec<RandomTrophyChanceCondition> CODEC = Codec.unit(INSTANCE);

    private RandomTrophyChanceCondition() { }

    public LootItemConditionType getType() {
        return ModLootConditions.RANDOM_TROPHY_CHANCE.get();
    }

    public boolean test(LootContext context) {
        return context.getRandom().nextDouble() < Trofers.CONFIG.general.getTrophyChance();
    }

    public static LootItemCondition.Builder randomTrophyChance() {
        return () -> INSTANCE;
    }
}
