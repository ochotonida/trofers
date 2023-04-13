package trofers.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public abstract class JsonHelper {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static ItemStack deserializeItem(JsonObject object, String memberName) {
        return getItemStack(GsonHelper.getAsJsonObject(object, memberName), true);
    }

    public static ItemStack getItemStack(JsonObject json, boolean readNBT) {
        String itemName = GsonHelper.getAsString(json, "item");
        Item item = getItem(itemName);
        if (readNBT && json.has("nbt")) {
            CompoundTag nbt = parseNBT(json.get("nbt"));
            CompoundTag itemTag = new CompoundTag();
            if (nbt.contains("ForgeCaps")) {
                // noinspection ConstantConditions
                itemTag.put("ForgeCaps", nbt.get("ForgeCaps"));
                nbt.remove("ForgeCaps");
            }

            itemTag.put("tag", nbt);
            itemTag.putString("id", itemName);
            itemTag.putInt("Count", GsonHelper.getAsInt(json, "count", 1));

            return ItemStack.of(itemTag);
        }

        return new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));
    }

    public static CompoundTag parseNBT(JsonElement element) {
        try {
            if (element.isJsonObject()) {
                return TagParser.parseTag(GSON.toJson(element));
            } else {
                return TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
            }
        } catch (CommandSyntaxException exception) {
            throw new JsonSyntaxException("Invalid NBT Entry: " + exception);
        }
    }

    public static Item getItem(String id) {
        ResourceLocation key = new ResourceLocation(id);
        if (!BuiltInRegistries.ITEM.containsKey(key)) {
            throw new JsonSyntaxException("Unknown item '" + id + "'");
        }

        Item item = BuiltInRegistries.ITEM.get(key);
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Invalid item: " + id);
        }
        return item;
    }

    public static JsonObject serializeItem(ItemStack item) {
        JsonObject result = new JsonObject();
        result.addProperty("item", BuiltInRegistries.ITEM.getKey(item.getItem()).toString());
        if (item.getCount() != 1) {
            result.addProperty("count", item.getCount());
        }
        if (item.hasTag()) {
            // noinspection ConstantConditions
            result.addProperty("nbt", item.getTag().toString());
        }
        return result;
    }

    public static CompoundTag deserializeNBT(JsonElement element) {
        try {
            if (element.isJsonObject())
                return TagParser.parseTag(GSON.toJson(element));
            else {
                return TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
            }
        } catch (CommandSyntaxException exception) {
            throw new JsonSyntaxException(String.format("Invalid NBT Entry: %s", exception));
        }
    }
}
