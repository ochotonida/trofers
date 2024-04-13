package trofers.neoforge.data;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import trofers.Trofers;
import trofers.data.ResourceLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResourceReloadListenerNeoForge<T> extends SimpleJsonResourceReloadListener {

    private final ResourceLoader<T> loader;

    public ResourceReloadListenerNeoForge(ResourceLoader<T> loader) {
        super(ResourceLoader.GSON, loader.getDirectory());
        this.loader = loader;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<ResourceLocation, JsonElement> result = new HashMap<>();
        int amountSkipped = 0;

        // TODO use neoforge:conditions
        // TODO fix this shit
        Codec<Optional<Boolean>> codec = ConditionalOps.createConditionalCodec(Codec.unit(false));

        for (ResourceLocation id : resources.keySet()) {
            JsonElement element = resources.get(id);
            codec.decode(JsonOps.INSTANCE, element).resultOrPartial(err -> {
            }).flatMap(Pair::getFirst).ifPresent(q -> result.put(id, element));
        }
        loader.deserializeResources(result);

        if (amountSkipped > 0) {
            Trofers.LOGGER.info("{}: Skipping loading {} resources as their conditions were not met", loader.getId(), amountSkipped);
        }
    }
}
