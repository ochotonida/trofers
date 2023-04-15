package trofers.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import trofers.Trofers;
import trofers.block.TrophyBlock;
import trofers.item.TrophyItem;

@SuppressWarnings("unused")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Trofers.MOD_ID, Registries.ITEM);

    private static ItemStack makeIcon() {
        ItemStack result = new ItemStack(ModBlocks.MEDIUM_PILLAR.get());
        result.getOrCreateTagElement("BlockEntityTag")
                .putString("Trophy", new ResourceLocation(Trofers.MOD_ID, "panda").toString());
        return result;
    }

    public static final RegistrySupplier<TrophyItem> SMALL_PILLAR = addTrophy(ModBlocks.SMALL_PILLAR);
    public static final RegistrySupplier<TrophyItem> MEDIUM_PILLAR = addTrophy(ModBlocks.MEDIUM_PILLAR);
    public static final RegistrySupplier<TrophyItem> LARGE_PILLAR = addTrophy(ModBlocks.LARGE_PILLAR);
    public static final RegistrySupplier<TrophyItem> SMALL_PLATE = addTrophy(ModBlocks.SMALL_PLATE);
    public static final RegistrySupplier<TrophyItem> MEDIUM_PLATE = addTrophy(ModBlocks.MEDIUM_PLATE);
    public static final RegistrySupplier<TrophyItem> LARGE_PLATE = addTrophy(ModBlocks.LARGE_PLATE);

    @SuppressWarnings("UnstableApiUsage")
    public static final CreativeTabRegistry.TabSupplier CREATIVE_TAB = CreativeTabRegistry.create(new ResourceLocation(Trofers.MOD_ID, "main"),
            builder -> builder
                    .icon(ModItems::makeIcon)
                    .title(Component.translatable("%s.creative_tab".formatted(Trofers.MOD_ID)))
                    .displayItems(
                            (itemDisplayParameters, output) -> BuiltInRegistries.ITEM.keySet()
                                    .stream()
                                    .filter(key -> key.getNamespace().equals(Trofers.MOD_ID))
                                    .sorted()
                                    .map(BuiltInRegistries.ITEM::get)
                                    .filter(item -> item.requiredFeatures().isSubsetOf(itemDisplayParameters.enabledFeatures()))
                                    .forEach(output::accept)
                    )
    );

    private static RegistrySupplier<TrophyItem> addTrophy(RegistrySupplier<TrophyBlock> block) {
        return ITEMS.register(block.getId().getPath(),
                () -> new TrophyItem(block.get(), new Item.Properties().fireResistant())
        );
    }
}
