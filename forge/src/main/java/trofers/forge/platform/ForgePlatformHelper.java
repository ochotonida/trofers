package trofers.forge.platform;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.loot.AbstractLootModifier;
import trofers.platform.PlatformHelper;

import java.util.function.Supplier;

public class ForgePlatformHelper implements PlatformHelper {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Trofers.MOD_ID);

    @Override
    public Codec<LootItemCondition[]> getLootConditionsCodec() {
        return IGlobalLootModifier.LOOT_CONDITIONS_CODEC;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AbstractLootModifier> Supplier<Codec<T>> registerLootModifier(String id, Supplier<Codec<T>> codec) {
        return (Supplier<Codec<T>>) ((Object) LOOT_MODIFIERS.register(id, (Supplier<Codec<? extends IGlobalLootModifier>>) (Object) codec));
    }
}
