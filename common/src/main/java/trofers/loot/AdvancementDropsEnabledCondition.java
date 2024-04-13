package trofers.loot;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import trofers.Trofers;
import trofers.registry.ModLootConditions;

public class AdvancementDropsEnabledCondition implements LootItemCondition {

    private static final AdvancementDropsEnabledCondition INSTANCE = new AdvancementDropsEnabledCondition();
    public static final Codec<AdvancementDropsEnabledCondition> CODEC = Codec.unit(INSTANCE);

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
