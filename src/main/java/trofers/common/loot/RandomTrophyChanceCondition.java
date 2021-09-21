package trofers.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import trofers.common.config.ModConfig;
import trofers.common.init.ModLootConditions;

public class RandomTrophyChanceCondition implements ILootCondition {

    private static final RandomTrophyChanceCondition INSTANCE = new RandomTrophyChanceCondition();

    private RandomTrophyChanceCondition() { }

    public LootConditionType getType() {
        return ModLootConditions.RANDOM_TROPHY_CHANCE;
    }

    public boolean test(LootContext context) {
        return context.getRandom().nextDouble() < ModConfig.common.trophyChance.get();
    }

    public static ILootCondition.IBuilder randomTrophyChance() {
        return () -> INSTANCE;
    }

    public static class Serializer implements ILootSerializer<RandomTrophyChanceCondition> {

        public void serialize(JsonObject object, RandomTrophyChanceCondition condition, JsonSerializationContext context) { }

        @Override
        public RandomTrophyChanceCondition deserialize(JsonObject object, JsonDeserializationContext context) {
            return INSTANCE;
        }
    }
}
