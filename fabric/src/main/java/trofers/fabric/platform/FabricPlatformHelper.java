package trofers.fabric.platform;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.loot.IGlobalLootModifier;
import io.github.fabricators_of_create.porting_lib.loot.PortingLibLoot;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import trofers.Trofers;
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
        return IGlobalLootModifier.LOOT_CONDITIONS_CODEC;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AbstractLootModifier> Supplier<Codec<T>> registerLootModifier(String id, Supplier<Codec<T>> codec) {
        Codec<T> c = (Codec<T>) Registry.register(PortingLibLoot.GLOBAL_LOOT_MODIFIER_SERIALIZERS.get(), Trofers.id(id), (Codec<? extends IGlobalLootModifier>) codec.get());
        return () -> c;
    }
}
