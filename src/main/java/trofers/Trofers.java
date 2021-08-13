package trofers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import trofers.common.TrophyBlockEntity;
import trofers.common.init.ModBlockEntityTypes;
import trofers.common.init.ModBlocks;
import trofers.common.init.ModItems;

@Mod(Trofers.MODID)
public class Trofers {

    public static final String MODID = "trofers";

    // TODO gui right click on empty trophy
    // use map<resourcelocation, trophy>
    // possibly use recipe manager (available from item renderer?)

    public Trofers() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.REGISTRY.register(modEventBus);
        ModBlocks.REGISTRY.register(modEventBus);
        ModBlockEntityTypes.REGISTRY.register(modEventBus);
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(ModBlockEntityTypes::addRenderers);
        }

        @SubscribeEvent
        public static void onColorHandler(ColorHandlerEvent.Block event) {
            event.getBlockColors().register((state, level, pos, index) -> {
                if (index >= 0 && index < 3 && level != null && pos != null) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TrophyBlockEntity) {
                        return ((TrophyBlockEntity) blockEntity).getColor(index);
                    }
                }
                return 0xFFFFFF;
            }, ModBlocks.TROPHIES.stream().map(RegistryObject::get).toArray(Block[]::new));
        }

        @SubscribeEvent
        public static void onColorHandler(ColorHandlerEvent.Item event) {
            event.getItemColors().register((stack, index) -> {
                CompoundTag tag = stack.getTag();
                if (tag != null) {
                    CompoundTag colorTag = tag.getCompound("BlockEntityTag").getCompound("Colors");
                    if (index == 0 && colorTag.contains("Top")) {
                        return TrophyBlockEntity.getCombinedColor(colorTag.getCompound("Top"));
                    } else if (index == 1 && colorTag.contains("Middle")) {
                        return TrophyBlockEntity.getCombinedColor(colorTag.getCompound("Middle"));
                    } else if (index == 2 && colorTag.contains("Bottom")) {
                        return TrophyBlockEntity.getCombinedColor(colorTag.getCompound("Bottom"));
                    }
                }
                return 0xFFFFFF;
            }, ModItems.TROPHIES.stream().map(RegistryObject::get).toArray(Item[]::new));
        }
    }
}
