package trofers.common.trophy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import trofers.Trofers;

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

    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<ResourceLocation, Trophy> trophies = new HashMap<>();

        resources.forEach((id, element) -> {
            try {
                trophies.put(id, Trophy.fromJson(element, id));
            } catch (Exception exception) {
                Trofers.LOGGER.error("Couldn't parse trophy {}", id, exception);
            }
        });
        TrophyManager.trophies = trophies;
        Trofers.LOGGER.info("Loaded {} trophies", trophies.size());
    }
}