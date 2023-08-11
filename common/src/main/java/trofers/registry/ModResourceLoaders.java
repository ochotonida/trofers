package trofers.registry;

import com.mojang.serialization.Codec;
import trofers.Trofers;
import trofers.data.AdvancementDrops;
import trofers.data.CodecResourceLoader;
import trofers.data.EntityDrops;
import trofers.data.ResourceLoader;
import trofers.trophy.TrophyManager;

import java.util.ArrayList;
import java.util.List;

public class ModResourceLoaders {

    private static final List<ResourceLoader<?>> loaders = new ArrayList<>();

    public static final TrophyManager TROPHIES = register(new TrophyManager());
    public static final ResourceLoader<EntityDrops> ENTITY_DROPS = registerCodecLoader("entity_drops", EntityDrops.CODEC);
    public static final ResourceLoader<AdvancementDrops> ADVANCEMENT_DROPS = registerCodecLoader("advancement_drops", AdvancementDrops.CODEC);

    private static <T extends ResourceLoader<?>> T register(T loader) {
        loaders.add(loader);
        return loader;
    }

    private static <T> ResourceLoader<T> registerCodecLoader(String id, Codec<T> codec) {
        return register(new CodecResourceLoader<>(Trofers.id(id), "trofers/%s".formatted(id), codec));
    }

    public static List<ResourceLoader<?>> getLoaders() {
        return loaders;
    }
}
