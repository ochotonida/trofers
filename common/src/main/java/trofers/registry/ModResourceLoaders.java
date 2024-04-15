package trofers.registry;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import trofers.Trofers;
import trofers.data.AdvancementDrops;
import trofers.data.EntityDrops;
import trofers.data.ResourceLoader;
import trofers.trophy.Trophy;
import trofers.trophy.TrophySearchTreeManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// TODO use architectury api data pack registries
@SuppressWarnings("SameParameterValue")
public class ModResourceLoaders {

    private static final Map<ResourceLocation, ResourceLoader<?>> loaders = new HashMap<>();

    @SuppressWarnings("Convert2MethodRef")
    public static final ResourceLoader<Trophy> TROPHIES = registerSynced("trophies", Trophy.CODEC, () -> TrophySearchTreeManager.createSearchTree());
    public static final ResourceLoader<EntityDrops> ENTITY_DROPS = register("entity_drops", EntityDrops.CODEC);
    public static final ResourceLoader<AdvancementDrops> ADVANCEMENT_DROPS = register("advancement_drops", AdvancementDrops.CODEC);

    private static <T> ResourceLoader<T> register(String name, Codec<T> codec) {
        ResourceLoader<T> loader = new ResourceLoader<>(Trofers.id(name), "%s/%s".formatted(Trofers.MOD_ID, name), codec);
        loaders.put(Trofers.id(name), loader);
        return loader;
    }

    private static <T> ResourceLoader<T> registerSynced(String name, Codec<T> codec, Runnable onSyncToClient) {
        ResourceLoader<T> loader = new ResourceLoader<>(Trofers.id(name), "%s/%s".formatted(Trofers.MOD_ID, name), codec, true, onSyncToClient);
        loaders.put(Trofers.id(name), loader);
        return loader;
    }

    public static Collection<ResourceLoader<?>> getLoaders() {
        return loaders.values();
    }

    public static ResourceLoader<?> get(ResourceLocation id) {
        return loaders.get(id);
    }
}
