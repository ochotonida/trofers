package trofers.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Trofers.MODID);

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(Trofers.MODID) {
        @Override
        public ItemStack makeIcon() {
            ItemStack result = new ItemStack(MEDIUM_PILLAR.get());
            result.getOrCreateTagElement("BlockEntityTag")
                    .putString("Trophy", new ResourceLocation(Trofers.MODID, "panda").toString());
            return result;
        }
    };

    public static final Set<RegistryObject<TrophyItem>> TROPHIES = new HashSet<>();

    public static final RegistryObject<TrophyItem> SMALL_PILLAR = addTrophy(ModBlocks.SMALL_PILLAR);
    public static final RegistryObject<TrophyItem> MEDIUM_PILLAR = addTrophy(ModBlocks.MEDIUM_PILLAR);
    public static final RegistryObject<TrophyItem> LARGE_PILLAR = addTrophy(ModBlocks.LARGE_PILLAR);
    public static final RegistryObject<TrophyItem> SMALL_PLATE = addTrophy(ModBlocks.SMALL_PLATE);
    public static final RegistryObject<TrophyItem> MEDIUM_PLATE = addTrophy(ModBlocks.MEDIUM_PLATE);
    public static final RegistryObject<TrophyItem> LARGE_PLATE = addTrophy(ModBlocks.LARGE_PLATE);

    private static RegistryObject<TrophyItem> addTrophy(RegistryObject<TrophyBlock> block) {
        RegistryObject<TrophyItem> trophy = REGISTRY.register(block.getId().getPath(), () ->
                new TrophyItem(
                        block.get(),
                        new Item.Properties()
                                .fireResistant()
                                .tab(CREATIVE_TAB)
                )
        );
        TROPHIES.add(trophy);
        return trophy;
    }
}
