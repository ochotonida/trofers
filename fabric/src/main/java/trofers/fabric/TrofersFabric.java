package trofers.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import trofers.Trofers;
import trofers.trophy.TrophyManager;

public class TrofersFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Trofers.init();

        registerTrophyManager();

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> TrophyManager.sync(player));
    }

    @SuppressWarnings("ConstantConditions")
    public void registerTrophyManager() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(
                (IdentifiableResourceReloadListener) new TrophyManager()
        );
    }
}
