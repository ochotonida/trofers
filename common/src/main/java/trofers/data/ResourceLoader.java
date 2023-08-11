package trofers.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import trofers.Trofers;

import java.util.HashMap;
import java.util.Map;

public abstract class ResourceLoader<T> {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    protected Map<ResourceLocation, T> resources = new HashMap<>();
    private final ResourceLocation id;
    private final String directory;

    public ResourceLoader(ResourceLocation id, String directory) {
        this.id = id;
        this.directory = directory;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getDirectory() {
        return directory;
    }

    public void deserializeResources(Map<ResourceLocation, JsonElement> resources) {
        Map<ResourceLocation, T> result = new HashMap<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement element = entry.getValue();
            try {
                result.put(id, deserializeResource(id, element));
            } catch (Exception exception) {
                Trofers.LOGGER.error("{}: Couldn't parse resource {}", getId(), id, exception);
            }
        }

        this.resources = result;

        Trofers.LOGGER.info("{}: Loaded {} resources", getId(), resources.size());
    }

    protected abstract T deserializeResource(ResourceLocation id, JsonElement element);
}
