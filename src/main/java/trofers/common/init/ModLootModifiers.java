package trofers.common.init;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.common.loot.AddItemLootModifier;

public class ModLootModifiers {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Trofers.MODID);

    public static final RegistryObject<GlobalLootModifierSerializer<AddItemLootModifier>> ADD_ITEM = REGISTRY.register("add_item", AddItemLootModifier.Serializer::new);

}
