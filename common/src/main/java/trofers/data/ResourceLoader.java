package trofers.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.network.NetworkHandler;
import trofers.network.ResourceLoaderSyncPacket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ResourceLoader<T> {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private Map<ResourceLocation, T> resources = new HashMap<>();
    private final ResourceLocation id;
    private final String directory;
    private final Codec<T> codec;
    private final boolean syncToClients;
    private final Runnable processSyncOnClient;

    public ResourceLoader(ResourceLocation id, String directory, Codec<T> codec, boolean syncToClients, Runnable processSyncOnClient) {
        this.id = id;
        this.directory = directory;
        this.codec = codec;
        this.syncToClients = syncToClients;
        this.processSyncOnClient = processSyncOnClient;
    }

    public ResourceLoader(ResourceLocation id, String directory, Codec<T> codec) {
        this(id, directory, codec, false, () -> { });
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getDirectory() {
        return directory;
    }

    public Codec<T> getCodec() {
        return codec;
    }

    @Nullable
    public T get(ResourceLocation id) {
        return resources.getOrDefault(id, null);
    }

    public Collection<ResourceLocation> keys() {
        return resources.keySet();
    }

    public Collection<T> getAllResources() {
        return resources.values();
    }

    public void deserializeResources(Map<ResourceLocation, JsonElement> resources, Function<Codec<T>, Codec<Optional<T>>> conditionalWrapper) {
        Codec<Optional<T>> conditionalCodec = conditionalWrapper.apply(codec);
        Map<ResourceLocation, T> result = new HashMap<>();
        int amountSkipped = 0;

        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement element = entry.getValue();
            try {
                Optional<T> resource = conditionalCodec
                        .parse(JsonOps.INSTANCE, element)
                        .getOrThrow(false, error -> { });
                if (resource.isPresent()) {
                    result.put(id, resource.get());
                } else {
                    amountSkipped++;
                }
            } catch (Exception exception) {
                Trofers.LOGGER.error("{}: Couldn't parse resource {}", getId(), id, exception);
            }
        }

        this.resources = result;

        Trofers.LOGGER.info("{}: Loaded {} resources", getId(), resources.size());
        if (amountSkipped > 0) {
            Trofers.LOGGER.info("{}: Skipped loading {} resources as their conditions were not met", getId(), amountSkipped);
        }
    }

    public void syncTo(ServerPlayer player) {
        if (syncToClients) {
            NetworkHandler.CHANNEL.sendToPlayer(player, new ResourceLoaderSyncPacket(this, resources));
        }
    }

    public void updateResources(Map<ResourceLocation, T> resources) {
        this.resources = resources;
        processSyncOnClient.run();
    }
}
