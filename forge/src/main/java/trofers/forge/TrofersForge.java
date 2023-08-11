package trofers.forge;

import dev.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import trofers.Trofers;
import trofers.config.ModConfig;
import trofers.forge.data.ResourceReloadListenerForge;
import trofers.forge.registry.ModLootModifiers;
import trofers.registry.ModResourceLoaders;

@Mod(Trofers.MOD_ID)
public class TrofersForge {

    public TrofersForge() {
        EventBuses.registerModEventBus(Trofers.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        Trofers.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TrofersForgeClient::new);

        registerConfig();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListener);
        MinecraftForge.EVENT_BUS.addListener(this::onDataPackReload);
        MinecraftForge.EVENT_BUS.addListener(this::onServerAboutToStart);
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
        ModResourceLoaders.getLoaders().forEach(loader -> event.addListener(new ResourceReloadListenerForge(loader)));
    }

    public void onDataPackReload(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            ModResourceLoaders.TROPHIES.sync(event.getPlayer());
        } else {
            event.getPlayerList().getPlayers().forEach(ModResourceLoaders.TROPHIES::sync);
            Trofers.onDataPackLoaded(event.getPlayerList().getServer());
        }
    }

    public void onServerAboutToStart(ServerAboutToStartEvent event) {
        Trofers.onDataPackLoaded(event.getServer());
    }
}
