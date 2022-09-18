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
        add(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE.get(), AMItemRegistry.SPIKED_SCUTE.get());
        add(AMEntityRegistry.ANACONDA.get(), AMItemRegistry.SHED_SNAKE_SKIN.get());
        add(AMEntityRegistry.ANTEATER.get(), AMItemRegistry.LEAFCUTTER_ANT_PUPA.get());
        add(AMEntityRegistry.BALD_EAGLE.get(), 1, 3, Items.SALMON);
        add(AMEntityRegistry.BISON.get(), AMItemRegistry.BISON_FUR.get());
        add(AMEntityRegistry.BLOBFISH.get(), AMItemRegistry.FISH_OIL.get());
        add(AMEntityRegistry.BONE_SERPENT.get(), AMItemRegistry.BONE_SERPENT_TOOTH.get());
        add(AMEntityRegistry.BUNFUNGUS.get(), 2, 6, Items.MYCELIUM);
        add(AMEntityRegistry.CACHALOT_WHALE.get(), AMItemRegistry.AMBERGRIS.get());
        add(AMEntityRegistry.CAPUCHIN_MONKEY.get(), AMItemRegistry.BANANA.get());
        add(AMEntityRegistry.CATFISH.get(), 1, 3, AMItemRegistry.RAW_CATFISH.get());
        add(AMEntityRegistry.CENTIPEDE_HEAD.get(), AMItemRegistry.CENTIPEDE_LEG.get());
        add(AMEntityRegistry.COCKROACH.get(), AMItemRegistry.MARACA.get());
        add(AMEntityRegistry.COMB_JELLY.get(), AMItemRegistry.RAINBOW_JELLY.get());
        add(AMEntityRegistry.COSMAW.get(), AMItemRegistry.COSMIC_COD.get());
        add(AMEntityRegistry.COSMIC_COD.get(), 1, 3, AMItemRegistry.COSMIC_COD.get());
        add(AMEntityRegistry.CRIMSON_MOSQUITO.get(), 1, 3, AMItemRegistry.BLOOD_SAC.get());
        add(AMEntityRegistry.CROCODILE.get(), AMItemRegistry.CROCODILE_SCUTE.get());
        add(AMEntityRegistry.CROW.get(), 1, 3, Items.PUMPKIN_SEEDS);
        add(AMEntityRegistry.DEVILS_HOLE_PUPFISH.get(), Items.SLIME_BALL);
        add(AMEntityRegistry.DROPBEAR.get(), AMItemRegistry.DROPBEAR_CLAW.get());
        add(AMEntityRegistry.ELEPHANT.get(), 1, 3, AMItemRegistry.ACACIA_BLOSSOM.get());
        add(AMEntityRegistry.EMU.get(), AMItemRegistry.EMU_EGG.get());
        add(AMEntityRegistry.ENDERGRADE.get(), 1, 3, Items.CHORUS_FRUIT);
        add(AMEntityRegistry.ENDERIOPHAGE.get(), 2, 6, AMItemRegistry.ENDERIOPHAGE_ROCKET.get());
        add(AMEntityRegistry.FARSEER.get(), AMItemRegistry.FARSEER_ARM.get());
        add(AMEntityRegistry.FLUTTER.get(), 1, 3, Items.SPORE_BLOSSOM);
        add(AMEntityRegistry.FLY.get(), AMItemRegistry.MAGGOT.get());
        add(AMEntityRegistry.FLYING_FISH.get(), AMItemRegistry.FLYING_FISH.get());
        add(AMEntityRegistry.FRILLED_SHARK.get(), AMItemRegistry.SERRATED_SHARK_TOOTH.get());
        add(AMEntityRegistry.FROSTSTALKER.get(), AMItemRegistry.FROSTSTALKER_HORN.get());
        add(AMEntityRegistry.GAZELLE.get(), AMItemRegistry.GAZELLE_HORN.get());
        add(AMEntityRegistry.GELADA_MONKEY.get(), 1, 3, Items.GRASS);
        add(AMEntityRegistry.GIANT_SQUID.get(), AMItemRegistry.LOST_TENTACLE.get());
        add(AMEntityRegistry.GORILLA.get(), 1, 3, AMItemRegistry.BANANA.get());
        add(AMEntityRegistry.GRIZZLY_BEAR.get(), AMItemRegistry.BEAR_FUR.get());
        add(AMEntityRegistry.GUSTER.get(), AMItemRegistry.GUSTER_EYE.get());
        add(AMEntityRegistry.HAMMERHEAD_SHARK.get(), 1, 3, AMItemRegistry.SHARK_TOOTH.get());
        add(AMEntityRegistry.HUMMINGBIRD.get(), 1, 3, Items.FEATHER);
        add(AMEntityRegistry.KANGAROO.get(), AMItemRegistry.KANGAROO_BURGER.get());
        add(AMEntityRegistry.KOMODO_DRAGON.get(), 1, 3, AMItemRegistry.KOMODO_SPIT.get());
        add(AMEntityRegistry.LAVIATHAN.get(), 2, 6, Items.MAGMA_BLOCK);
        add(AMEntityRegistry.LEAFCUTTER_ANT.get(), AMItemRegistry.GONGYLIDIA.get());
        add(AMEntityRegistry.LOBSTER.get(), AMItemRegistry.LOBSTER_TAIL.get());
        add(AMEntityRegistry.MANED_WOLF.get(), 1, 3, Items.APPLE);
        add(AMEntityRegistry.MANTIS_SHRIMP.get(), 1, 3, Items.COD, Items.SALMON);
        add(AMEntityRegistry.MIMIC_OCTOPUS.get(), Items.TROPICAL_FISH);
        add(AMEntityRegistry.MIMICUBE.get(), AMItemRegistry.MIMICREAM.get());
        add(AMEntityRegistry.MOOSE.get(), AMItemRegistry.MOOSE_ANTLER.get());
        add(AMEntityRegistry.MUDSKIPPER.get(), Items.MUD);
        add(AMEntityRegistry.MUNGUS.get(), AMItemRegistry.MUNGAL_SPORES.get());
        add(AMEntityRegistry.MURMUR_HEAD.get(), AMItemRegistry.ELASTIC_TENDON.get());
        add(AMEntityRegistry.PLATYPUS.get(), 1, 3, Items.REDSTONE_BLOCK);
        add(AMEntityRegistry.POTOO.get(), 1, 3, Items.FEATHER);
        add(AMEntityRegistry.RACCOON.get(), AMItemRegistry.RACCOON_TAIL.get());
        add(AMEntityRegistry.RAIN_FROG.get(), 8, 24, Items.SAND);
        add(AMEntityRegistry.RATTLESNAKE.get(), AMItemRegistry.RATTLESNAKE_RATTLE.get());
        add(AMEntityRegistry.RHINOCEROS.get(), Items.WHEAT);
        add(AMEntityRegistry.ROADRUNNER.get(), AMItemRegistry.ROADRUNNER_FEATHER.get());
        add(AMEntityRegistry.ROCKY_ROLLER.get(), AMItemRegistry.ROCKY_SHELL.get());
        add(AMEntityRegistry.SEAGULL.get(), 2, 6, Items.SAND);
        add(AMEntityRegistry.SEAL.get(), 1, 3, Items.COD, Items.SALMON);
        add(AMEntityRegistry.SHOEBILL.get(), 1, 3, Items.FEATHER);
        add(AMEntityRegistry.SKELEWAG.get(), AMItemRegistry.FISH_BONES.get());
        add(AMEntityRegistry.SKREECHER.get(), AMItemRegistry.SKREECHER_SOUL.get());
        add(AMEntityRegistry.SNOW_LEOPARD.get(), 1, 3, AMItemRegistry.MOOSE_RIBS.get());
        add(AMEntityRegistry.SOUL_VULTURE.get(), AMItemRegistry.SOUL_HEART.get());
        add(AMEntityRegistry.SPECTRE.get(), Items.PHANTOM_MEMBRANE);
        add(AMEntityRegistry.STRADDLER.get(), AMItemRegistry.STRADDLITE.get());
        add(AMEntityRegistry.STRADPOLE.get(), 2, 6, Items.BASALT);
        add(AMEntityRegistry.SUGAR_GLIDER.get(), Items.HONEYCOMB);
        add(AMEntityRegistry.TARANTULA_HAWK.get(), AMItemRegistry.TARANTULA_HAWK_WING_FRAGMENT.get());
        add(AMEntityRegistry.TASMANIAN_DEVIL.get(), 1, 3, Items.ROTTEN_FLESH);
        add(AMEntityRegistry.TERRAPIN.get(), 1, 3, Items.SEAGRASS);
        add(AMEntityRegistry.TOUCAN.get(), Items.GOLDEN_APPLE);
        add(AMEntityRegistry.TUSKLIN.get(), 1, 3, Items.BROWN_MUSHROOM);
        add(AMEntityRegistry.UNDERMINER.get(), 8, 24, Items.RAW_COPPER, Items.RAW_IRON, Items.RAW_GOLD);
        add(AMEntityRegistry.WARPED_MOSCO.get(), AMItemRegistry.HEMOLYMPH_SAC.get());
        add(AMEntityRegistry.WARPED_TOAD.get(), 1, 3, Items.NETHER_WART, Items.WARPED_FUNGUS);
    }
}
