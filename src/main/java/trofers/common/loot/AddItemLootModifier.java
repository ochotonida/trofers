package trofers.common.loot;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import trofers.common.util.JsonHelper;

import java.util.List;

public class AddItemLootModifier extends LootModifier {

    private final ItemStack item;

    public AddItemLootModifier(ILootCondition[] conditions, ItemStack item) {
        super(conditions);
        this.item = item;
    }

    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(item.copy());
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<AddItemLootModifier> {

        @Override
        public AddItemLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditions) {
            ItemStack item = JsonHelper.deserializeItem(object, "item");
            return new AddItemLootModifier(conditions, item);
        }

        @Override
        public JsonObject write(AddItemLootModifier lootModifier) {
            JsonObject object = makeConditions(lootModifier.conditions);
            object.add("item", JsonHelper.serializeItem(lootModifier.item));
            return object;
        }
    }
}
