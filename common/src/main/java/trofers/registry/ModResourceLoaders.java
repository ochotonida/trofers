package trofers.registry;

import trofers.Trofers;
import trofers.data.CodecResourceLoader;
import trofers.data.EntityDrops;
import trofers.data.ResourceLoader;
import trofers.trophy.TrophyManager;

import java.util.List;

public class ModResourceLoaders {

    public static final TrophyManager TROPHIES = new TrophyManager();
    public static final ResourceLoader<EntityDrops> ENTITY_DROPS = new CodecResourceLoader<>(Trofers.id("entity_drops"), "trofers/entity_drops", EntityDrops.CODEC);

    public static List<ResourceLoader<?>> getLoaders() {
        return List.of(
                TROPHIES,
                ENTITY_DROPS
        );
    }
}
