package trofers.platform;

import trofers.Trofers;

import java.util.ServiceLoader;

public class PlatformServices {

    public static final PlatformHelper platformHelper = load(PlatformHelper.class);

    public static <T> T load(Class<T> c) {
        final T loadedService = ServiceLoader.load(c)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + c.getName()));
        Trofers.LOGGER.debug("Loaded {} for service {}", loadedService, c);
        return loadedService;
    }
}
