package trofers;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import trofers.common.trophy.block.TrophyBlockEntity;
import trofers.common.trophy.block.TrophyBlockEntityRenderer;
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
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> BlockEntityRenderers.register(ModBlockEntityTypes.TROPHY.get(), TrophyBlockEntityRenderer::new));
    }

    public void onBlockColorHandler(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, level, pos, index) -> {
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

    public void onItemColorHandler(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, index) -> {
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
