package trofers.registry;

import trofers.data.ResourceLoader;
import trofers.trophy.TrophyManager;

import java.util.List;

public class ModResourceLoaders {

    public static final TrophyManager TROPHIES = new TrophyManager();

    public static List<ResourceLoader<?>> getLoaders() {
        return List.of(TROPHIES);
    }
}
