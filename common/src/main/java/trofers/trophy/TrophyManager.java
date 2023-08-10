package trofers.trophy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.network.NetworkHandler;
import trofers.network.TrophySyncPacket;
import trofers.platform.PlatformServices;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TrophyManager extends SimpleJsonResourceReloadListener {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static Map<ResourceLocation, Trophy> trophies = Map.of();

    public TrophyManager() {
        super(GSON, "trofers");
    }

    @Nullable
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

    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<ResourceLocation, Trophy> trophies = new HashMap<>();

        int amountSkipped = 0;
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement element = entry.getValue();
            try {
                if (element.isJsonObject() && element.getAsJsonObject().has("conditions") && !PlatformServices.platformHelper.matchesConditions(element.getAsJsonObject())) {
                    amountSkipped++;
                } else {
                    trophies.put(id, Trophy.fromJson(element, id));
                }

            } catch (Exception exception) {
                Trofers.LOGGER.error("Couldn't parse trophy {}", id, exception);
            }
        }

        if (amountSkipped > 0) {
            Trofers.LOGGER.info("Skipping loading {} trophies as their conditions were not met", amountSkipped);
        }

        setTrophies(trophies);
    }

    public static void sync(ServerPlayer player) {
        NetworkHandler.CHANNEL.sendToPlayer(player, new TrophySyncPacket(trophies));
    }
}
