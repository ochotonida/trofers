package trofers.common.loot;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.common.trophy.TrophyManager;

import java.util.List;

public class AddTrophy extends LootModifier {

    private final Item trophyItem;
    private final ResourceLocation trophyId;

    public AddTrophy(LootItemCondition[] conditions, Item trophyItem, ResourceLocation trophyId) {
        super(conditions);
        this.trophyItem = trophyItem;
        this.trophyId = trophyId;
    }

    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (TrophyManager.get(trophyId) == null) {
            Trofers.LOGGER.error("Failed to find trophy with invalid id '{}'", trophyId);
        } else {
            ItemStack stack = new ItemStack(trophyItem);
            stack.getOrCreateTagElement("BlockEntityTag").putString("Trophy", trophyId.toString());
            generatedLoot.add(stack);
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<AddTrophy> {

        @Override
        public AddTrophy read(ResourceLocation name, JsonObject object, LootItemCondition[] conditions) {
            Item trophyItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "trophyBase")));
            ResourceLocation trophyId = new ResourceLocation(GsonHelper.getAsString(object, "trophy"));
            return new AddTrophy(conditions, trophyItem, trophyId);
        }

        @Override
        public JsonObject write(AddTrophy lootModifier) {
            JsonObject object = makeConditions(lootModifier.conditions);
            // noinspection ConstantConditions
            object.addProperty("trophyBase", lootModifier.trophyItem.getRegistryName().toString());
            object.addProperty("trophy", lootModifier.trophyId.toString());
            return object;
        }
    }
}
