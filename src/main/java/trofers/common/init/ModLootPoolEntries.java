package trofers.common.init;

import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import trofers.Trofers;
import trofers.common.loot.OptionalLootItem;

public class ModLootPoolEntries {

    public static final LootPoolEntryType OPTIONAL_ITEM = new LootPoolEntryType(new OptionalLootItem.Serializer());

    @SuppressWarnings("unused")
    public static void register(RegistryEvent<GlobalLootModifierSerializer<?>> event) {
        Registry.register(
                Registry.LOOT_POOL_ENTRY_TYPE,
                new ResourceLocation(Trofers.MODID, "optional_item"),
                OPTIONAL_ITEM
        );
    }
}
