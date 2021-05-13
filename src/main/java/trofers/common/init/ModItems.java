package trofers.common.init;

import trofers.Trofers;
import trofers.client.TrophyItemRenderer;
import trofers.common.TrophyBlock;
import trofers.common.TrophyItem;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class ModItems {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Trofers.MODID);

    public static final ItemGroup CREATIVE_TAB = new ItemGroup(Trofers.MODID) {
        @Override
        @OnlyIn(Dist.CLIENT)
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
                                .stacksTo(1)
                                .rarity(Rarity.RARE)
                                .tab(CREATIVE_TAB)
                                .setISTER(() -> TrophyItemRenderer::new)
                )
        );
        TROPHIES.add(trophy);
        return trophy;
    }
}
