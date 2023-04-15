package trofers;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trofers.network.NetworkHandler;
import trofers.registry.ModBlockEntityTypes;
import trofers.registry.ModBlocks;
import trofers.registry.ModLootConditions;

public class Trofers {

    public static final String MOD_ID = "trofers";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation id(String path) {
        return new ResourceLocation(Trofers.MOD_ID, path);
    }

    public static void init() {
        NetworkHandler.register();

        ModBlocks.BLOCKS.register();
        ModBlocks.ITEMS.register();
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register();
        ModLootConditions.LOOT_CONDITION_TYPES.register();
    }
}
