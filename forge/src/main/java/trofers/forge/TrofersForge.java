package trofers.forge;

import dev.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import trofers.Trofers;
import trofers.config.ModConfig;
import trofers.forge.platform.ForgePlatformHelper;
import trofers.trophy.TrophyManager;

@Mod(Trofers.MOD_ID)
public class TrofersForge {

    public TrofersForge() {
        EventBuses.registerModEventBus(Trofers.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        Trofers.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TrofersForgeClient::new);

        registerConfig();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ForgePlatformHelper.LOOT_MODIFIERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListener);
        MinecraftForge.EVENT_BUS.addListener(this::onDataPackReload);
    }

    private void registerConfig() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
                )
        );
    }

    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new TrophyManager());
    }

    public void onDataPackReload(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            TrophyManager.sync(event.getPlayer());
        } else {
            event.getPlayerList().getPlayers().forEach(TrophyManager::sync);
        }
    }
}
