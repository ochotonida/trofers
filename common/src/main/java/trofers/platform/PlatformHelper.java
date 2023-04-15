package trofers.platform;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import trofers.loot.AbstractLootModifier;

import java.util.function.Supplier;

public interface PlatformHelper {

    boolean matchesConditions(JsonObject object);

    Codec<LootItemCondition[]> getLootConditionsCodec();

    <T extends AbstractLootModifier> Supplier<Codec<T>> registerLootModifier(String id, Supplier<Codec<T>> codec);
}
