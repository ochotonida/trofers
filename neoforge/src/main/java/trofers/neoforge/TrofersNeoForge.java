package trofers.neoforge;

import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import trofers.Trofers;
import trofers.config.ModConfig;
import trofers.neoforge.data.ResourceReloadListenerNeoForge;
import trofers.neoforge.registry.ModLootModifiers;
import trofers.registry.ModResourceLoaders;

@Mod(Trofers.MOD_ID)
public class TrofersNeoForge {

    public TrofersNeoForge(IEventBus modBus) {
        Trofers.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            new TrofersNeoForgeClient(modBus);
        }

        registerConfig();

        ModLootModifiers.LOOT_MODIFIERS.register(modBus);

        NeoForge.EVENT_BUS.addListener(this::onAddReloadListener);
        NeoForge.EVENT_BUS.addListener(this::onDataPackReload);
        NeoForge.EVENT_BUS.addListener(this::onServerAboutToStart);
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
        ModResourceLoaders.getLoaders().forEach(loader -> event.addListener(new ResourceReloadListenerNeoForge<>(loader)));
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
