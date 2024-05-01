package trofers.trophy.builder;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import trofers.trophy.components.EntityInfo;

import java.util.Optional;

public class ItemTrophyBuilder extends TrophyBuilder<ItemTrophyBuilder> {

    private ResourceLocation item;
    private int count = 1;
    private DataComponentMap components = DataComponentMap.EMPTY;

    public ItemTrophyBuilder setItem(ItemStack item) {
        setItem(item.getItem());
        setCount(item.getCount());
        setComponents(item.getComponents());
        return this;
    }

    public ItemTrophyBuilder setItem(ItemLike item) {
        this.item = BuiltInRegistries.ITEM.getKey(item.asItem());
        return this;
    }

    public ItemTrophyBuilder setItem(ResourceLocation itemId) {
        this.item = itemId;
        return this;
    }

    public ItemTrophyBuilder setCount(int count) {
        this.count = count;
        return this;
    }

    public ItemTrophyBuilder setComponents(DataComponentMap components) {
        this.components = components;
        return this;
    }

    @Override
    public ItemStack getDisplayItem() {
        ItemStack result = new ItemStack(BuiltInRegistries.ITEM.get(item));
        result.applyComponents(components);
        result.setCount(count);
        return result;
    }

    @Override
    protected Optional<EntityInfo> getEntityInfo() {
        return Optional.empty();
    }
}
