package trofers;

import dev.architectury.event.events.common.PlayerEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import trofers.config.ModConfig;
import trofers.data.AdvancementDrops;
import trofers.data.EntityDrops;
import trofers.network.NetworkHandler;
import trofers.registry.ModBlockEntityTypes;
import trofers.registry.ModBlocks;
import trofers.registry.ModLootConditions;

public class Trofers {

    public static final String MOD_ID = "trofers";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ModConfig CONFIG;

    public static ResourceLocation id(String path) {
        return new ResourceLocation(Trofers.MOD_ID, path);
    }

    public static void init() {
        AutoConfig.register(ModConfig.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        NetworkHandler.register();

        ModBlocks.CREATIVE_MODE_TABS.register();
        ModBlocks.BLOCKS.register();
        ModBlocks.ITEMS.register();
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register();
        ModLootConditions.LOOT_CONDITION_TYPES.register();

        PlayerEvent.PLAYER_ADVANCEMENT.register(AdvancementDrops::onAdvancementAwarded);
    }

    public static void onDataPackLoaded(MinecraftServer server) {
        AdvancementDrops.onDataPackLoaded(server);
        EntityDrops.onDataPackLoaded();
    }
}
