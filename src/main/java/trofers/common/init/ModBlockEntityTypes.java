package trofers.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import trofers.Trofers;
import trofers.common.block.entity.TrophyBlockEntity;

@SuppressWarnings("ConstantConditions")
public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Trofers.MODID);

    public static final RegistryObject<BlockEntityType<TrophyBlockEntity>> TROPHY = BLOCK_ENTITY_TYPES.register("trophy",
            () -> BlockEntityType.Builder.of(
                    TrophyBlockEntity::new,
                    ModBlocks.TROPHIES.stream()
                            .map(RegistryObject::get)
                            .toArray(Block[]::new)
            ).build(null)
    );
}
