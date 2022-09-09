package trofers.common.trophy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.network.PacketDistributor;
import trofers.Trofers;
import trofers.common.network.NetworkHandler;
import trofers.common.network.TrophySyncPacket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TrophyManager extends SimpleJsonResourceReloadListener {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static Map<ResourceLocation, Trophy> trophies = Map.of();

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

    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<ResourceLocation, Trophy> trophies = new HashMap<>();

        int amountSkipped = 0;
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement element = entry.getValue();
            try {
                if (element.isJsonObject() && element.getAsJsonObject().has("conditions") && !CraftingHelper.processConditions(GsonHelper.getAsJsonArray(element.getAsJsonObject(), "conditions"), ICondition.IContext.EMPTY)) {
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

    public static void onDataPackReload(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            sync(event.getPlayer());
        } else {
            event.getPlayerList().getPlayers().forEach(TrophyManager::sync);
        }
    }

    private static void sync(ServerPlayer player) {
        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new TrophySyncPacket(trophies));
    }
}
