package trofers.network;

import com.mojang.serialization.Codec;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import trofers.data.ResourceLoader;
import trofers.registry.ModResourceLoaders;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ResourceLoaderSyncPacket {

    private final ResourceLoader<?> loader;
    private final Map<ResourceLocation, Object> resources;

    public ResourceLoaderSyncPacket(FriendlyByteBuf buffer) {
        resources = new HashMap<>();
        loader = ModResourceLoaders.get(buffer.readResourceLocation());
        while (buffer.readBoolean()) {
            ResourceLocation id = buffer.readResourceLocation();
            resources.put(id, buffer.readJsonWithCodec(loader.getCodec()));
        }
    }

    public <T> ResourceLoaderSyncPacket(ResourceLoader<T> loader, Map<ResourceLocation, T> resources) {
        this.loader = loader;
        this.resources = new HashMap<>(resources);
    }

    void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(loader.getId());
        resources.forEach((id, resource) -> {
            buffer.writeBoolean(true);
            buffer.writeResourceLocation(id);
            writeResource(buffer, loader.getCodec(), resource);
        });
        buffer.writeBoolean(false);
    }

    @SuppressWarnings("unchecked")
    private static <T> void writeResource(FriendlyByteBuf buffer, Codec<T> codec, Object resource) {
        buffer.writeJsonWithCodec(codec, (T) resource);
    }

    void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> setResources(loader, resources));
    }

    @SuppressWarnings("unchecked")
    private static <T> void setResources(ResourceLoader<T> loader, Map<ResourceLocation, Object> resources) {
        Map<ResourceLocation, T> map = new HashMap<>();
        resources.forEach((id, resource) -> map.put(id, (T) resource));
        loader.updateResources(map);
    }
}
