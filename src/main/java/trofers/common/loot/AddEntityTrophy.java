package trofers.common.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
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

    public AddEntityTrophy(LootItemCondition[] conditions, Item trophyItem, Map<EntityType<?>, ResourceLocation> trophies) {
        super(conditions);
        this.trophyItem = trophyItem;
        this.trophies = trophies;
    }

    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (context.hasParam(LootContextParams.THIS_ENTITY)) {
            ResourceLocation trophyId = trophies.get(context.getParam(LootContextParams.THIS_ENTITY).getType());
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
        public AddEntityTrophy read(ResourceLocation name, JsonObject object, LootItemCondition[] conditions) {
            Item trophyItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "trophyBase")));
            HashMap<EntityType<?>, ResourceLocation> trophies = new HashMap<>();
            JsonArray array = GsonHelper.getAsJsonArray(object, "trophies");
            for (JsonElement element : array) {
                if (!element.isJsonObject()) {
                    throw new JsonParseException("Expected array entry to be an object, got " + element);
                }
                JsonObject entry = element.getAsJsonObject();

                ResourceLocation entityTypeId = new ResourceLocation(GsonHelper.getAsString(entry, "entity"));
                if (!ForgeRegistries.ENTITIES.containsKey(entityTypeId)) {
                    Trofers.LOGGER.debug("Skipping trophy loot modifier entry for missing entity type " + entityTypeId);
                    continue;
                }

                EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(entityTypeId);
                if (trophies.containsKey(entityType)) {
                    throw new JsonParseException("Duplicate entity type: " + entityTypeId);
                }

                trophies.put(entityType, new ResourceLocation(GsonHelper.getAsString(entry, "trophy")));
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
