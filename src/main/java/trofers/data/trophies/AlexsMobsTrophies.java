package trofers.data.trophies;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import trofers.common.trophy.DisplayInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AlexsMobsTrophies extends TrophyBuilder {

    private static final List<EntityType<?>> ENTITIES = Arrays.asList(
            AMEntityRegistry.GRIZZLY_BEAR,
            AMEntityRegistry.ROADRUNNER,
            AMEntityRegistry.BONE_SERPENT,
            AMEntityRegistry.GAZELLE,
            AMEntityRegistry.CROCODILE,
            AMEntityRegistry.FLY,
            AMEntityRegistry.HUMMINGBIRD,
            AMEntityRegistry.ORCA,
            AMEntityRegistry.SUNBIRD,
            AMEntityRegistry.GORILLA,
            AMEntityRegistry.CRIMSON_MOSQUITO,
            AMEntityRegistry.RATTLESNAKE,
            AMEntityRegistry.ENDERGRADE,
            AMEntityRegistry.HAMMERHEAD_SHARK,
            AMEntityRegistry.LOBSTER,
            AMEntityRegistry.KOMODO_DRAGON,
            AMEntityRegistry.CAPUCHIN_MONKEY,
            AMEntityRegistry.CENTIPEDE_HEAD,
            AMEntityRegistry.WARPED_TOAD,
            AMEntityRegistry.MOOSE,
            AMEntityRegistry.MIMICUBE,
            AMEntityRegistry.RACCOON,
            AMEntityRegistry.BLOBFISH,
            AMEntityRegistry.SEAL,
            AMEntityRegistry.COCKROACH,
            AMEntityRegistry.SHOEBILL,
            AMEntityRegistry.ELEPHANT,
            AMEntityRegistry.SOUL_VULTURE,
            AMEntityRegistry.SNOW_LEOPARD,
            AMEntityRegistry.SPECTRE,
            AMEntityRegistry.CROW,
            AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE,
            AMEntityRegistry.MUNGUS,
            AMEntityRegistry.MANTIS_SHRIMP,
            AMEntityRegistry.GUSTER,
            AMEntityRegistry.WARPED_MOSCO,
            AMEntityRegistry.STRADDLER,
            AMEntityRegistry.STRADPOLE,
            AMEntityRegistry.EMU,
            AMEntityRegistry.PLATYPUS,
            AMEntityRegistry.DROPBEAR,
            AMEntityRegistry.TASMANIAN_DEVIL,
            AMEntityRegistry.KANGAROO,
            AMEntityRegistry.CACHALOT_WHALE,
            AMEntityRegistry.LEAFCUTTER_ANT,
            AMEntityRegistry.ENDERIOPHAGE,
            AMEntityRegistry.BALD_EAGLE,
            AMEntityRegistry.TIGER,
            AMEntityRegistry.TARANTULA_HAWK,
            AMEntityRegistry.FRILLED_SHARK,
            AMEntityRegistry.MIMIC_OCTOPUS,
            AMEntityRegistry.SEAGULL,
            AMEntityRegistry.FROSTSTALKER,
            AMEntityRegistry.TUSKLIN,
            AMEntityRegistry.LAVIATHAN,
            AMEntityRegistry.COSMAW,
            AMEntityRegistry.ANACONDA,
            AMEntityRegistry.ANTEATER,
            AMEntityRegistry.MANED_WOLF,
            AMEntityRegistry.TOUCAN
    );

    @Override
    protected List<EntityType<?>> getEntities() {
        return ENTITIES;
    }

    @Override
    public String getModId() {
        return AlexsMobs.MODID;
    }

    @Override
    protected Map<EntityType<?>, Integer> getColors() {
        var result = super.getColors();

        result.put(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE, 0x6a5747);
        result.put(AMEntityRegistry.ANACONDA, 0x6c7239);
        result.put(AMEntityRegistry.ANTEATER, 0x5a4c47);
        result.put(AMEntityRegistry.BALD_EAGLE, 0xc48e2f);
        result.put(AMEntityRegistry.BLOBFISH, 0x7e7d7c);
        result.put(AMEntityRegistry.BONE_SERPENT, 0xd1c4ac);
        result.put(AMEntityRegistry.CACHALOT_WHALE, 0xbababa);
        result.put(AMEntityRegistry.CAPUCHIN_MONKEY, 0xeed7b1);
        result.put(AMEntityRegistry.CENTIPEDE_HEAD, 0x85435e);
        result.put(AMEntityRegistry.COCKROACH, 0x835b55);
        result.put(AMEntityRegistry.COSMAW, 0x8592dd);
        result.put(AMEntityRegistry.CRIMSON_MOSQUITO, 0xfcbeda);
        result.put(AMEntityRegistry.CROCODILE, 0x676d4c);
        result.put(AMEntityRegistry.CROW, 0x43485b);
        result.put(AMEntityRegistry.DROPBEAR, 0xbb4a4a);
        result.put(AMEntityRegistry.ELEPHANT, 0x7a7572);
        result.put(AMEntityRegistry.EMU, 0x746457);
        result.put(AMEntityRegistry.ENDERGRADE, 0xcaafe9);
        result.put(AMEntityRegistry.ENDERIOPHAGE, 0xa55ca8);
        result.put(AMEntityRegistry.FLY, 0xc19288);
        result.put(AMEntityRegistry.FRILLED_SHARK, 0x675c60);
        result.put(AMEntityRegistry.FROSTSTALKER, 0x9fc1fc);
        result.put(AMEntityRegistry.GAZELLE, 0xe4b57e);
        result.put(AMEntityRegistry.GORILLA, 0x53595d);
        result.put(AMEntityRegistry.GRIZZLY_BEAR, 0x825e3e);
        result.put(AMEntityRegistry.GUSTER, 0xf8e0a8);
        result.put(AMEntityRegistry.HAMMERHEAD_SHARK, 0x898e9d);
        result.put(AMEntityRegistry.HUMMINGBIRD, 0x3d966c);
        result.put(AMEntityRegistry.KANGAROO, 0xd6a771);
        result.put(AMEntityRegistry.KOMODO_DRAGON, 0x736d4a);
        result.put(AMEntityRegistry.LAVIATHAN, 0xffdb9b);
        result.put(AMEntityRegistry.LEAFCUTTER_ANT, 0xa4582f);
        result.put(AMEntityRegistry.LOBSTER, 0xda5e37);
        result.put(AMEntityRegistry.MANED_WOLF, 0xc88748);
        result.put(AMEntityRegistry.MANTIS_SHRIMP, 0x15971e);
        result.put(AMEntityRegistry.MIMIC_OCTOPUS, 0xfcede2);
        result.put(AMEntityRegistry.MIMICUBE, 0x5e5882);
        result.put(AMEntityRegistry.MOOSE, 0xc89e74);
        result.put(AMEntityRegistry.MUNGUS, 0x8679ae);
        result.put(AMEntityRegistry.ORCA, 0x71777f);
        result.put(AMEntityRegistry.PLATYPUS, 0x62bdba);
        result.put(AMEntityRegistry.RACCOON, 0x83807d);
        result.put(AMEntityRegistry.RATTLESNAKE, 0xccb792);
        result.put(AMEntityRegistry.ROADRUNNER, 0x4d696f);
        result.put(AMEntityRegistry.SEAGULL, 0xf8c444);
        result.put(AMEntityRegistry.SEAL, 0x65584b);
        result.put(AMEntityRegistry.SHOEBILL, 0x8f8f8f);
        result.put(AMEntityRegistry.SNOW_LEOPARD, 0x948a80);
        result.put(AMEntityRegistry.SOUL_VULTURE, 0x3dd6e1);
        result.put(AMEntityRegistry.SPECTRE, 0xc0c6ff);
        result.put(AMEntityRegistry.STRADDLER, 0x596980);
        result.put(AMEntityRegistry.STRADPOLE, 0x596980);
        result.put(AMEntityRegistry.SUNBIRD, 0xfc8865);
        result.put(AMEntityRegistry.TARANTULA_HAWK, 0xba4c2d);
        result.put(AMEntityRegistry.TASMANIAN_DEVIL, 0xa6b2bd);
        result.put(AMEntityRegistry.TIGER, 0xe2b653);
        result.put(AMEntityRegistry.TOUCAN, 0xf28d32);
        result.put(AMEntityRegistry.TUSKLIN, 0x7c6445);
        result.put(AMEntityRegistry.WARPED_MOSCO, 0x20fcce);
        result.put(AMEntityRegistry.WARPED_TOAD, 0x179896);

        return result;
    }

    @Override
    public Map<EntityType<?>, DisplayInfo> getDisplayInfos() {
        Map<EntityType<?>, DisplayInfo> result = super.getDisplayInfos();

        result.put(AMEntityRegistry.CENTIPEDE_HEAD, new DisplayInfo(0, -4 / 4F, 0, 0.25F));
        result.put(AMEntityRegistry.ANACONDA, new DisplayInfo(0, 0, 2 - 0.375F, 0.375F));

        result.put(AMEntityRegistry.BONE_SERPENT, new DisplayInfo(0, 0, 2, 0.25F));
        result.put(AMEntityRegistry.CACHALOT_WHALE, new DisplayInfo(0, 0, -1, 0.05F));
        result.put(AMEntityRegistry.COSMAW, new DisplayInfo(0, 0, 1, 0.1666F));
        result.put(AMEntityRegistry.CROCODILE, new DisplayInfo(0, 0, -1.5F, 0.1666F));
        result.put(AMEntityRegistry.HAMMERHEAD_SHARK, new DisplayInfo(0, 0.5F, 0, 0.20F));
        result.put(AMEntityRegistry.LAVIATHAN, new DisplayInfo(0, 0, 1, 0.075F));
        result.put(AMEntityRegistry.ORCA, new DisplayInfo(0, 1, -1, 0.10F));
        result.put(AMEntityRegistry.SPECTRE, new DisplayInfo(0, 0, -1.5F, 0.10F));

        result.put(AMEntityRegistry.CRIMSON_MOSQUITO, new DisplayInfo(0.1666F));
        result.put(AMEntityRegistry.ELEPHANT, new DisplayInfo(0.1666F));
        result.put(AMEntityRegistry.GUSTER, new DisplayInfo(0.1875F));
        result.put(AMEntityRegistry.MOOSE, new DisplayInfo(0.1666F));
        result.put(AMEntityRegistry.STRADDLER, new DisplayInfo(0.1666F));
        result.put(AMEntityRegistry.SUNBIRD, new DisplayInfo(0.1875F));
        result.put(AMEntityRegistry.TARANTULA_HAWK, new DisplayInfo(0.1875F));
        result.put(AMEntityRegistry.TUSKLIN, new DisplayInfo(0.2F));
        result.put(AMEntityRegistry.WARPED_MOSCO, new DisplayInfo(0.1666F));

        return result;
    }

    @Override
    protected Map<EntityType<?>, CompoundTag> getEntityData() {
        var result = super.getEntityData();

        result.get(AMEntityRegistry.BLOBFISH).putFloat("BlobfishScale", 1);
        result.get(AMEntityRegistry.ELEPHANT).putInt("Carpet", -1);
        result.get(AMEntityRegistry.FROSTSTALKER).putBoolean("Spiked", true);
        result.get(AMEntityRegistry.GRIZZLY_BEAR).putBoolean("Honeyed", true);
        result.get(AMEntityRegistry.MOOSE).putBoolean("Antlered", true);
        result.get(AMEntityRegistry.PLATYPUS).putString("CustomName", Component.Serializer.toJson(new TextComponent("perry")));
        result.get(AMEntityRegistry.RACCOON).putInt("Carpet", -1);

        return result;
    }

    @Override
    protected Map<EntityType<?>, SoundEvent> getSoundEvents() {
        var result = super.getSoundEvents();

        result.put(AMEntityRegistry.GAZELLE, AMSoundRegistry.GAZELLE_HURT);
        result.put(AMEntityRegistry.CRIMSON_MOSQUITO, AMSoundRegistry.MOSQUITO_LOOP);
        result.put(AMEntityRegistry.RATTLESNAKE, AMSoundRegistry.RATTLESNAKE_LOOP);
        result.put(AMEntityRegistry.HAMMERHEAD_SHARK, SoundEvents.COD_FLOP);
        result.put(AMEntityRegistry.LOBSTER, AMSoundRegistry.LOBSTER_HURT);
        result.put(AMEntityRegistry.CENTIPEDE_HEAD, AMSoundRegistry.CENTIPEDE_ATTACK);
        result.put(AMEntityRegistry.MIMICUBE, AMSoundRegistry.MIMICUBE_JUMP);
        result.put(AMEntityRegistry.BLOBFISH, SoundEvents.COD_FLOP);
        result.put(AMEntityRegistry.COCKROACH, AMSoundRegistry.COCKROACH_HURT);
        result.put(AMEntityRegistry.SHOEBILL, AMSoundRegistry.SHOEBILL_HURT);
        result.put(AMEntityRegistry.MANTIS_SHRIMP, AMSoundRegistry.MANTIS_SHRIMP_SNAP);
        result.put(AMEntityRegistry.STRADPOLE, AMSoundRegistry.STRADDLER_IDLE);
        result.put(AMEntityRegistry.LEAFCUTTER_ANT, AMSoundRegistry.LEAFCUTTER_ANT_HURT);
        result.put(AMEntityRegistry.ENDERIOPHAGE, AMSoundRegistry.ENDERIOPHAGE_SQUISH);
        result.put(AMEntityRegistry.TARANTULA_HAWK, AMSoundRegistry.TARANTULA_HAWK_WING);
        result.put(AMEntityRegistry.FRILLED_SHARK, SoundEvents.COD_FLOP);
        result.put(AMEntityRegistry.ANACONDA, AMSoundRegistry.ANACONDA_ATTACK);
        result.put(AMEntityRegistry.ANTEATER, AMSoundRegistry.ANTEATER_HURT);

        return result;
    }

    @Override
    protected ResourceLocation getSoundEvent(EntityType<?> type) {
        // noinspection ConstantConditions
        return new ResourceLocation(type.getRegistryName().getNamespace(), type.getRegistryName().getPath() + "_idle");
    }

    @Override
    protected Map<EntityType<?>, CompoundTag> getPotionEffects() {
        var result = super.getPotionEffects();

        result.put(AMEntityRegistry.ORCA, createEffect(AMEffectRegistry.ORCAS_MIGHT, 2 * 60 * 20));
        result.put(AMEntityRegistry.SUNBIRD, createEffect(AMEffectRegistry.SUNBIRD_CURSE, 10 * 20));
        result.put(AMEntityRegistry.TIGER, createEffect(AMEffectRegistry.TIGERS_BLESSING, 2 * 60 * 20));

        return result;
    }

    @Override
    protected Map<EntityType<?>, Integer> getCooldowns() {
        var result = super.getCooldowns();

        result.put(AMEntityRegistry.ORCA, 4 * 60 * 20);
        result.put(AMEntityRegistry.TIGER, 4 * 60 * 20);

        result.put(AMEntityRegistry.SUNBIRD, 0);
        return result;
    }
}
