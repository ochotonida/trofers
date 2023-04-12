package trofers.fabric;

import net.fabricmc.api.ModInitializer;
import trofers.Trofers;

public class TrofersFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Trofers.init();
    }
}
