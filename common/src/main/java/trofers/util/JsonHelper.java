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

@SuppressWarnings("SameParameterValue")
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

    public static float readOptionalFloat(JsonObject object, String memberName, int defaultValue) {
        if (object.has(memberName)) {
            return GsonHelper.getAsFloat(object, memberName);
        }
        return defaultValue;
    }

    private static final String FABRIC = "fabric";
    private static final String FABRIC_LOAD_CONDITIONS = new ResourceLocation(FABRIC, "load_conditions").toString();
    private static final String FABRIC_ALL_MODS_LOADED = new ResourceLocation(FABRIC, "all_mods_loaded").toString();

    private static final String FORGE_CONDITIONS = "conditions";
    private static final String FORGE_MOD_LOADED = new ResourceLocation("forge", "mod_loaded").toString();

    private static JsonArray getOrCreateList(JsonObject object, String name) {
        if (object.has(name)) {
            return object.getAsJsonArray(name);
        }
        JsonArray conditions = new JsonArray();
        object.add(name, conditions);
        return conditions;
    }

    private static JsonObject createCondition(JsonObject object, String conditionsName, String conditionName, String conditionValue) {
        JsonArray conditions = getOrCreateList(object, conditionsName);
        JsonObject condition = new JsonObject();
        conditions.add(condition);
        condition.addProperty(conditionName, conditionValue);
        return condition;
    }

    private static JsonObject createFabricCondition(JsonObject object, String type) {
        return createCondition(object, FABRIC_LOAD_CONDITIONS, "condition", type);
    }

    private static JsonObject createForgeCondition(JsonObject object, String type) {
        return createCondition(object, FORGE_CONDITIONS, "type", type);
    }

    public static void addModLoadedConditions(JsonObject object, String... modIds) {
        if (modIds.length == 0) {
            return;
        }
        addFabricModLoadedCondition(object, modIds);
        addForgeModLoadedCondition(object, modIds);
    }

    private static void addFabricModLoadedCondition(JsonObject object, String... modIds) {
        JsonObject condition = createFabricCondition(object, FABRIC_ALL_MODS_LOADED);
        JsonArray mods = getOrCreateList(condition, "values");

        for (String modId : modIds) {
            mods.add(modId);
        }
    }

    private static void addForgeModLoadedCondition(JsonObject object, String... modIds) {
        for (String modId : modIds) {
            createForgeCondition(object, FORGE_MOD_LOADED).addProperty("modid", modId);
        }
    }
}
