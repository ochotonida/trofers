package trofers.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import trofers.Trofers;
import trofers.block.PillarTrophyBlock;
import trofers.block.PlateTrophyBlock;
import trofers.block.TrophyBlock;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Trofers.MOD_ID, Registries.BLOCK);

    public static final Set<RegistrySupplier<TrophyBlock>> TROPHIES = new HashSet<>();

    public static final RegistrySupplier<TrophyBlock> SMALL_PILLAR = addPillar("small_pillar", 6);
    public static final RegistrySupplier<TrophyBlock> MEDIUM_PILLAR = addPillar("medium_pillar", 7);
    public static final RegistrySupplier<TrophyBlock> LARGE_PILLAR = addPillar("large_pillar", 8);
    public static final RegistrySupplier<TrophyBlock> SMALL_PLATE = addPlate("small_plate", 6);
    public static final RegistrySupplier<TrophyBlock> MEDIUM_PLATE = addPlate("medium_plate", 7);
    public static final RegistrySupplier<TrophyBlock> LARGE_PLATE = addPlate("large_plate", 8);

    private static RegistrySupplier<TrophyBlock> addPillar(String name, int size) {
        return addTrophy(name, () -> new PillarTrophyBlock(createProperties(), size));
    }

    private static RegistrySupplier<TrophyBlock> addPlate(String name, int size) {
        return addTrophy(name, () -> new PlateTrophyBlock(createProperties(), size));
    }

    private static RegistrySupplier<TrophyBlock> addTrophy(String name, Supplier<TrophyBlock> block) {
        RegistrySupplier<TrophyBlock> trophy = BLOCKS.register(name, block);
        TROPHIES.add(trophy);
        return trophy;
    }

    private static BlockBehaviour.Properties createProperties() {
        return Block.Properties.of(Material.STONE).strength(1.5F);
    }
}
