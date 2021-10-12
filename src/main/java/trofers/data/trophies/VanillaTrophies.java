package trofers.data.trophies;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import trofers.common.trophy.DisplayInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VanillaTrophies extends TrophyBuilder {

    private static final List<EntityType<?>> ENTITIES = Arrays.asList(
            EntityType.AXOLOTL,
            EntityType.BAT,
            EntityType.BEE,
            EntityType.BLAZE,
            EntityType.CAT,
            EntityType.CAVE_SPIDER,
            EntityType.CHICKEN,
            EntityType.COD,
            EntityType.COW,
            EntityType.CREEPER,
            EntityType.DOLPHIN,
            EntityType.DONKEY,
            EntityType.DROWNED,
            EntityType.ELDER_GUARDIAN,
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.EVOKER,
            EntityType.FOX,
            EntityType.GHAST,
            EntityType.GLOW_SQUID,
            EntityType.GOAT,
            EntityType.GUARDIAN,
            EntityType.HOGLIN,
            EntityType.HORSE,
            EntityType.HUSK,
            EntityType.IRON_GOLEM,
            EntityType.LLAMA,
            EntityType.MAGMA_CUBE,
            EntityType.MOOSHROOM,
            EntityType.MULE,
            EntityType.OCELOT,
            EntityType.PANDA,
            EntityType.PARROT,
            EntityType.PHANTOM,
            EntityType.PIG,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER,
            EntityType.POLAR_BEAR,
            EntityType.PUFFERFISH,
            EntityType.RABBIT,
            EntityType.RAVAGER,
            EntityType.SALMON,
            EntityType.SHEEP,
            EntityType.SHULKER,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.SKELETON_HORSE,
            EntityType.SLIME,
            EntityType.SNOW_GOLEM,
            EntityType.SPIDER,
            EntityType.SQUID,
            EntityType.STRAY,
            EntityType.STRIDER,
            EntityType.TRADER_LLAMA,
            EntityType.TROPICAL_FISH,
            EntityType.TURTLE,
            EntityType.VEX,
            EntityType.VILLAGER,
            EntityType.VINDICATOR,
            EntityType.WANDERING_TRADER,
            EntityType.WITCH,
            EntityType.WITHER_SKELETON,
            EntityType.WOLF,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIFIED_PIGLIN
    );

    @Override
    protected List<EntityType<?>> getEntities() {
        return ENTITIES;
    }

    @Override
    public Map<EntityType<?>, Integer> getColors() {
        Map<EntityType<?>, Integer> result = new HashMap<>();

        result.put(EntityType.AXOLOTL, 0xfbc1e2);
        result.put(EntityType.BAT, 0x75653f);
        result.put(EntityType.BEE, 0xebc542);
        result.put(EntityType.BLAZE, 0xede746);
        result.put(EntityType.CAT, 0x937155);
        result.put(EntityType.CAVE_SPIDER, 0x147b6a);
        result.put(EntityType.CHICKEN, 0xffffff);
        result.put(EntityType.COD, 0xb6986c);
        result.put(EntityType.COW, 0x6c5234);
        result.put(EntityType.CREEPER, 0x48b23a);
        result.put(EntityType.DOLPHIN, 0xb0c4d8);
        result.put(EntityType.DONKEY, 0x817164);
        result.put(EntityType.DROWNED, 0x56847e);
        result.put(EntityType.ELDER_GUARDIAN, 0xbfbbaa);
        result.put(EntityType.ENDERMAN, 0xa14fb6);
        result.put(EntityType.ENDERMITE, 0x644b84);
        result.put(EntityType.EVOKER, 0x959c9c);
        result.put(EntityType.FOX, 0xe37c21);
        result.put(EntityType.GHAST, 0xf0f0f0);
        result.put(EntityType.GLOW_SQUID, 0x32a1a1);
        result.put(EntityType.GOAT, 0xc2ab8e);
        result.put(EntityType.GUARDIAN, 0x6a9087);
        result.put(EntityType.HOGLIN, 0xd5957b);
        result.put(EntityType.HORSE, 0x926633);
        result.put(EntityType.HUSK, 0xc7ab6f);
        result.put(EntityType.IRON_GOLEM, 0xcdb297);
        result.put(EntityType.LLAMA, 0xe3e4d4);
        result.put(EntityType.MAGMA_CUBE, 0xff4600);
        result.put(EntityType.MOOSHROOM, 0xa41012);
        result.put(EntityType.MULE, 0x89492c);
        result.put(EntityType.OCELOT, 0xedb262);
        result.put(EntityType.PANDA, 0xe4e4e4);
        result.put(EntityType.PARROT, 0xe60000);
        result.put(EntityType.PHANTOM, 0x5161a5);
        result.put(EntityType.PIG, 0xf1a3a4);
        result.put(EntityType.PIGLIN, 0xefb987);
        result.put(EntityType.PIGLIN_BRUTE, 0xefb987);
        result.put(EntityType.PILLAGER, 0x929c9c);
        result.put(EntityType.POLAR_BEAR, 0xf2f2f4);
        result.put(EntityType.PUFFERFISH, 0xe3970b);
        result.put(EntityType.RABBIT, 0xa28b72);
        result.put(EntityType.RAVAGER, 0x91acab);
        result.put(EntityType.SALMON, 0xa83735);
        result.put(EntityType.SHEEP, 0xffffff);
        result.put(EntityType.SHULKER, 0x986a97);
        result.put(EntityType.SILVERFISH, 0x778c99);
        result.put(EntityType.SKELETON, 0xbdbdbd);
        result.put(EntityType.SKELETON_HORSE, 0xd0d0d2);
        result.put(EntityType.SLIME, 0x77c264);
        result.put(EntityType.SNOW_GOLEM, 0xffffff);
        result.put(EntityType.SPIDER, 0x7a6755);
        result.put(EntityType.SQUID, 0x546d80);
        result.put(EntityType.STRAY, 0x607576);
        result.put(EntityType.STRIDER, 0xb44040);
        result.put(EntityType.TRADER_LLAMA, 0x425f90);
        result.put(EntityType.TROPICAL_FISH, 0xFF4040);
        result.put(EntityType.TURTLE, 0x3ea240);
        result.put(EntityType.VEX, 0x89a0b6);
        result.put(EntityType.VILLAGER, 0xbf886d);
        result.put(EntityType.VINDICATOR, 0x929c9c);
        result.put(EntityType.WANDERING_TRADER, 0x425f90);
        result.put(EntityType.WITCH, 0xa39482);
        result.put(EntityType.WITHER_SKELETON, 0x626565);
        result.put(EntityType.WOLF, 0xdcdadb);
        result.put(EntityType.ZOGLIN, 0xe59796);
        result.put(EntityType.ZOMBIE, 0x70955c);
        result.put(EntityType.ZOMBIE_VILLAGER, 0x76a045);
        result.put(EntityType.ZOMBIFIED_PIGLIN, 0xe59796);

        return result;
    }

    @Override
    public Map<EntityType<?>, DisplayInfo> getDisplayInfos() {
        Map<EntityType<?>, DisplayInfo> result = super.getDisplayInfos();

        result.put(EntityType.GHAST, new DisplayInfo(0, 5, 0, 0.075F));
        result.put(EntityType.GLOW_SQUID, new DisplayInfo(0, 5, 0, 0.25F));
        result.put(EntityType.SQUID, new DisplayInfo(0, 5, 0, 0.25F));
        result.put(EntityType.PHANTOM, new DisplayInfo(0, 1, 0, 0.25F));

        result.put(EntityType.ELDER_GUARDIAN, new DisplayInfo(0.10625F));
        result.put(EntityType.RAVAGER, new DisplayInfo(0.175F));

        result.put(EntityType.FOX, new DisplayInfo(
                0.75F, 0, 0.5F, 0, -90, 0, 0.25F
        ));
        result.put(EntityType.COD, new DisplayInfo(
                -3/8F, 1, 0, 0, 0, -90, 0.25F
        ));
        result.put(EntityType.SALMON, new DisplayInfo(
                -3/8F, 1, 0, 0, 0, -90, 0.25F
        ));
        result.put(EntityType.TROPICAL_FISH, new DisplayInfo(
                -3/8F, 1, 0, 0, 0, -90, 0.25F
        ));

        return result;
    }

    @Override
    public Map<EntityType<?>, CompoundTag> getEntityData() {
        Map<EntityType<?>, CompoundTag> result = super.getEntityData();

        result.get(EntityType.AXOLOTL).putInt("Variant", 0);
        result.get(EntityType.CAT).putInt("CatType", 0);
        result.get(EntityType.CAT).putBoolean("Sitting", true);
        result.get(EntityType.COW).putUUID("UUID", Util.NIL_UUID);
        result.get(EntityType.ENDERMAN).put("carriedBlockState", new CompoundTag());
        result.get(EntityType.ENDERMAN).getCompound("carriedBlockState").putString("Name", "minecraft:tnt");
        result.get(EntityType.FOX).putString("Type", "red");
        result.get(EntityType.FOX).putBoolean("Sleeping", true);
        result.get(EntityType.GLOW_SQUID).putInt("DarkTicksRemaining", 1);
        result.get(EntityType.HOGLIN).putBoolean("IsImmuneToZombification", true);
        result.get(EntityType.HORSE).putInt("Variant", 1 | 1 << 8);
        result.get(EntityType.IRON_GOLEM).putUUID("UUID", Util.NIL_UUID);
        result.get(EntityType.LLAMA).putInt("Variant", 3);
        result.get(EntityType.MAGMA_CUBE).putInt("Size", 1);
        result.get(EntityType.PANDA).putString("MainGene", "playful");
        result.get(EntityType.PARROT).putInt("Variant", 0);
        result.get(EntityType.PIGLIN).putBoolean("IsImmuneToZombification", true);
        result.get(EntityType.PUFFERFISH).putInt("PuffState", 2);
        result.get(EntityType.RABBIT).putInt("RabbitType", 0);
        result.get(EntityType.SLIME).putInt("Size", 1);
        result.get(EntityType.TRADER_LLAMA).putInt("Variant", 2);
        result.get(EntityType.TROPICAL_FISH).putInt("Variant", 1 | 1 << 8 | 14 << 16 | 14 << 24);
        result.get(EntityType.VILLAGER).put("VillagerData", new CompoundTag());
        result.get(EntityType.VILLAGER).getCompound("VillagerData").putInt("level", 1);
        result.get(EntityType.VILLAGER).getCompound("VillagerData").putString("profession", "minecraft:weaponsmith");
        result.get(EntityType.VILLAGER).getCompound("VillagerData").putString("type", "minecraft:plains");
        result.get(EntityType.WOLF).putBoolean("Sitting", true);
        result.get(EntityType.WOLF).putUUID("Owner", Util.NIL_UUID);
        result.get(EntityType.ZOMBIE_VILLAGER).put("VillagerData", new CompoundTag());
        result.get(EntityType.ZOMBIE_VILLAGER).getCompound("VillagerData").putInt("level", 1);
        result.get(EntityType.ZOMBIE_VILLAGER).getCompound("VillagerData").putString("profession", "minecraft:weaponsmith");
        result.get(EntityType.ZOMBIE_VILLAGER).getCompound("VillagerData").putString("type", "minecraft:plains");

        putHandItem(result.get(EntityType.PIGLIN_BRUTE), Items.GOLDEN_AXE);
        putHandItem(result.get(EntityType.PILLAGER), Items.CROSSBOW);
        putHandItem(result.get(EntityType.SKELETON), Items.BOW);
        putHandItem(result.get(EntityType.STRAY), Items.BOW);
        putHandItem(result.get(EntityType.WITHER_SKELETON), Items.STONE_SWORD);
        putHandItem(result.get(EntityType.ZOMBIFIED_PIGLIN), Items.GOLDEN_SWORD);

        return result;
    }

    @Override
    public Map<EntityType<?>, SoundEvent> getSoundEvents() {
        Map<EntityType<?>, SoundEvent> result = super.getSoundEvents();

        result.put(EntityType.AXOLOTL, SoundEvents.AXOLOTL_IDLE_AIR);
        result.put(EntityType.BEE, SoundEvents.BEE_POLLINATE);
        result.put(EntityType.CAVE_SPIDER, SoundEvents.SPIDER_AMBIENT);
        result.put(EntityType.CREEPER, SoundEvents.CREEPER_PRIMED);
        result.put(EntityType.COD, SoundEvents.COD_FLOP);
        result.put(EntityType.IRON_GOLEM, SoundEvents.IRON_GOLEM_REPAIR);
        result.put(EntityType.MAGMA_CUBE, SoundEvents.MAGMA_CUBE_SQUISH);
        result.put(EntityType.MOOSHROOM, SoundEvents.COW_AMBIENT);
        result.put(EntityType.PUFFERFISH, SoundEvents.PUFFER_FISH_BLOW_UP);
        result.put(EntityType.SALMON, SoundEvents.SALMON_FLOP);
        result.put(EntityType.SLIME, SoundEvents.SLIME_SQUISH);
        result.put(EntityType.SNOW_GOLEM, SoundEvents.SNOW_PLACE);
        result.put(EntityType.TRADER_LLAMA, SoundEvents.LLAMA_AMBIENT);
        result.put(EntityType.TROPICAL_FISH, SoundEvents.TROPICAL_FISH_FLOP);
        result.put(EntityType.TURTLE, SoundEvents.TURTLE_AMBIENT_LAND);

        return result;
    }

    @Override
    public Map<EntityType<?>, CompoundTag> getPotionEffects() {
        Map<EntityType<?>, CompoundTag> result = super.getPotionEffects();

        result.put(EntityType.BAT, new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 40).save(new CompoundTag()));
        result.put(EntityType.CHICKEN, new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 5).save(new CompoundTag()));
        result.put(EntityType.DOLPHIN, new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 20 * 40).save(new CompoundTag()));
        result.put(EntityType.DROWNED, new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 40).save(new CompoundTag()));
        result.put(EntityType.ELDER_GUARDIAN, new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 40, 2).save(new CompoundTag()));
        result.put(EntityType.GLOW_SQUID, new MobEffectInstance(MobEffects.GLOWING, 20 * 10).save(new CompoundTag()));
        result.put(EntityType.IRON_GOLEM, new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60 * 2, 2).save(new CompoundTag()));
        result.put(EntityType.OCELOT, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 40, 3).save(new CompoundTag()));
        result.put(EntityType.PIGLIN_BRUTE, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60 * 2, 1).save(new CompoundTag()));
        result.put(EntityType.RABBIT, new MobEffectInstance(MobEffects.JUMP, 20 * 40, 3).save(new CompoundTag()));
        result.put(EntityType.SHULKER, new MobEffectInstance(MobEffects.LEVITATION, 20 * 5).save(new CompoundTag()));
        result.put(EntityType.STRIDER, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 40).save(new CompoundTag()));
        result.put(EntityType.TURTLE, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 2, 3).save(new CompoundTag()));
        result.put(EntityType.VILLAGER, new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 20 * 60 * 2, 4).save(new CompoundTag()));
        result.put(EntityType.WANDERING_TRADER, new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 40).save(new CompoundTag()));
        result.put(EntityType.WITCH, new MobEffectInstance(MobEffects.REGENERATION, 20 * 30).save(new CompoundTag()));

        return result;
    }

    @Override
    public Map<EntityType<?>, Integer> getCooldowns() {
        Map<EntityType<?>, Integer> result = super.getCooldowns();

        result.put(EntityType.BAT, 0);
        result.put(EntityType.CHICKEN, 0);
        result.put(EntityType.DOLPHIN, 0);
        result.put(EntityType.DROWNED, 0);
        result.put(EntityType.ELDER_GUARDIAN, 0);
        result.put(EntityType.EVOKER, 20 * 60 * 60 * 4);
        result.put(EntityType.GLOW_SQUID, 0);
        result.put(EntityType.OCELOT, 0);
        result.put(EntityType.RABBIT, 0);
        result.put(EntityType.SHULKER, 0);
        result.put(EntityType.STRIDER, 0);
        result.put(EntityType.WANDERING_TRADER, 0);
        result.put(EntityType.WITHER_SKELETON, 20 * 60 * 20);

        return result;
    }

    @Override
    public Map<EntityType<?>, ResourceLocation> getLootTables() {
        Map<EntityType<?>, ResourceLocation> result = super.getLootTables();

        result.put(EntityType.CAT, new ResourceLocation("gameplay/cat_morning_gift"));
        result.put(EntityType.PIGLIN, new ResourceLocation("gameplay/piglin_bartering"));

        return result;
    }
}
