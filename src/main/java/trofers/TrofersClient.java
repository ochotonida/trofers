package trofers;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import trofers.common.block.entity.TrophyBlockEntity;
import trofers.common.block.entity.TrophyBlockEntityRenderer;
import trofers.common.block.entity.TrophyScreen;
import trofers.common.init.ModBlockEntityTypes;
import trofers.common.init.ModBlocks;
import trofers.common.init.ModItems;
import trofers.common.trophy.Trophy;

public class TrofersClient {

    public TrofersClient() {
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
            if (index >= 0 && index < 3 && level != null && pos != null) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof TrophyBlockEntity trophyBlockEntity) {
                    Trophy trophy = trophyBlockEntity.getTrophy();
                    if (trophy == null) {
                        return 0xFFFFFF;
                    }

                    if (index == 0) {
                        return trophy.colors().base();
                    } else if (index == 1) {
                        return trophy.colors().accent();
                    }
                }
            }
            return 0xFFFFFF;
        }, ModBlocks.TROPHIES.stream().map(RegistryObject::get).toArray(Block[]::new));
    }

    public void onItemColorHandler(RegisterColorHandlersEvent.Item event) {
        event.register((stack, index) -> {
            Trophy trophy = Trophy.getTrophy(stack);
            if (trophy != null) {
                if (index == 0) {
                    return trophy.colors().base();
                } else if (index == 1) {
                    return trophy.colors().accent();
                }
            }
            return 0xFFFFFF;
        }, ModItems.TROPHIES.stream().map(RegistryObject::get).toArray(Item[]::new));
    }
}
