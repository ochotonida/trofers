package trofers.data.providers.trophies;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class VanillaTrophies extends EntityTrophyProvider {

    public VanillaTrophies() {
        super(ResourceLocation.DEFAULT_NAMESPACE);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addTrophies() {
        builder(EntityType.ALLAY)
                .accentColor(0x68ffff)
                .loot(Items.COOKIE)
                .sound(SoundEvents.ALLAY_ITEM_GIVEN);
        builder(EntityType.AXOLOTL)
                .accentColor(0xfbc1e2)
                .sound(SoundEvents.AXOLOTL_IDLE_AIR)
                .putInt("Variant", 0);
        builder(EntityType.BAT)
                .accentColor(0x75653f)
                .mobEffect(MobEffects.NIGHT_VISION, 20);
        builder(EntityType.BEE)
                .accentColor(0xebc542)
                .sound(SoundEvents.BEE_POLLINATE);
        builder(EntityType.BLAZE)
                .loot(Items.BLAZE_POWDER)
                .cooldown(16 * 60)
                .accentColor(0xede746);
        builder(EntityType.CAT)
                .accentColor(0xcccccc)
                .putString("variant", BuiltInRegistries.CAT_VARIANT.getOptional(CatVariant.BLACK).orElseThrow().toString())
                .putBoolean("Sitting", true);
        builder(EntityType.CAVE_SPIDER)
                .accentColor(0x147b6a)
                .sound(SoundEvents.SPIDER_AMBIENT);
        builder(EntityType.CHICKEN)
                .loot(Items.EGG)
                .accentColor(0xffffff);
        builder(EntityType.COD)
                .accentColor(0xb6986c)
                .offset(-3/8D, 1, 0)
                .rotate(0, 0, -90)
                .sound(SoundEvents.COD_FLOP);
        builder(EntityType.COW)
                .accentColor(0x6c5234)
                .putUUID("UUID", Util.NIL_UUID);
        builder(EntityType.CREEPER)
                .accentColor(0x48b23a)
                .sound(SoundEvents.CREEPER_PRIMED);
        builder(EntityType.DOLPHIN)
                .accentColor(0xb0c4d8)
                .mobEffect(MobEffects.DOLPHINS_GRACE, 20);
        builder(EntityType.DONKEY)
                .accentColor(0x817164);
        builder(EntityType.DROWNED)
                .accentColor(0x56847e);
        builder(EntityType.ELDER_GUARDIAN)
                .accentColor(0xbfbbaa)
                .scale(0.10625);
        builder(EntityType.ENDERMAN)
                .accentColor(0xa14fb6)
                .loot(Items.ENDER_PEARL)
                .cooldown(16 * 60)
                .sound(SoundEvents.ENDERMAN_TELEPORT)
                .tag("carriedBlockState", tag -> tag.putString("Name", "minecraft:tnt"));
        builder(EntityType.ENDERMITE)
                .accentColor(0x644b84);
        builder(EntityType.EVOKER)
                .accentColor(0x959c9c);
        builder(EntityType.FOX)
                .accentColor(0xe37c21)
                .loot(Items.SWEET_BERRIES)
                .offset(0.75, 0, 0.5)
                .rotate(0, -90, 0)
                .putBoolean("Sleeping", true)
                .putString("Type", "red");
        builder(EntityType.FROG)
                .accentColor(0x669530)
                .putString("variant", BuiltInRegistries.FROG_VARIANT.getKey(FrogVariant.COLD).toString());
        builder(EntityType.GHAST)
                .accentColor(0xf0f0f0)
                .offset(0, 5, 0)
                .scale(0.075);
        builder(EntityType.GLOW_SQUID)
                .accentColor(0x32a1a1)
                .offset(0, 5, 0)
                .putInt("DarkTicksRemaining", 1);
        builder(EntityType.GOAT)
                .accentColor(0xc2ab8e);
        builder(EntityType.GUARDIAN)
                .accentColor(0x6a9087);
        builder(EntityType.HOGLIN)
                .accentColor(0xd5957b)
                .putBoolean("IsImmuneToZombification", true);
        builder(EntityType.HORSE)
                .accentColor(0x926633)
                .putInt("Variant", 1 | 1 << 8);
        builder(EntityType.HUSK)
                .accentColor(0xc7ab6f);
        builder(EntityType.IRON_GOLEM)
                .accentColor(0xcdb297)
                .sound(SoundEvents.IRON_GOLEM_REPAIR)
                .putUUID("UUID", Util.NIL_UUID);
        builder(EntityType.LLAMA)
                .accentColor(0xe3e4d4)
                .putInt("Variant", 3);
        builder(EntityType.MAGMA_CUBE)
                .accentColor(0xff4600)
                .loot(Items.MAGMA_CREAM)
                .sound(SoundEvents.MAGMA_CUBE_SQUISH)
                .putInt("Size", 1);
        builder(EntityType.MOOSHROOM)
                .accentColor(0xa41012)
                .sound(SoundEvents.COW_AMBIENT);
        builder(EntityType.MULE)
                .accentColor(0x89492c);
        builder(EntityType.OCELOT)
                .accentColor(0xedb262);
        builder(EntityType.PANDA)
                .accentColor(0xe4e4e4)
                .putString("MainGene", "playful");
        builder(EntityType.PARROT)
                .accentColor(0xe60000)
                .putInt("Variant", 0);
        builder(EntityType.PHANTOM)
                .accentColor(0x5161a5)
                .offset(0, 1, 0);
        builder(EntityType.PIG)
                .accentColor(0xf1a3a4)
                .putUUID("UUID", new UUID(2, 0));
        builder(EntityType.PIGLIN)
                .accentColor(0xefb987)
                .putBoolean("IsImmuneToZombification", true);
        builder(EntityType.PIGLIN_BRUTE)
                .accentColor(0xefb987)
                .putHandItem(Items.GOLDEN_AXE);
        builder(EntityType.PILLAGER)
                .accentColor(0x929c9c).putHandItem(Items.CROSSBOW);
        builder(EntityType.POLAR_BEAR)
                .accentColor(0xf2f2f4);
        builder(EntityType.PUFFERFISH)
                .accentColor(0xe3970b)
                .mobEffect(MobEffects.POISON, 5)
                .sound(SoundEvents.PUFFER_FISH_BLOW_UP)
                .putInt("PuffState", 2);
        builder(EntityType.RABBIT)
                .accentColor(0xa28b72)
                .putInt("RabbitType", 0);
        builder(EntityType.RAVAGER)
                .accentColor(0x91acab)
                .scale(0.175);
        builder(EntityType.SALMON)
                .accentColor(0xa83735)
                .offset(-3/8D, 1, 0)
                .rotate(0, 0, -90)
                .sound(SoundEvents.SALMON_FLOP);
        builder(EntityType.SHEEP)
                .accentColor(0xffffff);
        builder(EntityType.SHULKER)
                .accentColor(0x986a97)
                .mobEffect(MobEffects.LEVITATION, 5);
        builder(EntityType.SILVERFISH)
                .accentColor(0x778c99);
        builder(EntityType.SKELETON)
                .accentColor(0xbdbdbd)
                .putHandItem(Items.BOW);
        builder(EntityType.SKELETON_HORSE)
                .accentColor(0xd0d0d2);
        builder(EntityType.SLIME)
                .accentColor(0x77c264)
                .loot(Items.SLIME_BALL)
                .sound(SoundEvents.SLIME_SQUISH)
                .putInt("Size", 1);
        builder(EntityType.SNOW_GOLEM)
                .accentColor(0xffffff)
                .sound(SoundEvents.SNOW_PLACE);
        builder(EntityType.SPIDER)
                .accentColor(0x7a6755);
        builder(EntityType.SQUID)
                .accentColor(0x546d80)
                .offset(0, 5, 0);
        builder(EntityType.STRAY)
                .accentColor(0x607576)
                .putHandItem(Items.BOW);
        builder(EntityType.STRIDER)
                .accentColor(0xb44040);
        builder(EntityType.TADPOLE)
                .accentColor(0x71563f)
                .sound(SoundEvents.TADPOLE_FLOP);
        builder(EntityType.TRADER_LLAMA)
                .accentColor(0x425f90)
                .sound(SoundEvents.LLAMA_AMBIENT)
                .putInt("Variant", 2);
        builder(EntityType.TROPICAL_FISH)
                .accentColor(0xFF4040)
                .offset(-3/8D, 1, 0)
                .rotate(0, 0, -90)
                .sound(SoundEvents.TROPICAL_FISH_FLOP)
                .putInt("Variant", 1 | 1 << 8 | 14 << 16 | 14 << 24);
        builder(EntityType.TURTLE)
                .accentColor(0x3ea240)
                .sound(SoundEvents.TURTLE_AMBIENT_LAND);
        builder(EntityType.VEX)
                .accentColor(0x89a0b6);
        builder(EntityType.VILLAGER)
                .accentColor(0xbf886d)
                .tag("VillagerData", villagerData -> {
                    villagerData.putInt("level", 1);
                    villagerData.putString("profession", "minecraft:weaponsmith");
                    villagerData.putString("type", "minecraft:plains");
                });
        builder(EntityType.VINDICATOR)
                .accentColor(0x929c9c);
        builder(EntityType.WANDERING_TRADER)
                .accentColor(0x425f90);
        builder(EntityType.WITCH)
                .accentColor(0xa39482);
        builder(EntityType.WITHER_SKELETON)
                .accentColor(0x626565)
                .putHandItem(Items.STONE_SWORD);
        builder(EntityType.WOLF)
                .accentColor(0xdcdadb)
                .putBoolean("Sitting", true)
                .putUUID("Owner", Util.NIL_UUID);
        builder(EntityType.ZOGLIN)
                .accentColor(0xe59796);
        builder(EntityType.ZOMBIE)
                .accentColor(0x70955c);
        builder(EntityType.ZOMBIE_VILLAGER)
                .accentColor(0x76a045)
                .tag("VillagerData", villagerData -> {
                    villagerData.putInt("level", 1);
                    villagerData.putString("profession", "minecraft:weaponsmith");
                    villagerData.putString("type", "minecraft:plains");
                });
        builder(EntityType.ZOMBIFIED_PIGLIN)
                .accentColor(0xe59796)
                .putHandItem(Items.GOLDEN_SWORD);
    }
}
