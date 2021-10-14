package trofers;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
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
        ModItems.REGISTRY.register(modEventBus);
        ModBlocks.REGISTRY.register(modEventBus);
        ModBlockEntityTypes.REGISTRY.register(modEventBus);
        ModLootModifiers.REGISTRY.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addGenericListener(GlobalLootModifierSerializer.class, ModLootConditions::register);
        modEventBus.addGenericListener(GlobalLootModifierSerializer.class, ModLootPoolEntries::register);

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
