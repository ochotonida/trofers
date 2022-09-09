package trofers;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trofers.common.config.ModConfig;
import trofers.common.init.*;
import trofers.common.network.NetworkHandler;
import trofers.common.trophy.TrophyManager;

@Mod(Trofers.MODID)
public class Trofers {

    public static final String MODID = "trofers";

    public static final Logger LOGGER = LogManager.getLogger("Trofers");

    public Trofers() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TrofersClient::new);

        ModConfig.registerCommon();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModLootConditions.LOOT_CONDITION_TYPES.register(modEventBus);
        ModLootPoolEntries.LOOT_POOL_ENTRY_TYPES.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListener);
        MinecraftForge.EVENT_BUS.addListener(TrophyManager::onDataPackReload);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkHandler::register);
    }

    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new TrophyManager());
    }
}
