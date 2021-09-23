package trofers.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.common.trophy.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class Trophies implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    protected final Map<ResourceLocation, Trophy> trophies = new HashMap<>();
    private final DataGenerator generator;

    protected Trophies(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    protected void addTrophies() {
        trophies.clear();

        Map<EntityType<?>, Integer> colors = new HashMap<>();
        Map<EntityType<?>, DisplayInfo> displayInfos = new HashMap<>();
        Map<EntityType<?>, CompoundTag> entityData = new HashMap<>();
        Map<EntityType<?>, SoundEvent> soundEvents = new HashMap<>();
        Map<EntityType<?>, CompoundTag> potionEffects = new HashMap<>();
        Map<EntityType<?>, Integer> cooldowns = new HashMap<>();
        Map<EntityType<?>, ResourceLocation> lootTables = new HashMap<>();

        colors.put(EntityType.AXOLOTL, 0xfbc1e2);
        colors.put(EntityType.BAT, 0x75653f);
        colors.put(EntityType.BEE, 0xebc542);
        colors.put(EntityType.BLAZE, 0xede746);
        colors.put(EntityType.CAT, 0x937155);
        colors.put(EntityType.CAVE_SPIDER, 0x147b6a);
        colors.put(EntityType.CHICKEN, 0xffffff);
        colors.put(EntityType.COD, 0xb6986c);
        colors.put(EntityType.COW, 0x6c5234);
        colors.put(EntityType.CREEPER, 0x48b23a);
        colors.put(EntityType.DOLPHIN, 0xb0c4d8);
        colors.put(EntityType.DONKEY, 0x817164);
        colors.put(EntityType.DROWNED, 0x56847e);
        colors.put(EntityType.ELDER_GUARDIAN, 0xbfbbaa);
        colors.put(EntityType.ENDERMAN, 0xa14fb6);
        colors.put(EntityType.ENDERMITE, 0x644b84);
        colors.put(EntityType.EVOKER, 0x959c9c);
        colors.put(EntityType.FOX, 0xe37c21);
        colors.put(EntityType.GHAST, 0xf0f0f0);
        colors.put(EntityType.GLOW_SQUID, 0x32a1a1);
        colors.put(EntityType.GOAT, 0xc2ab8e);
        colors.put(EntityType.GUARDIAN, 0x6a9087);
        colors.put(EntityType.HOGLIN, 0xd5957b);
        colors.put(EntityType.HORSE, 0x926633);
        colors.put(EntityType.HUSK, 0xc7ab6f);
        colors.put(EntityType.IRON_GOLEM, 0xcdb297);
        colors.put(EntityType.LLAMA, 0xe3e4d4);
        colors.put(EntityType.MAGMA_CUBE, 0xff4600);
        colors.put(EntityType.MOOSHROOM, 0xa41012);
        colors.put(EntityType.MULE, 0x89492c);
        colors.put(EntityType.OCELOT, 0xedb262);
        colors.put(EntityType.PANDA, 0xe4e4e4);
        colors.put(EntityType.PARROT, 0xe60000);
        colors.put(EntityType.PHANTOM, 0x5161a5);
        colors.put(EntityType.PIG, 0xf1a3a4);
        colors.put(EntityType.PIGLIN, 0xefb987);
        colors.put(EntityType.PIGLIN_BRUTE, 0xefb987);
        colors.put(EntityType.PILLAGER, 0x929c9c);
        colors.put(EntityType.POLAR_BEAR, 0xf2f2f4);
        colors.put(EntityType.PUFFERFISH, 0xe3970b);
        colors.put(EntityType.RABBIT, 0xa28b72);
        colors.put(EntityType.RAVAGER, 0x91acab);
        colors.put(EntityType.SALMON, 0xa83735);
        colors.put(EntityType.SHEEP, 0xffffff);
        colors.put(EntityType.SHULKER, 0x986a97);
        colors.put(EntityType.SILVERFISH, 0x778c99);
        colors.put(EntityType.SKELETON, 0xbdbdbd);
        colors.put(EntityType.SKELETON_HORSE, 0xd0d0d2);
        colors.put(EntityType.SLIME, 0x77c264);
        colors.put(EntityType.SNOW_GOLEM, 0xffffff);
        colors.put(EntityType.SPIDER, 0x7a6755);
        colors.put(EntityType.SQUID, 0x546d80);
        colors.put(EntityType.STRAY, 0x607576);
        colors.put(EntityType.STRIDER, 0xb44040);
        colors.put(EntityType.TRADER_LLAMA, 0x425f90);
        colors.put(EntityType.TROPICAL_FISH, 0xFF4040);
        colors.put(EntityType.TURTLE, 0x3ea240);
        colors.put(EntityType.VEX, 0x89a0b6);
        colors.put(EntityType.VILLAGER, 0xbf886d);
        colors.put(EntityType.VINDICATOR, 0x929c9c);
        colors.put(EntityType.WANDERING_TRADER, 0x425f90);
        colors.put(EntityType.WITCH, 0xa39482);
        colors.put(EntityType.WITHER_SKELETON, 0x626565);
        colors.put(EntityType.WOLF, 0xdcdadb);
        colors.put(EntityType.ZOGLIN, 0xe59796);
        colors.put(EntityType.ZOMBIE, 0x70955c);
        colors.put(EntityType.ZOMBIE_VILLAGER, 0x76a045);
        colors.put(EntityType.ZOMBIFIED_PIGLIN, 0xe59796);

        colors.keySet().forEach(type -> {
            displayInfos.put(type, new DisplayInfo(0, 0, 0, 0, 0, 0, 0.25F));
            entityData.put(type, new CompoundTag());
            potionEffects.put(type, new CompoundTag());
            cooldowns.put(type, 20 * 60 * 8);
            // noinspection ConstantConditions
            lootTables.put(type, new ResourceLocation(Trofers.MODID, String.format("trophies/%s", type.getRegistryName().getPath())));

            ResourceLocation soundLocation = new ResourceLocation(String.format("entity.%s.ambient", type.getRegistryName().getPath()));
            if (ForgeRegistries.SOUND_EVENTS.containsKey(soundLocation)) {
                soundEvents.put(type, ForgeRegistries.SOUND_EVENTS.getValue(soundLocation));
            } else {
                Trofers.LOGGER.error(String.format("no sound found for %s", type.getRegistryName()));
            }
        });

        potionEffects.put(EntityType.BAT, new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 40).save(new CompoundTag()));
        potionEffects.put(EntityType.CHICKEN, new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 5).save(new CompoundTag()));
        potionEffects.put(EntityType.DOLPHIN, new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 20 * 40).save(new CompoundTag()));
        potionEffects.put(EntityType.DROWNED, new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 40).save(new CompoundTag()));
        potionEffects.put(EntityType.ELDER_GUARDIAN, new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 40, 2).save(new CompoundTag()));
        potionEffects.put(EntityType.GLOW_SQUID, new MobEffectInstance(MobEffects.GLOWING, 20 * 10).save(new CompoundTag()));
        potionEffects.put(EntityType.IRON_GOLEM, new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60 * 2, 2).save(new CompoundTag()));
        potionEffects.put(EntityType.OCELOT, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 40, 3).save(new CompoundTag()));
        potionEffects.put(EntityType.PIGLIN_BRUTE, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60 * 2, 1).save(new CompoundTag()));
        potionEffects.put(EntityType.RABBIT, new MobEffectInstance(MobEffects.JUMP, 20 * 8, 3).save(new CompoundTag()));
        potionEffects.put(EntityType.SHULKER, new MobEffectInstance(MobEffects.LEVITATION, 20 * 5).save(new CompoundTag()));
        potionEffects.put(EntityType.STRIDER, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 40).save(new CompoundTag()));
        potionEffects.put(EntityType.TURTLE, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 2, 3).save(new CompoundTag()));
        potionEffects.put(EntityType.VILLAGER, new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 20 * 60 * 2, 4).save(new CompoundTag()));
        potionEffects.put(EntityType.WANDERING_TRADER, new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 40).save(new CompoundTag()));
        potionEffects.put(EntityType.WITCH, new MobEffectInstance(MobEffects.REGENERATION, 20 * 30).save(new CompoundTag()));

        lootTables.put(EntityType.CAT, new ResourceLocation("gameplay/cat_morning_gift"));
        lootTables.put(EntityType.PIGLIN, new ResourceLocation("gameplay/piglin_bartering"));

        cooldowns.put(EntityType.BAT, 0);
        cooldowns.put(EntityType.CHICKEN, 0);
        cooldowns.put(EntityType.DOLPHIN, 0);
        cooldowns.put(EntityType.DROWNED, 0);
        cooldowns.put(EntityType.ELDER_GUARDIAN, 0);
        cooldowns.put(EntityType.EVOKER, 20 * 60 * 60 * 4);
        cooldowns.put(EntityType.GLOW_SQUID, 0);
        cooldowns.put(EntityType.OCELOT, 0);
        cooldowns.put(EntityType.RABBIT, 0);
        cooldowns.put(EntityType.SHULKER, 0);
        cooldowns.put(EntityType.STRIDER, 0);
        cooldowns.put(EntityType.WANDERING_TRADER, 0);
        cooldowns.put(EntityType.WITHER_SKELETON, 20 * 60 * 20);

        soundEvents.put(EntityType.AXOLOTL, SoundEvents.AXOLOTL_IDLE_AIR);
        soundEvents.put(EntityType.BEE, SoundEvents.BEE_POLLINATE);
        soundEvents.put(EntityType.CAVE_SPIDER, SoundEvents.SPIDER_AMBIENT);
        soundEvents.put(EntityType.CREEPER, SoundEvents.CREEPER_PRIMED);
        soundEvents.put(EntityType.COD, SoundEvents.COD_FLOP);
        soundEvents.put(EntityType.IRON_GOLEM, SoundEvents.IRON_GOLEM_REPAIR);
        soundEvents.put(EntityType.MAGMA_CUBE, SoundEvents.MAGMA_CUBE_SQUISH);
        soundEvents.put(EntityType.MOOSHROOM, SoundEvents.COW_AMBIENT);
        soundEvents.put(EntityType.PUFFERFISH, SoundEvents.PUFFER_FISH_BLOW_UP);
        soundEvents.put(EntityType.SALMON, SoundEvents.SALMON_FLOP);
        soundEvents.put(EntityType.SLIME, SoundEvents.SLIME_SQUISH);
        soundEvents.put(EntityType.SNOW_GOLEM, SoundEvents.SNOW_PLACE);
        soundEvents.put(EntityType.TRADER_LLAMA, SoundEvents.LLAMA_AMBIENT);
        soundEvents.put(EntityType.TROPICAL_FISH, SoundEvents.TROPICAL_FISH_FLOP);
        soundEvents.put(EntityType.TURTLE, SoundEvents.TURTLE_AMBIENT_LAND);

        displayInfos.put(EntityType.GHAST, new DisplayInfo(0, 5, 0, 0.075F));
        displayInfos.put(EntityType.GLOW_SQUID, new DisplayInfo(0, 5, 0, 0.25F));
        displayInfos.put(EntityType.SQUID, new DisplayInfo(0, 5, 0, 0.25F));
        displayInfos.put(EntityType.PHANTOM, new DisplayInfo(0, 1, 0, 0.25F));

        displayInfos.put(EntityType.ELDER_GUARDIAN, new DisplayInfo(0.10625F));
        displayInfos.put(EntityType.RAVAGER, new DisplayInfo(0.175F));

        displayInfos.put(EntityType.FOX, new DisplayInfo(
                0.75F, 0, 0.5F, 0, -90, 0, 0.25F
        ));
        displayInfos.put(EntityType.COD, new DisplayInfo(
                -3/8F, 1, 0, 0, 0, -90, 0.25F
        ));
        displayInfos.put(EntityType.SALMON, new DisplayInfo(
                -3/8F, 1, 0, 0, 0, -90, 0.25F
        ));
        displayInfos.put(EntityType.TROPICAL_FISH, new DisplayInfo(
                -3/8F, 1, 0, 0, 0, -90, 0.25F
        ));

        entityData.get(EntityType.AXOLOTL).putInt("Variant", 0);
        entityData.get(EntityType.CAT).putInt("CatType", 0);
        entityData.get(EntityType.CAT).putBoolean("Sitting", true);
        entityData.get(EntityType.ENDERMAN).put("carriedBlockState", new CompoundTag());
        entityData.get(EntityType.ENDERMAN).getCompound("carriedBlockState").putString("Name", "minecraft:tnt");
        entityData.get(EntityType.FOX).putString("Type", "red");
        entityData.get(EntityType.FOX).putBoolean("Sleeping", true);
        entityData.get(EntityType.GLOW_SQUID).putInt("DarkTicksRemaining", 1);
        entityData.get(EntityType.HOGLIN).putBoolean("IsImmuneToZombification", true);
        entityData.get(EntityType.HORSE).putInt("Variant", 1 | 1 << 8);
        entityData.get(EntityType.LLAMA).putInt("Variant", 3);
        entityData.get(EntityType.MAGMA_CUBE).putInt("Size", 1);
        entityData.get(EntityType.PANDA).putString("MainGene", "playful");
        entityData.get(EntityType.PARROT).putInt("Variant", 0);
        entityData.get(EntityType.PIGLIN).putBoolean("IsImmuneToZombification", true);
        putHandItem(entityData.get(EntityType.PIGLIN_BRUTE), Items.GOLDEN_AXE);
        putHandItem(entityData.get(EntityType.PILLAGER), Items.CROSSBOW);
        entityData.get(EntityType.PUFFERFISH).putInt("PuffState", 2);
        entityData.get(EntityType.RABBIT).putInt("RabbitType", 0);
        putHandItem(entityData.get(EntityType.SKELETON), Items.BOW);
        entityData.get(EntityType.SLIME).putInt("Size", 1);
        putHandItem(entityData.get(EntityType.STRAY), Items.BOW);
        entityData.get(EntityType.TRADER_LLAMA).putInt("Variant", 2);
        entityData.get(EntityType.TROPICAL_FISH).putInt("Variant", 1 | 1 << 8 | 14 << 16 | 14 << 24);
        entityData.get(EntityType.VILLAGER).put("VillagerData", new CompoundTag());
        entityData.get(EntityType.VILLAGER).getCompound("VillagerData").putInt("level", 1);
        entityData.get(EntityType.VILLAGER).getCompound("VillagerData").putString("profession", "minecraft:weaponsmith");
        entityData.get(EntityType.VILLAGER).getCompound("VillagerData").putString("type", "minecraft:plains");
        entityData.get(EntityType.WOLF).putBoolean("Sitting", true);
        entityData.get(EntityType.WOLF).putUUID("Owner", Util.NIL_UUID);
        putHandItem(entityData.get(EntityType.WITHER_SKELETON), Items.STONE_SWORD);
        entityData.get(EntityType.ZOMBIE_VILLAGER).put("VillagerData", new CompoundTag());
        entityData.get(EntityType.ZOMBIE_VILLAGER).getCompound("VillagerData").putInt("level", 1);
        entityData.get(EntityType.ZOMBIE_VILLAGER).getCompound("VillagerData").putString("profession", "minecraft:weaponsmith");
        entityData.get(EntityType.ZOMBIE_VILLAGER).getCompound("VillagerData").putString("type", "minecraft:plains");
        putHandItem(entityData.get(EntityType.ZOMBIFIED_PIGLIN), Items.GOLDEN_SWORD);

        colors.keySet().forEach(type -> {
            // noinspection ConstantConditions
            addTrophy(new Trophy(
                    new ResourceLocation(Trofers.MODID, type.getRegistryName().getPath()),
                    createName(type, colors.get(type)),
                    Collections.emptyList(),
                    displayInfos.get(type),
                    Animation.STATIC,
                    ItemStack.EMPTY,
                    new EntityInfo(type, entityData.get(type), false),
                    new ColorInfo(0x606060, colors.get(type)),
                    new EffectInfo(
                            new EffectInfo.SoundInfo(soundEvents.get(type), 1, 1),
                            new EffectInfo.RewardInfo(
                                    potionEffects.get(type).isEmpty() ? lootTables.get(type) : null,
                                    potionEffects.get(type),
                                    cooldowns.get(type)
                            )
                    ),
                    false
            ));
        });
    }

    private void putHandItem(CompoundTag tag, Item item) {
        tag.put("HandItems", new ListTag());
        tag.getList("HandItems", Constants.NBT.TAG_COMPOUND).add(new ItemStack(item).save(new CompoundTag()));
        tag.getList("HandItems", Constants.NBT.TAG_COMPOUND).add(new CompoundTag());
    }

    private Component createName(EntityType<?> entityType, int color) {
        return ComponentUtils.mergeStyles(
                new TranslatableComponent(
                        "trophy.trofers.composed",
                        entityType.getDescription()
                ),
                Style.EMPTY.withColor(color)
        );
    }

    private void addTrophy(Trophy trophy) {
        trophies.put(trophy.id(), trophy);
    }

    @Override
    public void run(HashCache cache) {
        addTrophies();

        Path outputFolder = generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Trophy> consumer = (trophy) -> {
            if (!set.add(trophy.id())) {
                throw new IllegalStateException("Duplicate trophy " + trophy.id());
            } else {
                Path path = createPath(outputFolder, trophy);
                try {
                    DataProvider.save(GSON, cache, trophy.toJson(), path);
                } catch (IOException ioexception) {
                    Trofers.LOGGER.error("Couldn't save trophy {}", path, ioexception);
                }

            }
        };

        trophies.forEach((resourceLocation, trophy) -> consumer.accept(trophy));
    }

    private static Path createPath(Path path, Trophy trophy) {
        return path.resolve("data/" + trophy.id().getNamespace() + "/trofers/" + trophy.id().getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Trophies";
    }
}
