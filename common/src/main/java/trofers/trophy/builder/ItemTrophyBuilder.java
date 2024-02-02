package trofers.trophy.builder;

import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import trofers.trophy.components.EntityInfo;
import trofers.util.JsonHelper;

import java.util.Optional;

@SuppressWarnings("unused")
public class ItemTrophyBuilder extends TrophyBuilder<ItemTrophyBuilder> {

    private final ItemStack item;

    public ItemTrophyBuilder(ItemStack item) {
        this.item = item;
    }

    @Override
    public ItemStack getDisplayItem() {
        return item;
    }

    @Override
    protected void displayItemToJson(JsonObject result) {
        result.add("item", JsonHelper.serializeItem(item));
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
