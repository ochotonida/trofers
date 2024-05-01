package trofers.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("SameParameterValue")
public abstract class ConditionsHelper {

    private static final String FABRIC = "fabric";
    private static final String FABRIC_LOAD_CONDITIONS = new ResourceLocation(FABRIC, "load_conditions").toString();
    private static final String FABRIC_ALL_MODS_LOADED = new ResourceLocation(FABRIC, "all_mods_loaded").toString();

    private static final String NEOFORGE = "neoforge";
    private static final String NEOFORGE_CONDITIONS = new ResourceLocation(NEOFORGE, "conditions").toString();
    private static final String NEOFORGE_MOD_LOADED = new ResourceLocation(NEOFORGE, "mod_loaded").toString();

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
        return createCondition(object, NEOFORGE_CONDITIONS, "type", type);
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
            createForgeCondition(object, NEOFORGE_MOD_LOADED).addProperty("modid", modId);
        }
    }
}
