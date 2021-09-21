package trofers.common.init;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import trofers.Trofers;

import java.util.Arrays;
import java.util.List;

public class ModLootTables {

    public static final List<String> LOOT_TABLE_LOCATIONS = Arrays.asList(
            // "entities/axolotl",
            "entities/bat",
            "entities/bee",
            "entities/blaze",
            "entities/cat",
            "entities/cave_spider",
            "entities/chicken",
            "entities/cod",
            "entities/cow",
            "entities/creeper",
            "entities/dolphin",
            "entities/donkey",
            "entities/drowned",
            "entities/elder_guardian",
            "entities/enderman",
            "entities/endermite",
            "entities/evoker",
            "entities/fox",
            "entities/ghast",
            // "entities/glow_squid",
            // "entities/goat",
            "entities/guardian",
            "entities/hoglin",
            "entities/horse",
            "entities/husk",
            "entities/iron_golem",
            "entities/llama",
            "entities/magma_cube",
            "entities/mooshroom",
            "entities/mule",
            "entities/ocelot",
            "entities/panda",
            "entities/parrot",
            "entities/phantom",
            "entities/pig",
            "entities/piglin",
            "entities/piglin_brute",
            "entities/pillager",
            "entities/polar_bear",
            "entities/pufferfish",
            "entities/rabbit",
            "entities/ravager",
            "entities/salmon",
            "entities/sheep",
            "entities/shulker",
            "entities/silverfish",
            "entities/skeleton",
            "entities/skeleton_horse",
            "entities/slime",
            "entities/snow_golem",
            "entities/spider",
            "entities/squid",
            "entities/stray",
            "entities/strider",
            "entities/trader_llama",
            "entities/tropical_fish",
            "entities/turtle",
            "entities/vex",
            "entities/villager",
            "entities/vindicator",
            "entities/wandering_trader",
            "entities/witch",
            "entities/wither_skeleton",
            "entities/wolf",
            "entities/zoglin",
            "entities/zombie",
            "entities/zombie_villager",
            "entities/zombified_piglin"
    );

    public static void onLootTableLoad(LootTableLoadEvent event) {
        String prefix = "minecraft:";
        String name = event.getName().toString();

        if (name.startsWith(prefix)) {
            String location = name.substring(name.indexOf(prefix) + prefix.length());
            if (LOOT_TABLE_LOCATIONS.contains(location)) {
                Trofers.LOGGER.debug("Adding loot to " + name);
                event.getTable().addPool(getInjectPool(location));
            }
        }
    }

    public static LootPool getInjectPool(String entryName) {
        return LootPool.lootPool()
                .add(getInjectEntry(entryName))
                .name("trofers_inject")
                .build();
    }

    private static StandaloneLootEntry.Builder<?> getInjectEntry(String name) {
        ResourceLocation table = new ResourceLocation(Trofers.MODID, "inject/" + name);
        return TableLootEntry.lootTableReference(table).setWeight(1);
    }
}
