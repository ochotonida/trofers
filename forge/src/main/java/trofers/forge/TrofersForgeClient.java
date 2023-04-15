package trofers.forge;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import trofers.TrofersClient;
import trofers.block.TrophyBlock;
import trofers.block.entity.TrophyBlockEntityRenderer;
import trofers.registry.ModBlockEntityTypes;
import trofers.registry.ModBlocks;
import trofers.trophy.TrophySearchTreeManager;

import java.util.function.Supplier;

public class TrofersForgeClient {

    public TrofersForgeClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onBlockColorHandler);
        modEventBus.addListener(this::onItemColorHandler);
        modEventBus.addListener(this::onRegisterClientReloadListeners);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> BlockEntityRenderers.register(ModBlockEntityTypes.TROPHY.get(), TrophyBlockEntityRenderer::new));
    }

    public void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new TrophySearchTreeManager());
    }

    public void onBlockColorHandler(RegisterColorHandlersEvent.Block event) {
        for (Supplier<TrophyBlock> trophy : ModBlocks.TROPHIES) {
            event.register(TrofersClient::getTrophyBlockColor, trophy.get());
        }
    }

    public void onItemColorHandler(RegisterColorHandlersEvent.Item event) {
        for (Supplier<TrophyBlock> trophy : ModBlocks.TROPHIES) {
            event.register(TrofersClient::getTrophyItemColor, trophy.get());
        }
    }
}
