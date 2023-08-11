package trofers.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import trofers.Trofers;
import trofers.fabric.data.ResourceReloadListenerFabric;
import trofers.registry.ModResourceLoaders;

public class TrofersFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Trofers.init();

        registerTrophyManager();

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> ModResourceLoaders.TROPHIES.sync(player));
    }

    public void registerTrophyManager() {
        ModResourceLoaders.getLoaders().forEach(loader -> ResourceManagerHelper.get(PackType.SERVER_DATA)
                .registerReloadListener(new ResourceReloadListenerFabric(loader)));
    }
}
