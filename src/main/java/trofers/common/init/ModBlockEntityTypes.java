package trofers.common.init;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import trofers.Trofers;
import trofers.common.trophy.block.TrophyBlockEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("ConstantConditions")
public class ModBlockEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Trofers.MODID);

    public static final RegistryObject<TileEntityType<TrophyBlockEntity>> TROPHY = REGISTRY.register("trophy",
            () -> TileEntityType.Builder.of(
                    TrophyBlockEntity::new,
                    ModBlocks.TROPHIES.stream()
                            .map(RegistryObject::get)
                            .toArray(Block[]::new)
            ).build(null)
    );
}
