package trofers.common.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
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
