package trofers.neoforge.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.neoforge.loot.AddEntityTrophies;
import trofers.neoforge.loot.AddTrophy;

public class ModLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Trofers.MOD_ID);

    static {
        LOOT_MODIFIERS.register("add_entity_trophies", () -> AddEntityTrophies.CODEC);
        LOOT_MODIFIERS.register("add_trophy", AddTrophy.CODEC);
    }
}
