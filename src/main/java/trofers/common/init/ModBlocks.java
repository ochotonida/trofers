package trofers.common.init;

import trofers.Trofers;
import trofers.common.TrophyBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class ModBlocks {

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Trofers.MODID);

    public static final Set<RegistryObject<TrophyBlock>> TROPHIES = new HashSet<>();

    public static final RegistryObject<TrophyBlock> SMALL_TROPHY = addTrophy("small_trophy", 6);
    public static final RegistryObject<TrophyBlock> TROPHY = addTrophy("trophy", 7);
    public static final RegistryObject<TrophyBlock> LARGE_TROPHY = addTrophy("large_trophy", 8);

    private static RegistryObject<TrophyBlock> addTrophy(String name, int height) {
        RegistryObject<TrophyBlock> trophy = REGISTRY.register(name, () ->
                new TrophyBlock(
                        Block.Properties.of(Material.STONE).strength(1.5F),
                        height
                )
        );
        TROPHIES.add(trophy);
        return trophy;
    }
}
