package trofers.forge;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import trofers.block.entity.TrophyBlockEntity;
import trofers.block.entity.TrophyBlockEntityRenderer;
import trofers.block.entity.TrophyScreen;
import trofers.registry.ModBlockEntityTypes;
import trofers.registry.ModBlocks;
import trofers.registry.ModItems;
import trofers.trophy.Trophy;

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
        event.registerReloadListener(new TrophyScreen.SearchTreeManager());
    }

    public void onBlockColorHandler(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, index) -> {
            if (level != null && pos != null && level.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity) {
                return getTrophyColor(blockEntity.getTrophy(), index);
            }
            return 0xFFFFFF;
        }, ModBlocks.TROPHIES.stream().map(RegistrySupplier::get).toArray(Block[]::new));
    }

    public void onItemColorHandler(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, index) -> getTrophyColor(Trophy.getTrophy(stack), index),
                ModItems.TROPHIES.stream().map(RegistrySupplier::get).toArray(Item[]::new)
        );
    }

    private int getTrophyColor(Trophy trophy, int index) {
        if (trophy != null) {
            if (index == 0) {
                return trophy.colors().base();
            } else if (index == 1) {
                return trophy.colors().accent();
            }
        }
        return 0xFFFFFF;
    }
}
