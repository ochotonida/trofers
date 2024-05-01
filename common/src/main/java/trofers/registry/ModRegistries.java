package trofers.registry;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.architectury.utils.GameInstance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import trofers.Trofers;
import trofers.data.AdvancementDrops;
import trofers.data.EntityDrops;
import trofers.trophy.Trophy;

public class ModRegistries {

    public static ResourceKey<Registry<Trophy>> TROPHIES = ResourceKey.createRegistryKey(Trofers.id("trophies"));
    public static ResourceKey<Registry<EntityDrops>> ENTITY_DROPS = ResourceKey.createRegistryKey(Trofers.id("entity_drops"));
    public static ResourceKey<Registry<AdvancementDrops>> ADVANCEMENT_DROPS = ResourceKey.createRegistryKey(Trofers.id("advancement_drops"));

    private static <T> Registry<T> getRegistry(ResourceKey<Registry<T>> registryKey) {
        if (Platform.getEnvironment() == Env.CLIENT) {
            if (GameInstance.getClient().level != null) {
                return GameInstance.getClient().level.registryAccess().registry(registryKey).orElse(null);
            }
        } else if (GameInstance.getServer() != null) {
            return GameInstance.getServer().registryAccess().registry(registryKey).orElse(null);
        }
        return null;
    }

    public static Registry<Trophy> trophies() {
        return getRegistry(TROPHIES);
    }

    public static Registry<AdvancementDrops> advancementDrops() {
        return getRegistry(ADVANCEMENT_DROPS);
    }

    public static Registry<EntityDrops> entityDrops() {
        return getRegistry(ENTITY_DROPS);
    }

    public static <T> T get(ResourceKey<Registry<T>> registryKey, ResourceLocation id) {
        Registry<T> registry = getRegistry(registryKey);
        return registry == null ? null : registry.get(id);
    }
}
