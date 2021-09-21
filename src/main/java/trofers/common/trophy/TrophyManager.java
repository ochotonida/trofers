package trofers.common.trophy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import trofers.Trofers;
import trofers.common.network.NetworkHandler;
import trofers.common.network.TrophySyncPacket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TrophyManager extends JsonReloadListener {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static Map<ResourceLocation, Trophy> trophies = new HashMap<>();

    public TrophyManager() {
        super(GSON, "trofers");
    }

    public static Trophy get(ResourceLocation id) {
        return trophies.getOrDefault(id, null);
    }

    public static Collection<Trophy> values() {
        return trophies.values();
    }

    public static void setTrophies(Map<ResourceLocation, Trophy> trophies) {
        TrophyManager.trophies = trophies;
        Trofers.LOGGER.info("Loaded {} trophies", trophies.size());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, IResourceManager resourceManager, IProfiler profilerFiller) {
        Map<ResourceLocation, Trophy> trophies = new HashMap<>();

        resources.forEach((id, element) -> {
            try {
                trophies.put(id, Trophy.fromJson(element, id));
            } catch (Exception exception) {
                Trofers.LOGGER.error("Couldn't parse trophy {}", id, exception);
            }
        });
        setTrophies(trophies);
    }

    public static void onDataPackReload(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            sync(event.getPlayer());
        } else {
            event.getPlayerList().getPlayers().forEach(TrophyManager::sync);
        }
    }

    private static void sync(ServerPlayerEntity player) {
        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new TrophySyncPacket(trophies));
    }
}
