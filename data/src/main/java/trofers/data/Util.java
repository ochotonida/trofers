package trofers.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

public class Util {

    private static final String FABRIC = "fabric";
    private static final String FABRIC_LOAD_CONDITIONS = new ResourceLocation(FABRIC, "load_conditions").toString();
    private static final String FABRIC_ALL_MODS_LOADED = new ResourceLocation(FABRIC, "all_mods_loaded").toString();

    private static final String FORGE_CONDITIONS = "conditions";

    public static JsonObject addModLoadedConditions(JsonObject object, String... modIds) {
        addFabricModLoadedCondition(object, modIds);
        addForgeModLoadedCondition(object, modIds);
        return object;
    }

    private static void addFabricModLoadedCondition(JsonObject object, String... modIds) {
        JsonArray conditions = new JsonArray();
        object.add(FABRIC_LOAD_CONDITIONS, conditions);

        JsonObject modLoadedCondition = new JsonObject();
        modLoadedCondition.addProperty("condition", FABRIC_ALL_MODS_LOADED);
        conditions.add(modLoadedCondition);

        JsonArray mods = new JsonArray();
        modLoadedCondition.add("values", mods);

        for (String modId : modIds) {
            mods.add(modId);
        }
    }

    private static void addForgeModLoadedCondition(JsonObject object, String... modIds) {
        JsonArray conditions = new JsonArray();
        for (String modId : modIds) {
            conditions.add(CraftingHelper.serialize(new ModLoadedCondition(modId)));
        }
        object.add(FORGE_CONDITIONS, conditions);
    }
}
