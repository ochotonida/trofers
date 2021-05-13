package trofers.common.init;

import trofers.Trofers;
import trofers.client.TrophyBlockEntityRenderer;
import trofers.common.TrophyBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("ConstantConditions")
public class ModBlockEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Trofers.MODID);

    public static final RegistryObject<TileEntityType<TrophyBlockEntity>> TROPHY = REGISTRY.register(ModBlocks.TROPHY.getId().getPath(), () -> TileEntityType.Builder.of(TrophyBlockEntity::new, ModBlocks.TROPHIES.stream().map(RegistryObject::get).toArray(Block[]::new)).build(null));

    public static void addRenderers() {
        ClientRegistry.bindTileEntityRenderer(TROPHY.get(), TrophyBlockEntityRenderer::new);
    }
}
