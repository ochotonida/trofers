package trofers.fabric.data;

import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import trofers.data.ResourceLoader;

import java.util.Map;

public class ResourceReloadListenerFabric extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {

    private final ResourceLoader<?> loader;

    public ResourceReloadListenerFabric(ResourceLoader<?> loader) {
        super(ResourceLoader.GSON, loader.getDirectory());
        this.loader = loader;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        loader.deserializeResources(resources);
    }

    @Override
    public ResourceLocation getFabricId() {
        return loader.getId();
    }
}
