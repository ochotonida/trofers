package trofers.common.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.CraftingHelper;

public abstract class JsonHelper {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static ItemStack deserializeItem(JsonObject object, String memberName) {
        return CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(object, memberName), true);
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

    public static CompoundNBT deserializeNBT(JsonElement element) {
        try {
            if (element.isJsonObject())
                return JsonToNBT.parseTag(GSON.toJson(element));
            else {
                return JsonToNBT.parseTag(JSONUtils.convertToString(element, "nbt"));
            }
        } catch (CommandSyntaxException exception) {
            throw new JsonSyntaxException(String.format("Invalid NBT Entry: %s", exception));
        }
    }
}
