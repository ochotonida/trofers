package trofers.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.Constants;
import trofers.Trofers;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyAnimation;

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
        Map<EntityType<?>, Float> displayHeights = new HashMap<>();
        Map<EntityType<?>, Float> displayScales = new HashMap<>();
        Map<EntityType<?>, CompoundTag> entityData = new HashMap<>();

        colors.put(EntityType.AXOLOTL, 0xfbc1e2);
        colors.put(EntityType.BAT, 0x5b4b31);
        colors.put(EntityType.BEE, 0xebc542);
        colors.put(EntityType.BLAZE, 0xede746);
        colors.put(EntityType.CAT, 0x856549);
        colors.put(EntityType.CAVE_SPIDER, 0x002d30);
        colors.put(EntityType.CHICKEN, 0xffffff);
        colors.put(EntityType.COD, 0xb6986c);
        colors.put(EntityType.COW, 0x433626);
        colors.put(EntityType.CREEPER, 0x48b23a);
        colors.put(EntityType.DOLPHIN, 0xb0c4d8);
        colors.put(EntityType.DONKEY, 0x817164);
        colors.put(EntityType.DROWNED, 0x56847e);
        colors.put(EntityType.ELDER_GUARDIAN, 0xbfbbaa);
        colors.put(EntityType.ENDERMAN, 0xc442e6);
        colors.put(EntityType.ENDERMITE, 0x372647);
        colors.put(EntityType.EVOKER, 0x959c9c);
        colors.put(EntityType.FOX, 0xe37c21);
        colors.put(EntityType.GHAST, 0xf0f0f0);
        colors.put(EntityType.GLOW_SQUID, 0x32a1a1);
        colors.put(EntityType.GOAT, 0xc2ab8e);
        colors.put(EntityType.GUARDIAN, 0x6a9087);
        colors.put(EntityType.HOGLIN, 0xd5957b);
        colors.put(EntityType.HORSE, 0x926633);
        colors.put(EntityType.HUSK, 0x6b6251);
        colors.put(EntityType.IRON_GOLEM, 0xcdb297);
        colors.put(EntityType.LLAMA, 0xe3e4d4);
        colors.put(EntityType.MAGMA_CUBE, 0x320100);
        colors.put(EntityType.MOOSHROOM, 0xa41012);
        colors.put(EntityType.MULE, 0x502c1a);
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
        colors.put(EntityType.RABBIT, 0x8c775e);
        colors.put(EntityType.RAVAGER, 0x6f6d69);
        colors.put(EntityType.SALMON, 0xa83735);
        colors.put(EntityType.SHEEP, 0xffffff);
        colors.put(EntityType.SHULKER, 0x986a97);
        colors.put(EntityType.SILVERFISH, 0x727473);
        colors.put(EntityType.SKELETON, 0xbdbdbd);
        colors.put(EntityType.SKELETON_HORSE, 0xd0d0d2);
        colors.put(EntityType.SLIME, 0x77c264);
        colors.put(EntityType.SNOW_GOLEM, 0xffffff);
        colors.put(EntityType.SPIDER, 0x4e443c);
        colors.put(EntityType.SQUID, 0x546d80);
        colors.put(EntityType.STRAY, 0x586c6f);
        colors.put(EntityType.STRIDER, 0xb44040);
        colors.put(EntityType.TRADER_LLAMA, 0x425f90);
        colors.put(EntityType.TROPICAL_FISH, 0xFF4040);
        colors.put(EntityType.TURTLE, 0x3ea240);
        colors.put(EntityType.VEX, 0x89a0b6);
        colors.put(EntityType.VILLAGER, 0xbf886d);
        colors.put(EntityType.VINDICATOR, 0x929c9c);
        colors.put(EntityType.WANDERING_TRADER, 0x37547f);
        colors.put(EntityType.WITCH, 0xa39482);
        colors.put(EntityType.WITHER_SKELETON, 0x343434);
        colors.put(EntityType.WOLF, 0xdcdadb);
        colors.put(EntityType.ZOGLIN, 0x6b8f43);
        colors.put(EntityType.ZOMBIE, 0x70955c);
        colors.put(EntityType.ZOMBIE_VILLAGER, 0x76a045);
        colors.put(EntityType.ZOMBIFIED_PIGLIN, 0x6b8f43);

        colors.keySet().forEach(type -> {
            displayHeights.put(type, 0F);
            displayScales.put(type, 0.25F);
            entityData.put(type, new CompoundTag());
        });

        displayHeights.put(EntityType.GHAST, 5F);
        displayHeights.put(EntityType.GLOW_SQUID, 5F);
        displayHeights.put(EntityType.SQUID, 5F);

        displayScales.put(EntityType.GHAST, 0.075F);
        displayScales.put(EntityType.ELDER_GUARDIAN, 0.10625F);
        displayScales.put(EntityType.RAVAGER, 0.175F);
        displayScales.put(EntityType.SLIME, 0.5F);
        displayScales.put(EntityType.MAGMA_CUBE, 0.5F);

        entityData.get(EntityType.AXOLOTL).putInt("Variant", 0);
        entityData.get(EntityType.CAT).putInt("CatType", 0);
        entityData.get(EntityType.CAT).putBoolean("Sitting", true);
        entityData.get(EntityType.ENDERMAN).put("CarriedBlockState", new CompoundTag());
        entityData.get(EntityType.ENDERMAN).getCompound("CarriedBlockState").putString("Name", "minecraft:tnt");
        entityData.get(EntityType.FOX).putString("Type", "red");
        entityData.get(EntityType.GLOW_SQUID).putInt("DarkTicksRemaining", 1);
        entityData.get(EntityType.HOGLIN).putBoolean("IsImmuneToZombification", true);
        entityData.get(EntityType.HORSE).putInt("Variant", 1 | 1 << 8);
        entityData.get(EntityType.LLAMA).putInt("Variant", 3);
        entityData.get(EntityType.PANDA).putString("MainGene", "playful");
        entityData.get(EntityType.PARROT).putInt("Variant", 0);
        entityData.get(EntityType.PIGLIN).putBoolean("IsImmuneToZombification", true);
        putHandItem(entityData.get(EntityType.PIGLIN_BRUTE), Items.GOLDEN_AXE);
        putHandItem(entityData.get(EntityType.PILLAGER), Items.CROSSBOW);
        entityData.get(EntityType.PUFFERFISH).putInt("PuffState", 2);
        entityData.get(EntityType.RABBIT).putInt("RabbitType", 0);
        putHandItem(entityData.get(EntityType.SKELETON), Items.BOW);
        putHandItem(entityData.get(EntityType.STRAY), Items.BOW);
        entityData.get(EntityType.TRADER_LLAMA).putInt("Variant", 2);
        entityData.get(EntityType.TROPICAL_FISH).putInt("Variant", 1 | 1 << 8 | 4 << 16 | 14 << 24);
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

        // noinspection ConstantConditions
        colors.keySet().forEach(type -> addTrophy(new Trophy(
                new ResourceLocation(Trofers.MODID, type.getRegistryName().getPath()),
                ItemStack.EMPTY,
                type,
                entityData.get(type),
                false,
                TrophyAnimation.FIXED,
                createName(type),
                1,
                displayScales.get(type),
                displayHeights.get(type),
                0x606060,
                colors.get(type)
        )));
    }

    private void putHandItem(CompoundTag tag, Item item) {
        tag.put("HandItems", new ListTag());
        tag.getList("HandItems", Constants.NBT.TAG_COMPOUND).add(new ItemStack(item).save(new CompoundTag()));
        tag.getList("HandItems", Constants.NBT.TAG_COMPOUND).add(new CompoundTag());
    }

    private Component createName(EntityType<?> entityType) {
        return new TranslatableComponent("trophy.trofers.entity", entityType.getDescription());
    }

    private void addTrophy(Trophy trophy) {
        trophies.put(trophy.getId(), trophy);
    }

    private JsonObject serializeEntityTrophy(Trophy trophy) {
        JsonObject result = new JsonObject();
        JsonObject colors = new JsonObject();

        result.add("name", Component.Serializer.toJsonTree(trophy.getName()));

        result.add("colors", colors);

        colors.add("base", serializeColor(trophy.getColor()));
        colors.add("accent", serializeColor(trophy.getAccentColor()));

        JsonObject entity = new JsonObject();
        result.add("entity", entity);
        // noinspection ConstantConditions
        entity.addProperty("type", trophy.getEntityType().getRegistryName().toString());
        entity.addProperty("nbt", trophy.getEntityTag().toString());
        if (trophy.shouldAnimateEntity()) {
            entity.addProperty("animateEntity", trophy.shouldAnimateEntity());
        }

        if (trophy.getDisplayScale() != 1) {
            result.addProperty("displayScale", trophy.getDisplayScale());
        }
        if (trophy.getDisplayHeight() != 0) {
            result.addProperty("displayHeight", trophy.getDisplayHeight());
        }

        return result;
    }

    private JsonObject serializeColor(int color) {
        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;
        JsonObject result = new JsonObject();
        result.addProperty("red", red);
        result.addProperty("green", green);
        result.addProperty("blue", blue);
        return result;
    }


    @Override
    public void run(HashCache cache) {
        addTrophies();

        Path outputFolder = generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Trophy> consumer = (trophy) -> {
            if (!set.add(trophy.getId())) {
                throw new IllegalStateException("Duplicate trophy " + trophy.getId());
            } else {
                Path path = createPath(outputFolder, trophy);
                try {
                    DataProvider.save(GSON, cache, serializeEntityTrophy(trophy), path);
                } catch (IOException ioexception) {
                    Trofers.LOGGER.error("Couldn't save trophy {}", path, ioexception);
                }

            }
        };

        trophies.forEach((resourceLocation, trophy) -> consumer.accept(trophy));
    }

    private static Path createPath(Path path, Trophy trophy) {
        return path.resolve("data/" + trophy.getId().getNamespace() + "/trofers/" + trophy.getId().getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Trophies";
    }
}
