package trofers.neoforge.registry;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import trofers.Trofers;
import trofers.neoforge.loot.AddEntityTrophies;

public class ModLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Trofers.MOD_ID);

    static {
        LOOT_MODIFIERS.register("add_entity_trophies", () -> AddEntityTrophies.CODEC);
    }
}
