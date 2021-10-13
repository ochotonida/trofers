package trofers.common.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

public abstract class JsonHelper {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static ItemStack deserializeItem(JsonObject object, String memberName) {
        return CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(object, memberName), true);
    }

    public static JsonObject serializeItem(ItemStack item) {
        JsonObject result = new JsonObject();
        // noinspection ConstantConditions
        result.addProperty("item", item.getItem().getRegistryName().toString());
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
