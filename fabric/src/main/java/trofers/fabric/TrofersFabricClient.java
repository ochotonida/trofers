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
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import trofers.TrofersClient;
import trofers.block.TrophyBlock;
import trofers.block.entity.TrophyBlockEntity;
import trofers.block.entity.TrophyBlockEntityRenderer;
import trofers.item.TrophyItemRenderer;
import trofers.registry.ModBlockEntityTypes;
import trofers.registry.ModBlocks;
import trofers.trophy.TrophySearchTreeManager;

import java.util.List;
import java.util.function.Supplier;

public class TrofersFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
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
            ColorProviderRegistry.BLOCK.register(TrofersFabricClient::getTrophyBlockColor, trophy.get());
            ColorProviderRegistry.ITEM.register(TrofersClient::getTrophyItemColor, trophy.get());
        }
    }

    @SuppressWarnings("unused")
    public static int getTrophyBlockColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int index) {
        if (level == null || pos == null) {
            return 0xFFFFFF;
        } else if (level.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity) {
            return TrofersClient.getTrophyColor(blockEntity.getTrophy(), index);
        }

        // what the hell sodium 🤨🤔🤠
        pos = pos.offset(0, 1, 0);
        if (level.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity) {
            return TrofersClient.getTrophyColor(blockEntity.getTrophy(), index);
        }
        int count = 0;
        int[] colors = new int[9];
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                BlockPos p = pos.offset(i, 0, j);
                if (level.getBlockEntity(p) instanceof TrophyBlockEntity blockEntity) {
                    colors[count] = TrofersClient.getTrophyColor(blockEntity.getTrophy(), index);
                    count += 1;
                }
            }
        }
        return blendColors(colors, count);
    }

    private static int blendColors(int[] colors, int count) {
        if (count == 0) {
            return 0xFFFFFF;
        }
        int result = 0;
        for (int i = 0; i <= 16; i += 8) {
            int c = 0;
            for (int j = 0; j < count; j++) {
                c += Math.pow((colors[j] >> i) & 0xFF, 2);
            }
            result += Math.round(Math.sqrt(c / (double) count)) << i;
        }
        return result;
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
