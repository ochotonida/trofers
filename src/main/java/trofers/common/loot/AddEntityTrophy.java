package trofers.common.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyManager;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEntityTrophy extends LootModifier {

    private final Item trophyItem;
    private final Map<EntityType<?>, ResourceLocation> trophies;

    public AddEntityTrophy(ILootCondition[] conditions, Item trophyItem, Map<EntityType<?>, ResourceLocation> trophies) {
        super(conditions);
        this.trophyItem = trophyItem;
        this.trophies = trophies;
    }

    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (context.hasParam(LootParameters.THIS_ENTITY)) {
            // noinspection ConstantConditions
            ResourceLocation trophyId = trophies.get(context.getParamOrNull(LootParameters.THIS_ENTITY).getType());
            if (trophyId != null) {
                Trophy trophy = TrophyManager.get(trophyId);
                if (trophy == null) {
                    Trofers.LOGGER.error("Failed to find trophy with invalid id '{}'", trophyId);
                } else {
                    ItemStack stack = new ItemStack(trophyItem);
                    stack.getOrCreateTagElement("BlockEntityTag").putString("Trophy", trophyId.toString());
                    generatedLoot.add(stack);
                }
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<AddEntityTrophy> {

        @Override
        public AddEntityTrophy read(ResourceLocation name, JsonObject object, ILootCondition[] conditions) {
            Item trophyItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getAsString(object, "trophyBase")));
            HashMap<EntityType<?>, ResourceLocation> trophies = new HashMap<>();
            JsonArray array = JSONUtils.getAsJsonArray(object, "trophies");
            for (JsonElement element : array) {
                if (!element.isJsonObject()) {
                    throw new JsonParseException("Expected array entry to be an object, got " + element);
                }
                JsonObject entry = element.getAsJsonObject();

                ResourceLocation entityTypeId = new ResourceLocation(JSONUtils.getAsString(entry, "entity"));
                if (!ForgeRegistries.ENTITIES.containsKey(entityTypeId)) {
                    Trofers.LOGGER.debug("Skipping trophy loot modifier entry for missing entity type " + entityTypeId);
                    continue;
                }

                EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(entityTypeId);
                if (trophies.containsKey(entityType)) {
                    throw new JsonParseException("Duplicate entity type: " + entityTypeId);
                }

                trophies.put(entityType, new ResourceLocation(JSONUtils.getAsString(entry, "trophy")));
            }
            return new AddEntityTrophy(conditions, trophyItem, trophies);
        }

        @Override
        public JsonObject write(AddEntityTrophy lootModifier) {
            JsonObject object = makeConditions(lootModifier.conditions);
            // noinspection ConstantConditions
            object.addProperty("trophyBase", lootModifier.trophyItem.getRegistryName().toString());
            JsonArray array = new JsonArray();
            lootModifier.trophies.entrySet().stream()
                    .sorted(Comparator.comparing(entry -> entry.getValue().toString()))
                    .forEach(entry -> {
                        JsonObject arrayEntry = new JsonObject();
                        // noinspection ConstantConditions
                        arrayEntry.addProperty("entity", entry.getKey().getRegistryName().toString());
                        arrayEntry.addProperty("trophy", entry.getValue().toString());
                        array.add(arrayEntry);
            });
            object.add("trophies", array);
            return object;
        }
    }
}
