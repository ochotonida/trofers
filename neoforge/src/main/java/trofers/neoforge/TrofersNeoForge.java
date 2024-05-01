package trofers.neoforge;

import dev.architectury.networking.NetworkManager;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import trofers.Trofers;
import trofers.config.ModConfig;
import trofers.neoforge.registry.ModLootModifiers;
import trofers.neoforge.registry.ModRegistriesNeoForge;
import trofers.network.DataPackLoadedPacket;

@Mod(Trofers.MOD_ID)
public class TrofersNeoForge {

    public TrofersNeoForge(IEventBus modBus) {
        Trofers.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            new TrofersNeoForgeClient(modBus);
        }

        registerConfig();

        ModLootModifiers.LOOT_MODIFIERS.register(modBus);
        modBus.addListener(TrofersData::gatherData);
        modBus.addListener(ModRegistriesNeoForge::createDataPackRegistries);

        NeoForge.EVENT_BUS.addListener(this::onDataPackReload);
    }

    private void registerConfig() {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
        );
    }

    public void onDataPackReload(OnDatapackSyncEvent event) {
        event.getRelevantPlayers().forEach(player -> NetworkManager.sendToPlayer(player, new DataPackLoadedPacket()));
    }
}
