package trofers.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import trofers.Trofers;
import trofers.registry.ModLootConditions;

public class RandomTrophyChanceCondition implements LootItemCondition {

    private static final RandomTrophyChanceCondition INSTANCE = new RandomTrophyChanceCondition();

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

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<RandomTrophyChanceCondition> {

        public void serialize(JsonObject object, RandomTrophyChanceCondition condition, JsonSerializationContext context) { }

        @Override
        public RandomTrophyChanceCondition deserialize(JsonObject object, JsonDeserializationContext context) {
            return INSTANCE;
        }
    }
}
