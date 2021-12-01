package trofers.common.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import trofers.Trofers;
import trofers.common.block.PillarTrophyBlock;
import trofers.common.block.PlateTrophyBlock;
import trofers.common.block.TrophyBlock;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Trofers.MODID);

    public static final Set<RegistryObject<TrophyBlock>> TROPHIES = new HashSet<>();

    public static final RegistryObject<TrophyBlock> SMALL_PILLAR = addPillar("small_pillar", 6);
    public static final RegistryObject<TrophyBlock> MEDIUM_PILLAR = addPillar("medium_pillar", 7);
    public static final RegistryObject<TrophyBlock> LARGE_PILLAR = addPillar("large_pillar", 8);
    public static final RegistryObject<TrophyBlock> SMALL_PLATE = addPlate("small_plate", 6);
    public static final RegistryObject<TrophyBlock> MEDIUM_PLATE = addPlate("medium_plate", 7);
    public static final RegistryObject<TrophyBlock> LARGE_PLATE = addPlate("large_plate", 8);

    private static RegistryObject<TrophyBlock> addPillar(String name, int size) {
        return addTrophy(name, () -> new PillarTrophyBlock(createProperties(), size));
    }

    private static RegistryObject<TrophyBlock> addPlate(String name, int size) {
        return addTrophy(name, () -> new PlateTrophyBlock(createProperties(), size));
    }

    private static RegistryObject<TrophyBlock> addTrophy(String name, Supplier<TrophyBlock> block) {
        RegistryObject<TrophyBlock> trophy = REGISTRY.register(name, block);
        TROPHIES.add(trophy);
        return trophy;
    }

    private static BlockBehaviour.Properties createProperties() {
        return Block.Properties.of(Material.STONE).strength(1.5F);
    }
}
