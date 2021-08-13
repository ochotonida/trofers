package trofers.common.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import trofers.Trofers;
import trofers.common.TrophyBlockEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("ConstantConditions")
public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Trofers.MODID);

    public static final RegistryObject<BlockEntityType<TrophyBlockEntity>> TROPHY = REGISTRY.register(ModBlocks.TROPHY.getId().getPath(),
            () -> BlockEntityType.Builder.of(
                    TrophyBlockEntity::new,
                    ModBlocks.TROPHIES.stream()
                            .map(RegistryObject::get)
                            .toArray(Block[]::new)
            ).build(null)
    );

    public static void addRenderers() {
        // TODO ClientRegistry.bindTileEntityRenderer(TROPHY.get(), TrophyBlockEntityRenderer::new);
    }
}
