package trofers.neoforge.registry;

import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import trofers.data.AdvancementDrops;
import trofers.data.EntityDrops;
import trofers.registry.ModRegistries;
import trofers.trophy.Trophy;
public class ModRegistriesNeoForge {

    public static void createDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ModRegistries.TROPHIES, Trophy.CODEC, Trophy.CODEC);
        event.dataPackRegistry(ModRegistries.ENTITY_DROPS, EntityDrops.CODEC, EntityDrops.CODEC);
        event.dataPackRegistry(ModRegistries.ADVANCEMENT_DROPS, AdvancementDrops.CODEC, AdvancementDrops.CODEC);
    }
}
