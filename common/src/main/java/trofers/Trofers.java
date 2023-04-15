package trofers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trofers.network.NetworkHandler;
import trofers.registry.ModBlockEntityTypes;
import trofers.registry.ModBlocks;
import trofers.registry.ModItems;
import trofers.registry.ModLootConditions;

public class Trofers {

    public static final String MOD_ID = "trofers";

    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        NetworkHandler.register();

        ModBlocks.BLOCKS.register();
        ModItems.ITEMS.register();
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register();
        ModLootConditions.LOOT_CONDITION_TYPES.register();
    }
}
