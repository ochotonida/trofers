package trofers.fabric;

import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import trofers.TrofersClient;
import trofers.block.TrophyBlock;
import trofers.block.entity.TrophyBlockEntityRenderer;
import trofers.block.entity.TrophySearchTreeManager;
import trofers.item.TrophyItemRenderer;
import trofers.registry.ModBlockEntityTypes;
import trofers.registry.ModBlocks;

import java.util.List;
import java.util.function.Supplier;

public class TrofersFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TrofersClient.init();

        BlockEntityRenderers.register(ModBlockEntityTypes.TROPHY.get(), TrophyBlockEntityRenderer::new);

        registerTrophyItemRenderers();
        registerColorHandlers();
        registerTrophySearchTreeManager();
        registerRenderTypes();
    }

    private static void registerTrophyItemRenderers() {
        for (RegistrySupplier<TrophyBlock> trophy : ModBlocks.TROPHIES) {
            BuiltinItemRendererRegistry.INSTANCE.register(trophy.get(), TrophyItemRenderer::render);
        }
    }

    private static void registerColorHandlers() {
        for (Supplier<TrophyBlock> trophy : ModBlocks.TROPHIES) {
            ColorProviderRegistry.BLOCK.register(TrofersClient::getTrophyBlockColor, trophy.get());
            ColorProviderRegistry.ITEM.register(TrofersClient::getTrophyItemColor, trophy.get());
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static void registerTrophySearchTreeManager() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
                (IdentifiableResourceReloadListener) new TrophySearchTreeManager()
        );
    }

    private static void registerRenderTypes() {
        for (Block block : List.of(
            ModBlocks.SMALL_PLATE.get(),
            ModBlocks.MEDIUM_PLATE.get(),
            ModBlocks.LARGE_PLATE.get()
        )) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        }
    }
}
