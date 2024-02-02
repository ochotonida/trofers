package trofers.trophy.components;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import trofers.util.JsonHelper;

import java.util.UUID;
import java.util.function.Function;

public class EntityInfo {

    private final EntityType<?> type;
    private final CompoundTag nbt;
    private final boolean isAnimated;

    @Nullable
    private Entity entity;

    public EntityInfo(EntityType<?> type, CompoundTag nbt, boolean isAnimated) {
        this.type = type;
        this.nbt = nbt;
        this.isAnimated = isAnimated;
    }

    @Nullable
    public EntityType<?> getType() {
        return type;
    }

    public CompoundTag getTag() {
        return nbt;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    @Nullable
    public Entity getOrCreateEntity(Level level) {
        if (entity == null || entity.level != level) {
            createEntity(level);
        }
        return entity;
    }

    private void createEntity(Level level) {
        if (type == null) {
            return;
        }

        CompoundTag entityTag = this.nbt.copy();
        entityTag.putString("id", Registry.ENTITY_TYPE.getKey(getType()).toString());
        if (!entityTag.hasUUID("UUID")) {
            entityTag.putUUID("UUID", new UUID(1L, 1L));
        }

        entity = EntityType.loadEntityRecursive(entityTag, level, Function.identity());
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(Registry.ENTITY_TYPE.getKey(getType()));
        buffer.writeNbt(nbt);
        buffer.writeBoolean(isAnimated);
    }

    public static EntityInfo fromNetwork(FriendlyByteBuf buffer) {
        EntityType<?> type = Registry.ENTITY_TYPE.get(buffer.readResourceLocation());
        return new EntityInfo(type, buffer.readNbt(), buffer.readBoolean());
    }

    public static EntityInfo fromJson(JsonObject object) {
        ResourceLocation typeID = new ResourceLocation(GsonHelper.getAsString(object, "type"));
        if (!Registry.ENTITY_TYPE.containsKey(typeID)) {
            throw new JsonParseException(String.format("Unknown entity type %s", typeID));
        }
        EntityType<?> type = Registry.ENTITY_TYPE.get(typeID);
        CompoundTag nbt = new CompoundTag();
        if (object.has("nbt")) {
            JsonElement nbtElement = object.get("nbt");
            nbt = JsonHelper.deserializeNBT(nbtElement);
        }
        boolean isAnimated = false;
        if (object.has("animated")) {
            isAnimated = GsonHelper.getAsBoolean(object, "animated");
        }
        return new EntityInfo(type, nbt, isAnimated);
    }
}
