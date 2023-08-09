package trofers.fabric.platform;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import trofers.loot.AbstractLootModifier;
import trofers.platform.PlatformHelper;

import java.util.function.Supplier;

public class FabricPlatformHelper implements PlatformHelper {

    @Override
    public boolean matchesConditions(JsonObject object) {
        return true;
    }

    @Override
    public Codec<LootItemCondition[]> getLootConditionsCodec() {
        return null;
    }

    @Override
    public <T extends AbstractLootModifier> Supplier<Codec<T>> registerLootModifier(String id, Supplier<Codec<T>> codec) {
        return () -> null;
    }
}
