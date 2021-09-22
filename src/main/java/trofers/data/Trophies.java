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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.Constants;
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

        Map<EntityType<?>, Integer> entityColors = new HashMap<>();
        Map<EntityType<?>, DisplayInfo> displayInfos = new HashMap<>();
        Map<EntityType<?>, CompoundTag> entityData = new HashMap<>();

        entityColors.put(EntityType.AXOLOTL, 0xfbc1e2);
        entityColors.put(EntityType.BAT, 0x75653f);
        entityColors.put(EntityType.BEE, 0xebc542);
        entityColors.put(EntityType.BLAZE, 0xede746);
        entityColors.put(EntityType.CAT, 0x937155);
        entityColors.put(EntityType.CAVE_SPIDER, 0x147b6a);
        entityColors.put(EntityType.CHICKEN, 0xffffff);
        entityColors.put(EntityType.COD, 0xb6986c);
        entityColors.put(EntityType.COW, 0x6c5234);
        entityColors.put(EntityType.CREEPER, 0x48b23a);
        entityColors.put(EntityType.DOLPHIN, 0xb0c4d8);
        entityColors.put(EntityType.DONKEY, 0x817164);
        entityColors.put(EntityType.DROWNED, 0x56847e);
        entityColors.put(EntityType.ELDER_GUARDIAN, 0xbfbbaa);
        entityColors.put(EntityType.ENDERMAN, 0xa14fb6);
        entityColors.put(EntityType.ENDERMITE, 0x644b84);
        entityColors.put(EntityType.EVOKER, 0x959c9c);
        entityColors.put(EntityType.FOX, 0xe37c21);
        entityColors.put(EntityType.GHAST, 0xf0f0f0);
        entityColors.put(EntityType.GLOW_SQUID, 0x32a1a1);
        entityColors.put(EntityType.GOAT, 0xc2ab8e);
        entityColors.put(EntityType.GUARDIAN, 0x6a9087);
        entityColors.put(EntityType.HOGLIN, 0xd5957b);
        entityColors.put(EntityType.HORSE, 0x926633);
        entityColors.put(EntityType.HUSK, 0xc7ab6f);
        entityColors.put(EntityType.IRON_GOLEM, 0xcdb297);
        entityColors.put(EntityType.LLAMA, 0xe3e4d4);
        entityColors.put(EntityType.MAGMA_CUBE, 0xff4600);
        entityColors.put(EntityType.MOOSHROOM, 0xa41012);
        entityColors.put(EntityType.MULE, 0x89492c);
        entityColors.put(EntityType.OCELOT, 0xedb262);
        entityColors.put(EntityType.PANDA, 0xe4e4e4);
        entityColors.put(EntityType.PARROT, 0xe60000);
        entityColors.put(EntityType.PHANTOM, 0x5161a5);
        entityColors.put(EntityType.PIG, 0xf1a3a4);
        entityColors.put(EntityType.PIGLIN, 0xefb987);
        entityColors.put(EntityType.PIGLIN_BRUTE, 0xefb987);
        entityColors.put(EntityType.PILLAGER, 0x929c9c);
        entityColors.put(EntityType.POLAR_BEAR, 0xf2f2f4);
        entityColors.put(EntityType.PUFFERFISH, 0xe3970b);
        entityColors.put(EntityType.RABBIT, 0xa28b72);
        entityColors.put(EntityType.RAVAGER, 0x91acab);
        entityColors.put(EntityType.SALMON, 0xa83735);
        entityColors.put(EntityType.SHEEP, 0xffffff);
        entityColors.put(EntityType.SHULKER, 0x986a97);
        entityColors.put(EntityType.SILVERFISH, 0x778c99);
        entityColors.put(EntityType.SKELETON, 0xbdbdbd);
        entityColors.put(EntityType.SKELETON_HORSE, 0xd0d0d2);
        entityColors.put(EntityType.SLIME, 0x77c264);
        entityColors.put(EntityType.SNOW_GOLEM, 0xffffff);
        entityColors.put(EntityType.SPIDER, 0x7a6755);
        entityColors.put(EntityType.SQUID, 0x546d80);
        entityColors.put(EntityType.STRAY, 0x607576);
        entityColors.put(EntityType.STRIDER, 0xb44040);
        entityColors.put(EntityType.TRADER_LLAMA, 0x425f90);
        entityColors.put(EntityType.TROPICAL_FISH, 0xFF4040);
        entityColors.put(EntityType.TURTLE, 0x3ea240);
        entityColors.put(EntityType.VEX, 0x89a0b6);
        entityColors.put(EntityType.VILLAGER, 0xbf886d);
        entityColors.put(EntityType.VINDICATOR, 0x929c9c);
        entityColors.put(EntityType.WANDERING_TRADER, 0x425f90);
        entityColors.put(EntityType.WITCH, 0xa39482);
        entityColors.put(EntityType.WITHER_SKELETON, 0x626565);
        entityColors.put(EntityType.WOLF, 0xdcdadb);
        entityColors.put(EntityType.ZOGLIN, 0xe59796);
        entityColors.put(EntityType.ZOMBIE, 0x70955c);
        entityColors.put(EntityType.ZOMBIE_VILLAGER, 0x76a045);
        entityColors.put(EntityType.ZOMBIFIED_PIGLIN, 0xe59796);

        entityColors.keySet().forEach(type -> {
            displayInfos.put(type, new DisplayInfo(0, 0, 0, 0, 0, 0, 0.25F));
            entityData.put(type, new CompoundTag());
        });

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

        entityColors.keySet().forEach(type -> {
            // noinspection ConstantConditions
            addTrophy(new Trophy(
                    new ResourceLocation(Trofers.MODID, type.getRegistryName().getPath()),
                    createName(type, entityColors.get(type)),
                    displayInfos.get(type),
                    Animation.STATIC,
                    ItemStack.EMPTY,
                    new EntityInfo(type, entityData.get(type), false),
                    new ColorInfo(0x606060, entityColors.get(type)),
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
