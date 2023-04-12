package trofers.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import trofers.Trofers;
import trofers.common.block.TrophyBlock;
import trofers.common.item.TrophyItem;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Trofers.MODID);

    public static CreativeModeTab CREATIVE_TAB;

    public static void registerTab(CreativeModeTabEvent.Register event) {
        CREATIVE_TAB = event.registerCreativeModeTab(new ResourceLocation(Trofers.MODID, "main"), builder -> builder
                .icon(ModItems::makeIcon)
                .title(Component.translatable("%s.creative_tab".formatted(Trofers.MODID)))
                .displayItems((parameters, output) -> ForgeRegistries.ITEMS.forEach(item -> {
                    ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
                    if (key != null && key.getNamespace().equals(Trofers.MODID)) {
                        output.accept(item);
                    }
                }))
        );
    }

    private static ItemStack makeIcon() {
        ItemStack result = new ItemStack(MEDIUM_PILLAR.get());
        result.getOrCreateTagElement("BlockEntityTag")
                .putString("Trophy", new ResourceLocation(Trofers.MODID, "panda").toString());
        return result;
    }

    public static final Set<RegistryObject<TrophyItem>> TROPHIES = new HashSet<>();

    public static final RegistryObject<TrophyItem> SMALL_PILLAR = addTrophy(ModBlocks.SMALL_PILLAR);
    public static final RegistryObject<TrophyItem> MEDIUM_PILLAR = addTrophy(ModBlocks.MEDIUM_PILLAR);
    public static final RegistryObject<TrophyItem> LARGE_PILLAR = addTrophy(ModBlocks.LARGE_PILLAR);
    public static final RegistryObject<TrophyItem> SMALL_PLATE = addTrophy(ModBlocks.SMALL_PLATE);
    public static final RegistryObject<TrophyItem> MEDIUM_PLATE = addTrophy(ModBlocks.MEDIUM_PLATE);
    public static final RegistryObject<TrophyItem> LARGE_PLATE = addTrophy(ModBlocks.LARGE_PLATE);

    private static RegistryObject<TrophyItem> addTrophy(RegistryObject<TrophyBlock> block) {
        RegistryObject<TrophyItem> trophy = ITEMS.register(block.getId().getPath(), () ->
                new TrophyItem(
                        block.get(),
                        new Item.Properties()
                                .fireResistant()
                )
        );
        TROPHIES.add(trophy);
        return trophy;
    }
}
