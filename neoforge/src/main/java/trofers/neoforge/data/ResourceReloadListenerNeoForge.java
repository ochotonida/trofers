package trofers.neoforge.data;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import trofers.data.ResourceLoader;

import java.util.Map;

public class ResourceReloadListenerNeoForge extends SimpleJsonResourceReloadListener {

    private final ResourceLoader<?> loader;

    public ResourceReloadListenerNeoForge(ResourceLoader<?> loader) {
        super(ResourceLoader.GSON, loader.getDirectory());
        this.loader = loader;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        loader.deserializeResources(resources, ConditionalOps::createConditionalCodec);
    }
}
