package trofers.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import trofers.Trofers;
import trofers.TrofersClient;
import trofers.forge.common.config.ModConfig;
import trofers.forge.common.init.*;
import trofers.forge.common.network.NetworkHandler;
import trofers.forge.common.trophy.TrophyManager;

@Mod(Trofers.MOD_ID)
public class TrofersForge {

    public TrofersForge() {
        EventBuses.registerModEventBus(Trofers.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        Trofers.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TrofersClient::init);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TrofersForgeClient::new);

        ModConfig.registerCommon();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModLootConditions.LOOT_CONDITION_TYPES.register(modEventBus);
        ModLootPoolEntries.LOOT_POOL_ENTRY_TYPES.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(ModItems::registerTab);

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
