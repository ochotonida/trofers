package trofers.data.trophies;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import trofers.common.trophy.Trophy;

import java.util.ArrayList;
import java.util.List;

public class VanillaTrophies {

    private static final List<EntityTrophyBuilder> TROPHIES = new ArrayList<>();

    private static EntityTrophyBuilder builder(EntityType<?> entityType, int color) {
        EntityTrophyBuilder builder = new EntityTrophyBuilder(entityType, color);
        TROPHIES.add(builder);
        return builder;
    }

    public static List<Trophy> createTrophies() {
        TROPHIES.clear();
        builder(EntityType.AXOLOTL, 0xfbc1e2)
                .sound(SoundEvents.AXOLOTL_IDLE_AIR)
                .getTag().putInt("Variant", 0);
        builder(EntityType.BAT, 0x75653f)
                .effect(MobEffects.NIGHT_VISION, 20 * 40)
                .cooldown(0);
        builder(EntityType.BEE, 0xebc542)
                .sound(SoundEvents.BEE_POLLINATE);
        builder(EntityType.BLAZE, 0xede746);
        EntityTrophyBuilder cat = builder(EntityType.CAT, 0x937155)
                .lootTable("gameplay/cat_morning_gift");
        cat.getTag().putInt("CatType", 0);
        cat.getTag().putBoolean("Sitting", true);
        builder(EntityType.CAVE_SPIDER, 0x147b6a)
                .sound(SoundEvents.SPIDER_AMBIENT);
        builder(EntityType.CHICKEN, 0xffffff)
                .effect(MobEffects.SLOW_FALLING, 20 * 5)
                .cooldown(0);
        builder(EntityType.COD, 0xb6986c)
                .offset(-3/8D, 1, 0)
                .rotate(0, 0, -90)
                .sound(SoundEvents.COD_FLOP);
        builder(EntityType.COW, 0x6c5234)
                .getTag().putUUID("UUID", Util.NIL_UUID);
        builder(EntityType.CREEPER, 0x48b23a)
                .sound(SoundEvents.CREEPER_PRIMED);
        builder(EntityType.DOLPHIN, 0xb0c4d8)
                .effect(MobEffects.DOLPHINS_GRACE, 20 * 40)
                .cooldown(0);
        builder(EntityType.DONKEY, 0x817164);
        builder(EntityType.DROWNED, 0x56847e)
                .effect(MobEffects.WATER_BREATHING, 20 * 40)
                .cooldown(0);
        builder(EntityType.ELDER_GUARDIAN, 0xbfbbaa)
                .scale(0.10625)
                .effect(MobEffects.DIG_SPEED, 20 * 40, 2)
                .cooldown(0);
        builder(EntityType.ENDERMAN, 0xa14fb6)
                .getTag("carriedBlockState").putString("Name", "minecraft:tnt");
        builder(EntityType.ENDERMITE, 0x644b84);
        builder(EntityType.EVOKER, 0x959c9c)
                .cooldown(20 * 60 * 60 * 4);
        EntityTrophyBuilder fox = builder(EntityType.FOX, 0xe37c21)
                .offset(0.75, 0, 0.5)
                .rotate(0, -90, 0);
        fox.getTag().putBoolean("Sleeping", true);
        fox.getTag().putString("Type", "red");
        builder(EntityType.GHAST, 0xf0f0f0)
                .offset(0, 5, 0)
                .scale(0.075);
        builder(EntityType.GLOW_SQUID, 0x32a1a1)
                .offset(0, 5, 0)
                .effect(MobEffects.GLOWING, 20 * 10)
                .cooldown(0)
                .getTag().putInt("DarkTicksRemaining", 1);
        builder(EntityType.GOAT, 0xc2ab8e);
        builder(EntityType.GUARDIAN, 0x6a9087);
        builder(EntityType.HOGLIN, 0xd5957b)
                .getTag().putBoolean("IsImmuneToZombification", true);
        builder(EntityType.HORSE, 0x926633)
                .getTag().putInt("Variant", 1 | 1 << 8);
        builder(EntityType.HUSK, 0xc7ab6f);
        builder(EntityType.IRON_GOLEM, 0xcdb297)
                .sound(SoundEvents.IRON_GOLEM_REPAIR)
                .effect(MobEffects.ABSORPTION, 20 * 60 * 2, 2)
                .getTag().putUUID("UUID", Util.NIL_UUID);
        EntityTrophyBuilder llama = builder(EntityType.LLAMA, 0xe3e4d4);
        llama.getTag().putInt("Variant", 3);
        builder(EntityType.MAGMA_CUBE, 0xff4600)
                .sound(SoundEvents.MAGMA_CUBE_SQUISH)
                .getTag().putInt("Size", 1);
        builder(EntityType.MOOSHROOM, 0xa41012)
                .sound(SoundEvents.COW_AMBIENT);
        builder(EntityType.MULE, 0x89492c);
        builder(EntityType.OCELOT, 0xedb262)
                .effect(MobEffects.MOVEMENT_SPEED, 20 * 40, 3)
                .cooldown(0);
        builder(EntityType.PANDA, 0xe4e4e4)
                .getTag().putString("MainGene", "playful");
        builder(EntityType.PARROT, 0xe60000)
                .getTag().putInt("Variant", 0);
        builder(EntityType.PHANTOM, 0x5161a5)
                .offset(0, 1, 0);
        builder(EntityType.PIG, 0xf1a3a4);
        builder(EntityType.PIGLIN, 0xefb987).lootTable("gameplay/piglin_bartering")
                .getTag().putBoolean("IsImmuneToZombification", true);
        builder(EntityType.PIGLIN_BRUTE, 0xefb987)
                .effect(MobEffects.DAMAGE_BOOST, 20 * 60 * 2, 1)
                .putHandItem(Items.GOLDEN_AXE);
        builder(EntityType.PILLAGER, 0x929c9c).putHandItem(Items.CROSSBOW);
        builder(EntityType.POLAR_BEAR, 0xf2f2f4);
        builder(EntityType.PUFFERFISH, 0xe3970b)
                .sound(SoundEvents.PUFFER_FISH_BLOW_UP)
                .getTag().putInt("PuffState", 2);
        builder(EntityType.RABBIT, 0xa28b72)
                .effect(MobEffects.JUMP, 20 * 40, 3)
                .cooldown(0)
                .getTag().putInt("RabbitType", 0);
        builder(EntityType.RAVAGER, 0x91acab)
                .scale(0.175);
        builder(EntityType.SALMON, 0xa83735)
                .offset(-3/8D, 1, 0)
                .rotate(0, 0, -90)
                .sound(SoundEvents.SALMON_FLOP);
        builder(EntityType.SHEEP, 0xffffff);
        builder(EntityType.SHULKER, 0x986a97)
                .effect(MobEffects.LEVITATION, 20 * 5)
                .cooldown(0);
        builder(EntityType.SILVERFISH, 0x778c99);
        builder(EntityType.SKELETON, 0xbdbdbd)
                .putHandItem(Items.BOW);
        builder(EntityType.SKELETON_HORSE, 0xd0d0d2);
        builder(EntityType.SLIME, 0x77c264).sound(SoundEvents.SLIME_SQUISH)
                .getTag().putInt("Size", 1);
        builder(EntityType.SNOW_GOLEM, 0xffffff)
                .sound(SoundEvents.SNOW_PLACE);
        builder(EntityType.SPIDER, 0x7a6755);
        builder(EntityType.SQUID, 0x546d80)
                .offset(0, 5, 0);
        builder(EntityType.STRAY, 0x607576)
                .putHandItem(Items.BOW);
        builder(EntityType.STRIDER, 0xb44040)
                .effect(MobEffects.FIRE_RESISTANCE, 20 * 40)
                .cooldown(0);
        builder(EntityType.TRADER_LLAMA, 0x425f90)
                .sound(SoundEvents.LLAMA_AMBIENT)
                .getTag().putInt("Variant", 2);
        builder(EntityType.TROPICAL_FISH, 0xFF4040)
                .offset(-3/8D, 1, 0)
                .rotate(0, 0, -90)
                .sound(SoundEvents.TROPICAL_FISH_FLOP)
                .getTag().putInt("Variant", 1 | 1 << 8 | 14 << 16 | 14 << 24);
        builder(EntityType.TURTLE, 0x3ea240)
                .sound(SoundEvents.TURTLE_AMBIENT_LAND)
                .effect(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 2, 3);
        builder(EntityType.VEX, 0x89a0b6);
        EntityTrophyBuilder villager = builder(EntityType.VILLAGER, 0xbf886d)
                .effect(MobEffects.HERO_OF_THE_VILLAGE, 20 * 60 * 2, 4);
        villager.getTag("VillagerData").putInt("level", 1);
        villager.getTag("VillagerData").putString("profession", "minecraft:weaponsmith");
        villager.getTag("VillagerData").putString("type", "minecraft:plains");
        builder(EntityType.VINDICATOR, 0x929c9c);
        builder(EntityType.WANDERING_TRADER, 0x425f90)
                .effect(MobEffects.INVISIBILITY, 20 * 40)
                .cooldown(0);
        builder(EntityType.WITCH, 0xa39482)
                .effect(MobEffects.REGENERATION, 20 * 30);
        builder(EntityType.WITHER_SKELETON, 0x626565)
                .cooldown(20 * 60 * 20)
                .putHandItem(Items.STONE_SWORD);
        EntityTrophyBuilder wolf = builder(EntityType.WOLF, 0xdcdadb);
        wolf.getTag().putBoolean("Sitting", true);
        wolf.getTag().putUUID("Owner", Util.NIL_UUID);
        builder(EntityType.ZOGLIN, 0xe59796);
        builder(EntityType.ZOMBIE, 0x70955c);
        EntityTrophyBuilder zombie_villager = builder(EntityType.ZOMBIE_VILLAGER, 0x76a045);
        zombie_villager.getTag("VillagerData").putInt("level", 1);
        zombie_villager.getTag("VillagerData").putString("profession", "minecraft:weaponsmith");
        zombie_villager.getTag("VillagerData").putString("type", "minecraft:plains");
        builder(EntityType.ZOMBIFIED_PIGLIN, 0xe59796)
                .putHandItem(Items.GOLDEN_SWORD);

        return TROPHIES.stream().map(EntityTrophyBuilder::createTrophy).toList();
    }
}
