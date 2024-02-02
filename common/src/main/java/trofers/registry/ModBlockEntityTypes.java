package trofers.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import trofers.Trofers;
import trofers.block.entity.TrophyBlockEntity;

public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Trofers.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);

    public static final RegistrySupplier<BlockEntityType<TrophyBlockEntity>> TROPHY = BLOCK_ENTITY_TYPES.register("trophy",
            () -> BlockEntityType.Builder.of(
                    TrophyBlockEntity::new,
                    ModBlocks.TROPHIES.stream()
                            .map(RegistrySupplier::get)
                            .toArray(Block[]::new)
            ).build(null)
    );
}
