package trofers.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import trofers.Trofers;
import trofers.registry.ModLootConditions;

public class AdvancementDropsEnabledCondition implements LootItemCondition {

    private static final AdvancementDropsEnabledCondition INSTANCE = new AdvancementDropsEnabledCondition();

    private AdvancementDropsEnabledCondition() { }

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.ADVANCEMENT_DROPS_ENABLED.get();
    }

    @Override
    public boolean test(LootContext context) {
        return Trofers.CONFIG.general.enableAdvancementDrops;
    }

    public static LootItemCondition.Builder advancementDropsEnabled() {
        return () -> INSTANCE;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<AdvancementDropsEnabledCondition> {

        @Override
        public void serialize(JsonObject object, AdvancementDropsEnabledCondition condition, JsonSerializationContext context) { }

        @Override
        public AdvancementDropsEnabledCondition deserialize(JsonObject object, JsonDeserializationContext context) {
            return INSTANCE;
        }
    }
}
