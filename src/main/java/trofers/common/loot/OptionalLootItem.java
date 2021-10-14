package trofers.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootPoolEntryType;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.common.init.ModLootPoolEntries;
import trofers.common.util.JsonHelper;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class OptionalLootItem extends StandaloneLootEntry {

    private final ResourceLocation item;
    private final List<ICondition> loadingConditions;

    OptionalLootItem(ResourceLocation item, List<ICondition> loadingConditions, int weight, int quality, ILootCondition[] conditions, ILootFunction[] functions) {
        super(weight, quality, conditions, functions);
        this.item = item;
        this.loadingConditions = loadingConditions;
    }

    public LootPoolEntryType getType() {
        return ModLootPoolEntries.OPTIONAL_ITEM;
    }

    public void createItemStack(Consumer<ItemStack> pStackConsumer, LootContext pLootContext) {
        pStackConsumer.accept(new ItemStack(ForgeRegistries.ITEMS.getValue(item)));
    }

    public static StandaloneLootEntry.Builder<?> optionalLootItem(IItemProvider item, ICondition... loadingConditions) {
        return simpleBuilder((weight, quality, conditions, functions) -> new OptionalLootItem(item.asItem().getRegistryName(), Arrays.asList(loadingConditions), weight, quality, conditions, functions));
    }

    public static class Serializer extends StandaloneLootEntry.Serializer<OptionalLootItem> {

        public void serializeCustom(JsonObject object, OptionalLootItem lootItem, JsonSerializationContext context) {
            super.serializeCustom(object, lootItem, context);
            object.add("when", JsonHelper.serializeConditions(lootItem.loadingConditions));
            object.addProperty("name", lootItem.item.toString());
        }

        protected OptionalLootItem deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, ILootCondition[] conditions, ILootFunction[] functions) {
            ResourceLocation item = new ResourceLocation(JSONUtils.getAsString(object, "name"));
            List<ICondition> loadingConditions = JsonHelper.deserializeConditions(object, "when");
            if (loadingConditions.stream().allMatch(ICondition::test) && !ForgeRegistries.ITEMS.containsKey(item)) {
                throw new JsonParseException("Could not find unknown item " + item);
            }
            return new OptionalLootItem(item, loadingConditions, weight, quality, conditions, functions);
        }
    }
}
