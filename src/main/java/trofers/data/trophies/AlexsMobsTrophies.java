package trofers.data.trophies;

import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import trofers.common.trophy.Trophy;

import java.util.ArrayList;
import java.util.List;

public class AlexsMobsTrophies {

    private static final List<EntityTrophyBuilder> TROPHIES = new ArrayList<>();

    private static EntityTrophyBuilder builder(EntityType<?> entityType, int color) {
        EntityTrophyBuilder builder = new EntityTrophyBuilder(entityType, color) {
            @SuppressWarnings("ConstantConditions")
            @Override
            protected ResourceLocation getDefaultSoundEventId() {
                return new ResourceLocation(entityType.getRegistryName().getNamespace(), entityType.getRegistryName().getPath() + "_idle");
            }
        };
        TROPHIES.add(builder);
        return builder;
    }

    public static List<Trophy> createTrophies() {
        TROPHIES.clear();
        builder(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE, 0x6a5747);
        builder(AMEntityRegistry.ANACONDA, 0x6c7239).offset(0, 0, 2 - 0.375F).scale(0.375F).sound(AMSoundRegistry.ANACONDA_ATTACK);
        builder(AMEntityRegistry.ANTEATER, 0x5a4c47).sound( AMSoundRegistry.ANTEATER_HURT);
        builder(AMEntityRegistry.BALD_EAGLE, 0xc48e2f);
        EntityTrophyBuilder blobfish = builder(AMEntityRegistry.BLOBFISH, 0x7e7d7c).sound(SoundEvents.COD_FLOP);
        blobfish.getTag().putFloat("BlobfishScale", 1);
        builder(AMEntityRegistry.BONE_SERPENT, 0xd1c4ac).offset(0, 0, 2);
        builder(AMEntityRegistry.CACHALOT_WHALE, 0xbababa).offset(0, 0, -1).scale(0.05);
        builder(AMEntityRegistry.CAPUCHIN_MONKEY, 0xeed7b1);
        builder(AMEntityRegistry.CENTIPEDE_HEAD, 0x85435e).offset(0, -4 / 4F, 0).sound(AMSoundRegistry.CENTIPEDE_ATTACK);
        builder(AMEntityRegistry.COCKROACH, 0x835b55).sound(AMSoundRegistry.COCKROACH_HURT);
        builder(AMEntityRegistry.COSMAW, 0x8592dd).offset(0, 0, 1).scale(0.1666);
        builder(AMEntityRegistry.CRIMSON_MOSQUITO, 0xfcbeda).scale(0.1666).sound(AMSoundRegistry.MOSQUITO_LOOP);
        builder(AMEntityRegistry.CROCODILE, 0x676d4c).offset(0, 0, -1.5).scale(0.1666);
        builder(AMEntityRegistry.CROW, 0x43485b);
        builder(AMEntityRegistry.DROPBEAR, 0xbb4a4a);
        EntityTrophyBuilder elephant = builder(AMEntityRegistry.ELEPHANT, 0x7a7572).scale(0.1666);
        elephant.getTag().putInt("Carpet", -1);
        builder(AMEntityRegistry.EMU, 0x746457);
        builder(AMEntityRegistry.ENDERGRADE, 0xcaafe9);
        builder(AMEntityRegistry.ENDERIOPHAGE, 0xa55ca8).sound(AMSoundRegistry.ENDERIOPHAGE_SQUISH);
        builder(AMEntityRegistry.FLY, 0xc19288);
        builder(AMEntityRegistry.FRILLED_SHARK, 0x675c60).sound(SoundEvents.COD_FLOP);
        EntityTrophyBuilder froststalker = builder(AMEntityRegistry.FROSTSTALKER, 0x9fc1fc);
        froststalker.getTag().putBoolean("Spiked", true);
        builder(AMEntityRegistry.GAZELLE, 0xe4b57e).sound(AMSoundRegistry.GAZELLE_HURT);
        builder(AMEntityRegistry.GORILLA, 0x53595d);
        EntityTrophyBuilder grizzly_bear = builder(AMEntityRegistry.GRIZZLY_BEAR, 0x825e3e);
        grizzly_bear.getTag().putBoolean("Honeyed", true);
        builder(AMEntityRegistry.GUSTER, 0xf8e0a8).scale(0.1875);
        builder(AMEntityRegistry.HAMMERHEAD_SHARK, 0x898e9d).offset(0, 0.5, 0).scale(0.2).sound(SoundEvents.COD_FLOP);
        builder(AMEntityRegistry.HUMMINGBIRD, 0x3d966c);
        builder(AMEntityRegistry.KANGAROO, 0xd6a771);
        builder(AMEntityRegistry.KOMODO_DRAGON, 0x736d4a);
        builder(AMEntityRegistry.LAVIATHAN, 0xffdb9b).offset(0, 0, 1).scale(0.075);
        builder(AMEntityRegistry.LEAFCUTTER_ANT, 0xa4582f).sound(AMSoundRegistry.LEAFCUTTER_ANT_HURT);
        builder(AMEntityRegistry.LOBSTER, 0xda5e37).sound(AMSoundRegistry.LOBSTER_HURT);
        builder(AMEntityRegistry.MANED_WOLF, 0xc88748);
        builder(AMEntityRegistry.MANTIS_SHRIMP, 0x15971e).sound(AMSoundRegistry.MANTIS_SHRIMP_SNAP);
        builder(AMEntityRegistry.MIMIC_OCTOPUS, 0xfcede2);
        builder(AMEntityRegistry.MIMICUBE, 0x5e5882).sound(AMSoundRegistry.MIMICUBE_JUMP);
        EntityTrophyBuilder moose = builder(AMEntityRegistry.MOOSE, 0xc89e74).scale(0.1666);
        moose.getTag().putBoolean("Antlered", true);
        builder(AMEntityRegistry.MUNGUS, 0x8679ae);
        builder(AMEntityRegistry.ORCA, 0x71777f).offset(0, 1, -1).scale(0.1).effect(AMEffectRegistry.ORCAS_MIGHT, 2 * 60 * 20).cooldown(4 * 60 * 20);
        EntityTrophyBuilder platypus = builder(AMEntityRegistry.PLATYPUS, 0x62bdba);
        platypus.getTag().putString("CustomName", Component.Serializer.toJson(new TextComponent("perry")));
        EntityTrophyBuilder raccoon = builder(AMEntityRegistry.RACCOON, 0x83807d);
        raccoon.getTag().putInt("Carpet", -1);
        builder(AMEntityRegistry.RATTLESNAKE, 0xccb792).sound(AMSoundRegistry.RATTLESNAKE_LOOP);
        builder(AMEntityRegistry.ROADRUNNER, 0x4d696f);
        builder(AMEntityRegistry.SEAGULL, 0xf8c444);
        builder(AMEntityRegistry.SEAL, 0x65584b);
        builder(AMEntityRegistry.SHOEBILL, 0x8f8f8f).sound(AMSoundRegistry.SHOEBILL_HURT);
        builder(AMEntityRegistry.SNOW_LEOPARD, 0x948a80);
        builder(AMEntityRegistry.SOUL_VULTURE, 0x3dd6e1);
        builder(AMEntityRegistry.SPECTRE, 0xc0c6ff).offset(0, 0, -1.5).scale(0.1);
        builder(AMEntityRegistry.STRADDLER, 0x596980).scale(0.1666);
        builder(AMEntityRegistry.STRADPOLE, 0x596980).sound(AMSoundRegistry.STRADDLER_IDLE);
        builder(AMEntityRegistry.SUNBIRD, 0xfc8865).scale(0.1875).effect(AMEffectRegistry.SUNBIRD_CURSE, 10 * 20).cooldown(0);
        builder(AMEntityRegistry.TARANTULA_HAWK, 0xba4c2d).scale(0.1875).sound(AMSoundRegistry.TARANTULA_HAWK_WING);
        builder(AMEntityRegistry.TASMANIAN_DEVIL, 0xa6b2bd);
        builder(AMEntityRegistry.TIGER, 0xe2b653).effect(AMEffectRegistry.TIGERS_BLESSING, 2 * 60 * 20).cooldown(4 * 60 * 20);
        builder(AMEntityRegistry.TOUCAN, 0xf28d32);
        builder(AMEntityRegistry.TUSKLIN, 0x7c6445).scale(0.2);
        builder(AMEntityRegistry.WARPED_MOSCO, 0x20fcce).scale(0.1666);
        builder(AMEntityRegistry.WARPED_TOAD, 0x179896);
        return TROPHIES.stream().map(EntityTrophyBuilder::createTrophy).toList();
    }
}
