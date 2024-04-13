package trofers.neoforge.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import trofers.Trofers;
import trofers.data.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

public class ResourceReloadListenerNeoForge extends SimpleJsonResourceReloadListener {

    private final ResourceLoader<?> loader;

    public ResourceReloadListenerNeoForge(ResourceLoader<?> loader) {
        super(ResourceLoader.GSON, loader.getDirectory());
        this.loader = loader;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<ResourceLocation, JsonElement> result = new HashMap<>();
        int amountSkipped = 0;

        for (ResourceLocation id : resources.keySet()) {
            JsonElement element = resources.get(id);
            if (matchesConditions(element)) {
                result.put(id, element);
            } else {
                amountSkipped++;
            }
        }
        loader.deserializeResources(result);

        if (amountSkipped > 0) {
            Trofers.LOGGER.info("{}: Skipping loading {} resources as their conditions were not met", loader.getId(), amountSkipped);
        }
    }

    private static boolean matchesConditions(JsonElement element) {
        if (!element.isJsonObject() || !element.getAsJsonObject().has("conditions")) {
            return true;
        }
        JsonArray conditions = GsonHelper.getAsJsonArray(element.getAsJsonObject(), "conditions");
        return CraftingHelper.processConditions(conditions, ICondition.IContext.EMPTY);
    }
}
