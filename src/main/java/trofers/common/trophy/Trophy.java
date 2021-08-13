package trofers.common.trophy;

import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class Trophy {

    private final ItemStack item;
    private final Entity entity;

    private Trophy(ItemStack item, EntityType<?> entityType, Entity entity) {
        this.item = item;
        this.entity = entity;
    }

    private static Trophy create(ItemStack item) {
        return new Trophy(item, null);
    }

    private static Trophy create(EntityType<?> entityType) {
        if (Minecraft.getInstance().level != null) {
            return
        }
        entityType.create()
    }

    public static Trophy fromJson(JsonElement element) {
        UUID.fromString("")
        return new Trophy();
    }

    // TODO read/write entities
}
