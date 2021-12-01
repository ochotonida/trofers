package trofers.common.init;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import trofers.Trofers;
import trofers.common.loot.AddEntityTrophy;

public class ModLootModifiers {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Trofers.MODID);

    public static final RegistryObject<GlobalLootModifierSerializer<AddEntityTrophy>> ADD_ENTITY_TROPHY = REGISTRY.register("add_entity_trophy", AddEntityTrophy.Serializer::new);

}
