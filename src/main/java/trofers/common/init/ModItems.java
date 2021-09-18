package trofers.common.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.RegistryObject;
import trofers.Trofers;
import trofers.common.trophy.block.TrophyBlock;
import trofers.common.trophy.item.TrophyItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class ModItems {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Trofers.MODID);

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(Trofers.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(TROPHY.get());
        }
    };

    public static final Set<RegistryObject<TrophyItem>> TROPHIES = new HashSet<>();

    public static final RegistryObject<TrophyItem> SMALL_TROPHY = addTrophy(ModBlocks.SMALL_TROPHY);
    public static final RegistryObject<TrophyItem> TROPHY = addTrophy(ModBlocks.TROPHY);
    public static final RegistryObject<TrophyItem> LARGE_TROPHY = addTrophy(ModBlocks.LARGE_TROPHY);

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
