package trofers.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;

public class CodecResourceLoader<T> extends ResourceLoader<T> {

    // TODO merge this with ResourceLoader
    private final Codec<T> codec;

    public CodecResourceLoader(ResourceLocation id, String directory, Codec<T> codec) {
        super(id, directory);
        this.codec = codec;
    }

    @Override
    protected T deserializeResource(ResourceLocation id, JsonElement element) {
        return codec.decode(JsonOps.INSTANCE, element).getOrThrow(false, error -> {}).getFirst();
    }
}
