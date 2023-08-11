package trofers.platform;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import trofers.loot.AbstractLootModifier;

import java.util.function.Supplier;

public interface PlatformHelper {

    Codec<LootItemCondition[]> getLootConditionsCodec();

    <T extends AbstractLootModifier> Supplier<Codec<T>> registerLootModifier(String id, Supplier<Codec<T>> codec);
}
