package trofers.data.providers.trophies;

import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class AlexsMobsTrophies extends EntityTrophyProvider {

    public AlexsMobsTrophies() {
        super("alexsmobs");
    }

    @Override
    protected ResourceLocation getEntitySound(String entityName, String entitySoundName) {
        return id("%s_%s".formatted(entityName, entitySoundName));
    }

    @Override
    protected String getDefaultSoundName() {
        return "idle";
    }

    @Override
    public void addTrophies() {
        builder(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE.get())
                .accentColor(0x6a5747);
        builder(AMEntityRegistry.ANACONDA.get())
                .accentColor(0x6c7239)
                .offset(0, 0, 2 - 0.375F)
                .scale(0.375F)
                .sound(AMSoundRegistry.ANACONDA_ATTACK.get());
        builder(AMEntityRegistry.ANTEATER.get())
                .accentColor(0x5a4c47)
                .sound(AMSoundRegistry.ANTEATER_HURT.get());
        builder(AMEntityRegistry.BALD_EAGLE.get())
                .accentColor(0xc48e2f);
        builder(AMEntityRegistry.BANANA_SLUG.get())
                .accentColor(0xfcdd53)
                .sound(AMSoundRegistry.BANANA_SLUG_HURT.get());
        builder(AMEntityRegistry.BLOBFISH.get())
                .accentColor(0x7e7d7c)
                .sound(SoundEvents.COD_FLOP)
                .putFloat("BlobfishScale", 1);
        builder(AMEntityRegistry.BLUE_JAY.get())
                .accentColor(0x54a4fb);
        builder(AMEntityRegistry.BISON.get())
                .accentColor(0x796445)
                .scale(0.1666);
        builder(AMEntityRegistry.BONE_SERPENT.get())
                .accentColor(0xd1c4ac)
                .offset(0, 0, 2);
        builder(AMEntityRegistry.BUNFUNGUS.get())
                .accentColor(0xd13636)
                .scale(0.1875);
        builder(AMEntityRegistry.CACHALOT_WHALE.get())
                .accentColor(0xbababa)
                .offset(0, 0, -1)
                .scale(0.05);
        builder(AMEntityRegistry.CAPUCHIN_MONKEY.get())
                .accentColor(0xeed7b1);
        builder(AMEntityRegistry.CATFISH.get())
                .accentColor(0xc1c282)
                .offset(0, 1, 0)
                .sound(SoundEvents.COD_FLOP);
        builder(AMEntityRegistry.CENTIPEDE_HEAD.get())
                .accentColor(0x85435e)
                .offset(0, -4 / 4F, 0)
                .sound(AMSoundRegistry.CENTIPEDE_ATTACK.get());
        builder(AMEntityRegistry.COCKROACH.get())
                .accentColor(0x835b55)
                .sound(AMSoundRegistry.COCKROACH_HURT.get());
        builder(AMEntityRegistry.COMB_JELLY.get())
                .accentColor(0xcde6fb)
                .sound(AMSoundRegistry.COMB_JELLY_HURT.get())
                .putFloat("JellyScale", 1);
        builder(AMEntityRegistry.COSMAW.get())
                .accentColor(0x8592dd)
                .offset(0, 0, 1)
                .scale(0.1666);
        builder(AMEntityRegistry.COSMIC_COD.get())
                .accentColor(0x6e9ddb)
                .sound(AMSoundRegistry.COSMIC_COD_HURT.get());
        builder(AMEntityRegistry.CRIMSON_MOSQUITO.get())
                .accentColor(0xfcbeda)
                .scale(0.1666)
                .sound(AMSoundRegistry.MOSQUITO_LOOP.get());
        builder(AMEntityRegistry.CROCODILE.get())
                .accentColor(0x676d4c)
                .offset(0, 0, -1.5)
                .scale(0.1666);
        builder(AMEntityRegistry.CROW.get())
                .accentColor(0x43485b);
        builder(AMEntityRegistry.DEVILS_HOLE_PUPFISH.get())
                .accentColor(0x557ac2)
                .sound(SoundEvents.COD_FLOP)
                .offset(0, 1, 0)
                .putFloat("PupfishScale", 1);
        builder(AMEntityRegistry.DROPBEAR.get())
                .accentColor(0xbb4a4a);
        builder(AMEntityRegistry.ELEPHANT.get())
                .accentColor(0x7a7572)
                .scale(0.1666)
                .putInt("Carpet", -1);
        builder(AMEntityRegistry.EMU.get())
                .accentColor(0x746457);
        builder(AMEntityRegistry.ENDERGRADE.get())
                .accentColor(0x7fbbe8);
        builder(AMEntityRegistry.ENDERIOPHAGE.get())
                .accentColor(0xa55ca8)
                .sound(AMSoundRegistry.ENDERIOPHAGE_SQUISH.get());
        builder(AMEntityRegistry.FARSEER.get())
                .accentColor(0xa194c1);
        builder(AMEntityRegistry.FLUTTER.get())
                .accentColor(0xd885e5);
        builder(AMEntityRegistry.FLY.get())
                .accentColor(0xc19288);
        builder(AMEntityRegistry.FLYING_FISH.get())
                .accentColor(0x7abaea)
                .offset(0, 1, 0)
                .sound(SoundEvents.COD_FLOP);
        builder(AMEntityRegistry.FRILLED_SHARK.get())
                .accentColor(0x675c60)
                .sound(SoundEvents.COD_FLOP);
        builder(AMEntityRegistry.FROSTSTALKER.get())
                .accentColor(0x9fc1fc)
                .putBoolean("Spiked", true);
        builder(AMEntityRegistry.GAZELLE.get())
                .accentColor(0xe4b57e)
                .sound(AMSoundRegistry.GAZELLE_HURT.get());
        builder(AMEntityRegistry.GELADA_MONKEY.get())
                .accentColor(0xae8a63);
        builder(AMEntityRegistry.GIANT_SQUID.get())
                .accentColor(0xbd6157)
                .sound(AMSoundRegistry.GIANT_SQUID_GAMES.get())
                .rotate(180, 0, 0)
                .offset(0, 1, -4)
                .scale(0.1);
        builder(AMEntityRegistry.GORILLA.get())
                .accentColor(0x53595d);
        builder(AMEntityRegistry.GRIZZLY_BEAR.get())
                .accentColor(0x825e3e)
                .putBoolean("Honeyed", true);
        builder(AMEntityRegistry.GUSTER.get())
                .accentColor(0xf8e0a8)
                .scale(0.1875);
        builder(AMEntityRegistry.HAMMERHEAD_SHARK.get())
                .accentColor(0x898e9d)
                .offset(0, 0.5, 0)
                .scale(0.2)
                .sound(SoundEvents.COD_FLOP);
        builder(AMEntityRegistry.HUMMINGBIRD.get())
                .accentColor(0x3d966c);
        builder(AMEntityRegistry.JERBOA.get())
                .accentColor(0xdbc388);
        builder(AMEntityRegistry.KANGAROO.get())
                .accentColor(0xd6a771);
        builder(AMEntityRegistry.KOMODO_DRAGON.get())
                .accentColor(0x736d4a);
        builder(AMEntityRegistry.LAVIATHAN.get())
                .accentColor(0xffdb9b)
                .offset(0, 0, 1)
                .scale(0.075);
        builder(AMEntityRegistry.LEAFCUTTER_ANT.get())
                .accentColor(0xa4582f)
                .sound(AMSoundRegistry.LEAFCUTTER_ANT_HURT.get());
        builder(AMEntityRegistry.LOBSTER.get())
                .accentColor(0xda5e37)
                .sound(AMSoundRegistry.LOBSTER_HURT.get());
        builder(AMEntityRegistry.MANED_WOLF.get())
                .accentColor(0xc88748);
        builder(AMEntityRegistry.MANTIS_SHRIMP.get())
                .accentColor(0x15971e)
                .sound(AMSoundRegistry.MANTIS_SHRIMP_SNAP.get());
        builder(AMEntityRegistry.MIMIC_OCTOPUS.get())
                .accentColor(0xfcede2);
        builder(AMEntityRegistry.MIMICUBE.get())
                .accentColor(0x5e5882)
                .sound(AMSoundRegistry.MIMICUBE_JUMP.get());
        builder(AMEntityRegistry.MOOSE.get())
                .accentColor(0xc89e74)
                .scale(0.1666)
                .putBoolean("Antlered", true);
        builder(AMEntityRegistry.MUDSKIPPER.get())
                .accentColor(0x5f6f49)
                .sound(AMSoundRegistry.MUDSKIPPER_HURT.get());
        builder(AMEntityRegistry.MUNGUS.get())
                .accentColor(0x8679ae);
        builder(AMEntityRegistry.MURMUR_HEAD.get())
                .accentColor(0x9a5069)
                .scale(0.5)
                .sound(AMSoundRegistry.MURMUR_IDLE.get());
        builder(AMEntityRegistry.POTOO.get())
                .accentColor(0xa18a63)
                .sound(AMSoundRegistry.POTOO_CALL.get());
        builder(AMEntityRegistry.ORCA.get())
                .accentColor(0x71777f)
                .offset(0, 1, -1)
                .scale(0.1);
        builder(AMEntityRegistry.PLATYPUS.get())
                .accentColor(0x62bdba)
                .putCustomName("perry");
        builder(AMEntityRegistry.RACCOON.get())
                .accentColor(0x83807d)
                .putInt("Carpet", -1);
        builder(AMEntityRegistry.RAIN_FROG.get())
                .accentColor(0x988863)
                .putInt("Variant", 2);
        builder(AMEntityRegistry.RATTLESNAKE.get())
                .accentColor(0xccb792)
                .sound(AMSoundRegistry.RATTLESNAKE_LOOP.get());
        builder(AMEntityRegistry.RHINOCEROS.get())
                .accentColor(0x9f9392)
                .scale(0.1666);
        builder(AMEntityRegistry.ROADRUNNER.get())
                .accentColor(0x4d696f);
        builder(AMEntityRegistry.ROCKY_ROLLER.get())
                .accentColor(0xaea58f);
        builder(AMEntityRegistry.SEAGULL.get())
                .accentColor(0xf8c444);
        builder(AMEntityRegistry.SEAL.get())
                .accentColor(0x65584b);
        builder(AMEntityRegistry.SHOEBILL.get())
                .accentColor(0x8f8f8f)
                .sound(AMSoundRegistry.SHOEBILL_HURT.get());
        builder(AMEntityRegistry.SKELEWAG.get())
                .accentColor(0xd6f9af)
                .offset(0, 1, 2)
                .scale(0.1666);
        builder(AMEntityRegistry.SKREECHER.get())
                .accentColor(0x16dbe9)
                .sound(AMSoundRegistry.SKREECHER_DETECT.get());
        builder(AMEntityRegistry.SKUNK.get())
                .accentColor(0xe1e2ef);
        builder(AMEntityRegistry.SNOW_LEOPARD.get())
                .accentColor(0x948a80);
        builder(AMEntityRegistry.SOUL_VULTURE.get())
                .accentColor(0x3dd6e1);
        builder(AMEntityRegistry.SPECTRE.get())
                .accentColor(0xc0c6ff)
                .offset(0, 0, -1.5).scale(0.1);
        builder(AMEntityRegistry.STRADDLER.get())
                .accentColor(0x596980)
                .scale(0.1666);
        builder(AMEntityRegistry.STRADPOLE.get())
                .accentColor(0x596980)
                .sound(AMSoundRegistry.STRADDLER_IDLE.get());
        builder(AMEntityRegistry.SUGAR_GLIDER.get())
                .accentColor(0xdbd9cc);
        builder(AMEntityRegistry.SUNBIRD.get())
                .accentColor(0xfc8865)
                .scale(0.1);
        builder(AMEntityRegistry.TARANTULA_HAWK.get())
                .accentColor(0xba4c2d)
                .scale(0.1875).sound(AMSoundRegistry.TARANTULA_HAWK_WING.get());
        builder(AMEntityRegistry.TASMANIAN_DEVIL.get())
                .accentColor(0xa6b2bd);
        builder(AMEntityRegistry.TERRAPIN.get())
                .accentColor(0x6d6d2f)
                .sound(AMSoundRegistry.TERRAPIN_HURT.get());
        builder(AMEntityRegistry.TIGER.get())
                .accentColor(0xe2b653);
        builder(AMEntityRegistry.TOUCAN.get())
                .accentColor(0xf28d32);
        builder(AMEntityRegistry.TUSKLIN.get())
                .accentColor(0x7c6445)
                .scale(0.2);
        builder(AMEntityRegistry.UNDERMINER.get())
                .accentColor(0x96bbc4)
                .putHandItem(AMItemRegistry.GHOSTLY_PICKAXE.get())
                .putBoolean("Dwarf", true);
        builder(AMEntityRegistry.WARPED_MOSCO.get())
                .accentColor(0x20fcce)
                .scale(0.1666);
        builder(AMEntityRegistry.WARPED_TOAD.get())
                .accentColor(0x179896);
    }
}
