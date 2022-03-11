package trofers.data.loottables;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import net.minecraft.world.item.Items;

public class AlexsMobsLootTables extends LootTableProvider {

    @Override
    public String getModId() {
        return AlexsMobs.MODID;
    }

    @Override
    protected void addLootTables() {
        add(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE, AMItemRegistry.SPIKED_SCUTE);
        add(AMEntityRegistry.ANACONDA, AMItemRegistry.SHED_SNAKE_SKIN);
        add(AMEntityRegistry.ANTEATER, AMItemRegistry.LEAFCUTTER_ANT_PUPA);
        add(AMEntityRegistry.BALD_EAGLE, 1, 3, Items.SALMON);
        add(AMEntityRegistry.BISON, AMItemRegistry.BISON_FUR);
        add(AMEntityRegistry.BLOBFISH, AMItemRegistry.FISH_OIL);
        add(AMEntityRegistry.BONE_SERPENT, AMItemRegistry.BONE_SERPENT_TOOTH);
        add(AMEntityRegistry.BUNFUNGUS, 2, 6, Items.MYCELIUM);
        add(AMEntityRegistry.CACHALOT_WHALE, AMItemRegistry.AMBERGRIS);
        add(AMEntityRegistry.CAPUCHIN_MONKEY, AMItemRegistry.BANANA);
        add(AMEntityRegistry.CENTIPEDE_HEAD, AMItemRegistry.CENTIPEDE_LEG);
        add(AMEntityRegistry.COCKROACH, AMItemRegistry.MARACA);
        add(AMEntityRegistry.COMB_JELLY, AMItemRegistry.RAINBOW_JELLY);
        add(AMEntityRegistry.COSMAW, AMItemRegistry.COSMIC_COD);
        add(AMEntityRegistry.COSMIC_COD, 1, 3, AMItemRegistry.COSMIC_COD);
        add(AMEntityRegistry.CRIMSON_MOSQUITO, 1, 3, AMItemRegistry.BLOOD_SAC);
        add(AMEntityRegistry.CROCODILE, AMItemRegistry.CROCODILE_SCUTE);
        add(AMEntityRegistry.CROW, 1, 3, Items.PUMPKIN_SEEDS);
        add(AMEntityRegistry.DROPBEAR, AMItemRegistry.DROPBEAR_CLAW);
        add(AMEntityRegistry.ELEPHANT, 1, 3, AMItemRegistry.ACACIA_BLOSSOM);
        add(AMEntityRegistry.EMU, AMItemRegistry.EMU_EGG);
        add(AMEntityRegistry.ENDERGRADE, 1, 3, Items.CHORUS_FRUIT);
        add(AMEntityRegistry.ENDERIOPHAGE, 2, 6, AMItemRegistry.ENDERIOPHAGE_ROCKET);
        add(AMEntityRegistry.FLUTTER, 1, 3, Items.SPORE_BLOSSOM);
        add(AMEntityRegistry.FLY, AMItemRegistry.MAGGOT);
        add(AMEntityRegistry.FRILLED_SHARK, AMItemRegistry.SERRATED_SHARK_TOOTH);
        add(AMEntityRegistry.FROSTSTALKER, AMItemRegistry.FROSTSTALKER_HORN);
        add(AMEntityRegistry.GAZELLE, AMItemRegistry.GAZELLE_HORN);
        add(AMEntityRegistry.GELADA_MONKEY, 1, 3, Items.GRASS);
        add(AMEntityRegistry.GIANT_SQUID, AMItemRegistry.LOST_TENTACLE);
        add(AMEntityRegistry.GORILLA, 1, 3, AMItemRegistry.BANANA);
        add(AMEntityRegistry.GRIZZLY_BEAR, AMItemRegistry.BEAR_FUR);
        add(AMEntityRegistry.GUSTER, AMItemRegistry.GUSTER_EYE);
        add(AMEntityRegistry.HAMMERHEAD_SHARK, 1, 3, AMItemRegistry.SHARK_TOOTH);
        add(AMEntityRegistry.HUMMINGBIRD, 1, 3, Items.FEATHER);
        add(AMEntityRegistry.KANGAROO, AMItemRegistry.KANGAROO_BURGER);
        add(AMEntityRegistry.KOMODO_DRAGON, 1, 3, AMItemRegistry.KOMODO_SPIT);
        add(AMEntityRegistry.LAVIATHAN, 2, 6, Items.MAGMA_BLOCK);
        add(AMEntityRegistry.LEAFCUTTER_ANT, AMItemRegistry.GONGYLIDIA);
        add(AMEntityRegistry.LOBSTER, AMItemRegistry.LOBSTER_TAIL);
        add(AMEntityRegistry.MANED_WOLF, 1, 3, Items.APPLE);
        add(AMEntityRegistry.MANTIS_SHRIMP, 1, 3, Items.COD, Items.SALMON);
        add(AMEntityRegistry.MIMIC_OCTOPUS, Items.TROPICAL_FISH);
        add(AMEntityRegistry.MIMICUBE, AMItemRegistry.MIMICREAM);
        add(AMEntityRegistry.MOOSE, AMItemRegistry.MOOSE_ANTLER);
        add(AMEntityRegistry.MUNGUS, AMItemRegistry.MUNGAL_SPORES);
        add(AMEntityRegistry.PLATYPUS, 1, 3, Items.REDSTONE_BLOCK);
        add(AMEntityRegistry.RACCOON, AMItemRegistry.RACCOON_TAIL);
        add(AMEntityRegistry.RATTLESNAKE, AMItemRegistry.RATTLESNAKE_RATTLE);
        add(AMEntityRegistry.ROADRUNNER, AMItemRegistry.ROADRUNNER_FEATHER);
        add(AMEntityRegistry.ROCKY_ROLLER, AMItemRegistry.ROCKY_SHELL);
        add(AMEntityRegistry.SEAGULL, 2, 6, Items.SAND);
        add(AMEntityRegistry.SEAL, 1, 3, Items.COD, Items.SALMON);
        add(AMEntityRegistry.SHOEBILL, 1, 3, Items.FEATHER);
        add(AMEntityRegistry.SNOW_LEOPARD, 1, 3, AMItemRegistry.MOOSE_RIBS);
        add(AMEntityRegistry.SOUL_VULTURE, AMItemRegistry.SOUL_HEART);
        add(AMEntityRegistry.SPECTRE, Items.PHANTOM_MEMBRANE);
        add(AMEntityRegistry.STRADDLER, AMItemRegistry.STRADDLITE);
        add(AMEntityRegistry.STRADPOLE, 2, 6, Items.BASALT);
        add(AMEntityRegistry.TARANTULA_HAWK, AMItemRegistry.TARANTULA_HAWK_WING_FRAGMENT);
        add(AMEntityRegistry.TASMANIAN_DEVIL, 1, 3, Items.ROTTEN_FLESH);
        add(AMEntityRegistry.TERRAPIN, 1, 3, Items.SEAGRASS);
        add(AMEntityRegistry.TOUCAN, Items.GOLDEN_APPLE);
        add(AMEntityRegistry.TUSKLIN, 1, 3, Items.BROWN_MUSHROOM);
        add(AMEntityRegistry.WARPED_MOSCO, AMItemRegistry.HEMOLYMPH_SAC);
        add(AMEntityRegistry.WARPED_TOAD, 1, 3, Items.NETHER_WART, Items.WARPED_FUNGUS);
    }
}
