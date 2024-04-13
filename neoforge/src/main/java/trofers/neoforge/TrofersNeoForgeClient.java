package trofers.neoforge;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import trofers.TrofersClient;
import trofers.block.TrophyBlock;
import trofers.block.entity.TrophyBlockEntityRenderer;
import trofers.registry.ModBlockEntityTypes;
import trofers.registry.ModBlocks;
import trofers.trophy.TrophySearchTreeManager;

import java.util.function.Supplier;

public class TrofersNeoForgeClient {

    public TrofersNeoForgeClient(IEventBus modBus) {
        modBus.addListener(this::onClientSetup);
        modBus.addListener(this::onBlockColorHandler);
        modBus.addListener(this::onItemColorHandler);
        modBus.addListener(this::onRegisterClientReloadListeners);
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
