package trofers.data.loottables;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;

public class VanillaLootTables extends LootTableProvider {

    @Override
    protected void addLootTables() {
        add(EntityType.AXOLOTL, Items.AMETHYST_SHARD);
        add(EntityType.BEE, entry(Items.HONEY_BOTTLE), entry(Items.HONEYCOMB, 2));
        add(EntityType.BLAZE, 1, 3, entry(Items.BLAZE_ROD), entry(Items.BLAZE_POWDER, 2));
        add(EntityType.CAVE_SPIDER, Items.COBWEB);
        add(EntityType.COD, 1, 3, Items.COD);
        add(EntityType.COW, 1, 3, Items.BEEF);
        add(EntityType.CREEPER, 1, 3, entry(Items.GUNPOWDER, 3), entry(Items.TNT));
        add(EntityType.DONKEY, Items.GOLDEN_APPLE);
        add(EntityType.ENDERMAN, Items.ENDER_PEARL);
        add(EntityType.ENDERMITE, 3, 12, Items.END_STONE);
        add(EntityType.EVOKER, Items.TOTEM_OF_UNDYING);
        add(EntityType.FOX, 1, 3, entry(Items.SWEET_BERRIES, 2), entry(Items.GLOW_BERRIES));
        add(EntityType.GHAST, Items.GHAST_TEAR);
        add(EntityType.GOAT, Items.POWDER_SNOW_BUCKET);
        add(EntityType.GUARDIAN, 1, 6, entry(Items.PRISMARINE_SHARD, 2), entry(Items.PRISMARINE_CRYSTALS));
        add(EntityType.HOGLIN, Items.LEATHER);
        add(EntityType.HORSE, Items.HAY_BLOCK);
        add(EntityType.HUSK, 1, 3, Items.ROTTEN_FLESH, Items.IRON_INGOT);
        add(EntityType.LLAMA, 1, 6, Items.BLACK_CARPET, Items.CYAN_CARPET, Items.BLUE_CARPET, Items.BROWN_CARPET, Items.GRAY_CARPET, Items.GREEN_CARPET, Items.LIGHT_BLUE_CARPET, Items.LIGHT_GRAY_CARPET, Items.LIME_CARPET, Items.MAGENTA_CARPET, Items.ORANGE_CARPET, Items.PINK_CARPET, Items.PURPLE_CARPET, Items.RED_CARPET, Items.WHITE_CARPET, Items.YELLOW_CARPET);
        add(EntityType.MAGMA_CUBE, 1, 3, Items.MAGMA_CREAM);
        add(EntityType.MOOSHROOM, 1, 3, Items.BROWN_MUSHROOM, Items.RED_MUSHROOM);
        add(EntityType.MULE, Items.GOLDEN_CARROT);
        add(EntityType.PANDA, Items.CAKE);
        add(EntityType.PARROT, Items.COOKIE);
        add(EntityType.PHANTOM, Items.PHANTOM_MEMBRANE);
        add(EntityType.PIG, 1, 3, Items.PORKCHOP);
        add(EntityType.PILLAGER, 3, 12, Items.ARROW);
        add(EntityType.POLAR_BEAR, 1, 4, Items.SALMON, Items.COD);
        add(EntityType.PUFFERFISH, Items.PUFFERFISH);
        add(EntityType.RAVAGER, Items.SADDLE);
        add(EntityType.SALMON, 1, 3, Items.SALMON);
        add(EntityType.SHEEP, 1, 6, Items.BLACK_WOOL, Items.CYAN_WOOL, Items.BLUE_WOOL, Items.BROWN_WOOL, Items.GRAY_WOOL, Items.GREEN_WOOL, Items.LIGHT_BLUE_WOOL, Items.LIGHT_GRAY_WOOL, Items.LIME_WOOL, Items.MAGENTA_WOOL, Items.ORANGE_WOOL, Items.PINK_WOOL, Items.PURPLE_WOOL, Items.RED_WOOL, Items.WHITE_WOOL, Items.YELLOW_WOOL);
        add(EntityType.SILVERFISH, 1, 3, Items.ENDER_EYE);
        add(EntityType.SKELETON, 1, 4, Items.BONE);
        add(EntityType.SKELETON_HORSE, 1, 4, Items.BONE_BLOCK);
        add(EntityType.SLIME, 1, 3, Items.SLIME_BALL);
        add(EntityType.SNOW_GOLEM, 2, 6, entry(Items.SNOW_BLOCK), entry(Items.SNOWBALL, 2));
        add(EntityType.SPIDER, 1, 3, Items.STRING);
        add(EntityType.SQUID, 1, 3, Items.INK_SAC);
        // noinspection deprecation
        add(EntityType.STRAY, 2, 8, entry(Items.TIPPED_ARROW).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (tag) -> tag.putString("Potion", "minecraft:slowness")))));
        add(EntityType.TRADER_LLAMA, Items.LEAD);
        add(EntityType.TROPICAL_FISH, Items.TROPICAL_FISH);
        add(EntityType.VEX, Items.EMERALD);
        add(EntityType.VINDICATOR, Items.IRON_AXE);
        add(EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL);
        add(EntityType.WOLF, Items.WHITE_WOOL);
        add(EntityType.ZOGLIN, 1, 3, Items.ROTTEN_FLESH);
        add(EntityType.ZOMBIE, 1, 3, Items.ROTTEN_FLESH);
        add(EntityType.ZOMBIE_VILLAGER, 1, 3, entry(Items.EMERALD), entry(Items.ROTTEN_FLESH, 2));
        add(EntityType.ZOMBIFIED_PIGLIN, 1, 3, entry(Items.GOLD_NUGGET, 2), entry(Items.GOLD_INGOT));
    }
}
