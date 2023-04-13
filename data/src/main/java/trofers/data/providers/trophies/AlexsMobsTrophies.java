package trofers.data.providers.trophies;

/*import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;*/
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.trophy.Trophy;

import java.util.ArrayList;
import java.util.List;

public class AlexsMobsTrophies {

    private static final List<EntityTrophyBuilder> TROPHIES = new ArrayList<>();

    private static EntityTrophyBuilder builder(EntityType<?> entityType, int color) {
        EntityTrophyBuilder builder = new EntityTrophyBuilder(entityType, color) {
            @SuppressWarnings("ConstantConditions")
            @Override
            protected ResourceLocation getDefaultSoundEventId() {
                return new ResourceLocation(ForgeRegistries.ENTITY_TYPES.getKey(entityType).getNamespace(), ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath() + "_idle");
            }
        };
        TROPHIES.add(builder);
        return builder;
    }

    public static List<Trophy> createTrophies() {
        TROPHIES.clear();
        /*builder(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE.get(), 0x6a5747);
        builder(AMEntityRegistry.ANACONDA.get(), 0x6c7239)
                .offset(0, 0, 2 - 0.375F)
                .scale(0.375F)
                .sound(AMSoundRegistry.ANACONDA_ATTACK.get());
        builder(AMEntityRegistry.ANTEATER.get(), 0x5a4c47)
                .sound( AMSoundRegistry.ANTEATER_HURT.get());
        builder(AMEntityRegistry.BALD_EAGLE.get(), 0xc48e2f);
        builder(AMEntityRegistry.BANANA_SLUG.get(), 0xfcdd53)
                .sound(AMSoundRegistry.BANANA_SLUG_HURT.get());
        builder(AMEntityRegistry.BLOBFISH.get(), 0x7e7d7c)
                .sound(SoundEvents.COD_FLOP)
                .getTag().putFloat("BlobfishScale", 1);
        builder(AMEntityRegistry.BLUE_JAY.get(), 0x54a4fb);
        builder(AMEntityRegistry.BISON.get(), 0x796445)
                .scale(0.1666);
        builder(AMEntityRegistry.BONE_SERPENT.get(), 0xd1c4ac)
                .offset(0, 0, 2);
        builder(AMEntityRegistry.BUNFUNGUS.get(), 0xd13636)
                .scale(0.1875);
        builder(AMEntityRegistry.CACHALOT_WHALE.get(), 0xbababa)
                .offset(0, 0, -1)
                .scale(0.05);
        builder(AMEntityRegistry.CAPUCHIN_MONKEY.get(), 0xeed7b1);
        builder(AMEntityRegistry.CATFISH.get(), 0xc1c282)
                .offset(0, 1, 0)
                .sound(SoundEvents.COD_FLOP);
        builder(AMEntityRegistry.CENTIPEDE_HEAD.get(), 0x85435e)
                .offset(0, -4 / 4F, 0)
                .sound(AMSoundRegistry.CENTIPEDE_ATTACK.get());
        builder(AMEntityRegistry.COCKROACH.get(), 0x835b55)
                .sound(AMSoundRegistry.COCKROACH_HURT.get());
        builder(AMEntityRegistry.COMB_JELLY.get(), 0xcde6fb)
                .sound(AMSoundRegistry.COMB_JELLY_HURT.get())
                .getTag().putFloat("JellyScale", 1);
        builder(AMEntityRegistry.COSMAW.get(), 0x8592dd)
                .offset(0, 0, 1)
                .scale(0.1666);
        builder(AMEntityRegistry.COSMIC_COD.get(), 0x6e9ddb)
                .sound(AMSoundRegistry.COSMIC_COD_HURT.get());
        builder(AMEntityRegistry.CRIMSON_MOSQUITO.get(), 0xfcbeda)
                .scale(0.1666)
                .sound(AMSoundRegistry.MOSQUITO_LOOP.get());
        builder(AMEntityRegistry.CROCODILE.get(), 0x676d4c)
                .offset(0, 0, -1.5)
                .scale(0.1666);
        builder(AMEntityRegistry.CROW.get(), 0x43485b);
        builder(AMEntityRegistry.DEVILS_HOLE_PUPFISH.get(), 0x557ac2)
                .sound(SoundEvents.COD_FLOP)
                .offset(0, 1, 0)
                .getTag().putFloat("PupfishScale", 1);
        builder(AMEntityRegistry.DROPBEAR.get(), 0xbb4a4a);
        builder(AMEntityRegistry.ELEPHANT.get(), 0x7a7572)
                .scale(0.1666)
                .getTag().putInt("Carpet", -1);
        builder(AMEntityRegistry.EMU.get(), 0x746457);
        builder(AMEntityRegistry.ENDERGRADE.get(), 0x7fbbe8);
        builder(AMEntityRegistry.ENDERIOPHAGE.get(), 0xa55ca8)
                .sound(AMSoundRegistry.ENDERIOPHAGE_SQUISH.get());
        builder(AMEntityRegistry.FARSEER.get(), 0xa194c1)
                .cooldown(60 * 60 * 20);
        builder(AMEntityRegistry.FLUTTER.get(), 0xd885e5);
        builder(AMEntityRegistry.FLY.get(), 0xc19288);
        builder(AMEntityRegistry.FLYING_FISH.get(), 0x7abaea)
                .offset(0, 1, 0)
                .sound(SoundEvents.COD_FLOP);
        builder(AMEntityRegistry.FRILLED_SHARK.get(), 0x675c60)
                .sound(SoundEvents.COD_FLOP);
        builder(AMEntityRegistry.FROSTSTALKER.get(), 0x9fc1fc)
                .getTag().putBoolean("Spiked", true);
        builder(AMEntityRegistry.GAZELLE.get(), 0xe4b57e)
                .sound(AMSoundRegistry.GAZELLE_HURT.get());
        builder(AMEntityRegistry.GELADA_MONKEY.get(), 0xae8a63);
        builder(AMEntityRegistry.GIANT_SQUID.get(), 0xbd6157)
                .sound(AMSoundRegistry.GIANT_SQUID_GAMES.get())
                .rotate(180, 0, 0)
                .offset(0, 1, -4)
                .scale(0.1);
        builder(AMEntityRegistry.GORILLA.get(), 0x53595d);
        builder(AMEntityRegistry.GRIZZLY_BEAR.get(), 0x825e3e)
                .getTag().putBoolean("Honeyed", true);
        builder(AMEntityRegistry.GUSTER.get(), 0xf8e0a8)
                .scale(0.1875);
        builder(AMEntityRegistry.HAMMERHEAD_SHARK.get(), 0x898e9d)
                .offset(0, 0.5, 0)
                .scale(0.2)
                .sound(SoundEvents.COD_FLOP);
        builder(AMEntityRegistry.HUMMINGBIRD.get(), 0x3d966c);
        builder(AMEntityRegistry.JERBOA.get(), 0xdbc388)
                .effect(AMEffectRegistry.FLEET_FOOTED.get(), 2 * 60 * 20, 4 * 60 * 20);
        builder(AMEntityRegistry.KANGAROO.get(), 0xd6a771);
        builder(AMEntityRegistry.KOMODO_DRAGON.get(), 0x736d4a);
        builder(AMEntityRegistry.LAVIATHAN.get(), 0xffdb9b)
                .offset(0, 0, 1)
                .scale(0.075);
        builder(AMEntityRegistry.LEAFCUTTER_ANT.get(), 0xa4582f)
                .sound(AMSoundRegistry.LEAFCUTTER_ANT_HURT.get());
        builder(AMEntityRegistry.LOBSTER.get(), 0xda5e37)
                .sound(AMSoundRegistry.LOBSTER_HURT.get());
        builder(AMEntityRegistry.MANED_WOLF.get(), 0xc88748);
        builder(AMEntityRegistry.MANTIS_SHRIMP.get(), 0x15971e)
                .sound(AMSoundRegistry.MANTIS_SHRIMP_SNAP.get());
        builder(AMEntityRegistry.MIMIC_OCTOPUS.get(), 0xfcede2);
        builder(AMEntityRegistry.MIMICUBE.get(), 0x5e5882)
                .sound(AMSoundRegistry.MIMICUBE_JUMP.get());
        builder(AMEntityRegistry.MOOSE.get(), 0xc89e74)
                .scale(0.1666)
                .getTag().putBoolean("Antlered", true);
        builder(AMEntityRegistry.MUDSKIPPER.get(), 0x5f6f49)
                .sound(AMSoundRegistry.MUDSKIPPER_HURT.get());
        builder(AMEntityRegistry.MUNGUS.get(), 0x8679ae);
        builder(AMEntityRegistry.MURMUR_HEAD.get(), 0x9a5069)
                .scale(0.5)
                .sound(AMSoundRegistry.MURMUR_IDLE.get());
        builder(AMEntityRegistry.POTOO.get(), 0xa18a63)
                .sound(AMSoundRegistry.POTOO_CALL.get());
        builder(AMEntityRegistry.ORCA.get(), 0x71777f)
                .offset(0, 1, -1)
                .scale(0.1)
                .effect(AMEffectRegistry.ORCAS_MIGHT.get(), 2 * 60 * 20)
                .cooldown(4 * 60 * 20);
        builder(AMEntityRegistry.PLATYPUS.get(), 0x62bdba)
                .getTag().putString("CustomName", Component.Serializer.toJson(Component.literal("perry")));
        builder(AMEntityRegistry.RACCOON.get(), 0x83807d)
                .getTag().putInt("Carpet", -1);
        builder(AMEntityRegistry.RAIN_FROG.get(), 0x988863)
                .getTag().putInt("Variant", 2);
        builder(AMEntityRegistry.RATTLESNAKE.get(), 0xccb792)
                .sound(AMSoundRegistry.RATTLESNAKE_LOOP.get());
        builder(AMEntityRegistry.RHINOCEROS.get(), 0x9f9392)
                .scale(0.1666);
        builder(AMEntityRegistry.ROADRUNNER.get(), 0x4d696f);
        builder(AMEntityRegistry.ROCKY_ROLLER.get(), 0xaea58f);
        builder(AMEntityRegistry.SEAGULL.get(), 0xf8c444);
        builder(AMEntityRegistry.SEAL.get(), 0x65584b);
        builder(AMEntityRegistry.SHOEBILL.get(), 0x8f8f8f)
                .sound(AMSoundRegistry.SHOEBILL_HURT.get());
        builder(AMEntityRegistry.SKELEWAG.get(), 0xd6f9af)
                .offset(0, 1, 2)
                .scale(0.1666);
        builder(AMEntityRegistry.SKREECHER.get(), 0x16dbe9)
                .sound(AMSoundRegistry.SKREECHER_DETECT.get());
        builder(AMEntityRegistry.SKUNK.get(), 0xe1e2ef);
        builder(AMEntityRegistry.SNOW_LEOPARD.get(), 0x948a80);
        builder(AMEntityRegistry.SOUL_VULTURE.get(), 0x3dd6e1);
        builder(AMEntityRegistry.SPECTRE.get(), 0xc0c6ff)
                .offset(0, 0, -1.5).scale(0.1);
        builder(AMEntityRegistry.STRADDLER.get(), 0x596980)
                .scale(0.1666);
        builder(AMEntityRegistry.STRADPOLE.get(), 0x596980)
                .sound(AMSoundRegistry.STRADDLER_IDLE.get());
        builder(AMEntityRegistry.SUGAR_GLIDER.get(), 0xdbd9cc);
        builder(AMEntityRegistry.SUNBIRD.get(), 0xfc8865)
                .scale(0.1)
                .effect(AMEffectRegistry.SUNBIRD_CURSE.get(), 10 * 20)
                .cooldown(0);
        builder(AMEntityRegistry.TARANTULA_HAWK.get(), 0xba4c2d)
                .scale(0.1875).sound(AMSoundRegistry.TARANTULA_HAWK_WING.get());
        builder(AMEntityRegistry.TASMANIAN_DEVIL.get(), 0xa6b2bd);
        builder(AMEntityRegistry.TERRAPIN.get(), 0x6d6d2f)
                .sound(AMSoundRegistry.TERRAPIN_HURT.get());
        builder(AMEntityRegistry.TIGER.get(), 0xe2b653)
                .effect(AMEffectRegistry.TIGERS_BLESSING.get(), 2 * 60 * 20)
                .cooldown(4 * 60 * 20);
        builder(AMEntityRegistry.TOUCAN.get(), 0xf28d32);
        builder(AMEntityRegistry.TUSKLIN.get(), 0x7c6445)
                .scale(0.2);
        builder(AMEntityRegistry.UNDERMINER.get(), 0x96bbc4)
                .putHandItem(AMItemRegistry.GHOSTLY_PICKAXE.get())
                .getTag().putBoolean("Dwarf", true);
        builder(AMEntityRegistry.WARPED_MOSCO.get(), 0x20fcce)
                .scale(0.1666);
        builder(AMEntityRegistry.WARPED_TOAD.get(), 0x179896);*/

        return TROPHIES.stream().map(EntityTrophyBuilder::createTrophy).toList();
    }
}
