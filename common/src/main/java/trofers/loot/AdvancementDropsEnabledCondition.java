package trofers.loot;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import trofers.Trofers;
import trofers.registry.ModLootConditions;

public class AdvancementDropsEnabledCondition implements LootItemCondition {

    private static final AdvancementDropsEnabledCondition INSTANCE = new AdvancementDropsEnabledCondition();
    public static final MapCodec<AdvancementDropsEnabledCondition> CODEC = MapCodec.unit(INSTANCE);

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
}
