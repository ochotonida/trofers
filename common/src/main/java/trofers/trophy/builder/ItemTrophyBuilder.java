package trofers.trophy.builder;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import trofers.trophy.components.EntityInfo;

import java.util.Optional;

public class ItemTrophyBuilder extends TrophyBuilder<ItemTrophyBuilder> {

    private ResourceLocation itemId;
    private int count;
    private CompoundTag tag;

    public ItemTrophyBuilder setItem(ItemStack item) {
        setItem(item.getItem());
        setCount(item.getCount());
        setTag(item.getTag());
        return this;
    }

    public ItemTrophyBuilder setItem(ItemLike item) {
        itemId = BuiltInRegistries.ITEM.getKey(item.asItem());
        return this;
    }

    public ItemTrophyBuilder setItem(ResourceLocation itemId) {
        this.itemId = itemId;
        return this;
    }

    public ItemTrophyBuilder setCount(int count) {
        this.count = count;
        return this;
    }

    public ItemTrophyBuilder setTag(CompoundTag tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack result = new ItemStack(BuiltInRegistries.ITEM.get(itemId));
        result.setTag(tag);
        result.setCount(count);
        return result;
    }

    @Override
    protected void displayItemToJson(JsonObject result) {
        JsonObject object = new JsonObject();
        object.addProperty("item", itemId.toString());
        if (count != 1) {
            object.addProperty("count", count);
        }
        if (tag != null && !tag.isEmpty()) {
            object.addProperty("nbt", tag.toString());
        }
        result.add("item", object);
    }

    @Override
    protected Optional<EntityInfo> getEntityInfo() {
        return Optional.empty();
    }

    @Override
    protected void entityInfoToJson(JsonObject result) {
        // no-op
    }
}
