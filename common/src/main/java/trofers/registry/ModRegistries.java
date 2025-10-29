package trofers.registry;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.architectury.utils.GameInstance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.data.AdvancementDrops;
import trofers.data.EntityDrops;
import trofers.trophy.Trophy;

import java.util.Optional;

public class ModRegistries {

    public static ResourceKey<Registry<Trophy>> TROPHIES = ResourceKey.createRegistryKey(Trofers.id("trophies"));
    public static ResourceKey<Registry<EntityDrops>> ENTITY_DROPS = ResourceKey.createRegistryKey(Trofers.id("entity_drops"));
    public static ResourceKey<Registry<AdvancementDrops>> ADVANCEMENT_DROPS = ResourceKey.createRegistryKey(Trofers.id("advancement_drops"));

    @Nullable
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

    public static Optional<Registry<Trophy>> trophies() {
        return Optional.ofNullable(getRegistry(TROPHIES));
    }

    public static Optional<Registry<AdvancementDrops>> advancementDrops() {
        return Optional.ofNullable(getRegistry(ADVANCEMENT_DROPS));
    }

    public static Optional<Registry<EntityDrops>> entityDrops() {
        return Optional.ofNullable(getRegistry(ENTITY_DROPS));
    }

    public static <T> T get(ResourceKey<Registry<T>> registryKey, ResourceLocation id) {
        Registry<T> registry = getRegistry(registryKey);
        return registry == null ? null : registry.get(id);
    }
}
