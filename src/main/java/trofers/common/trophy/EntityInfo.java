package trofers.common.trophy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class EntityInfo {

    private final EntityType<?> type;
    private final CompoundNBT nbt;
    private final boolean isAnimated;

    @Nullable
    private Entity entity;

    public EntityInfo(EntityType<?> type, CompoundNBT nbt, boolean isAnimated) {
        this.type = type;
        this.nbt = nbt;
        this.isAnimated = isAnimated;
    }

    @Nullable
    public EntityType<?> getType() {
        return type;
    }

    public CompoundNBT getTag() {
        return nbt;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    @Nullable
    public Entity getOrCreateEntity(World level) {
        if (entity == null || entity.level != level) {
            createEntity(level);
        }
        return entity;
    }

    private void createEntity(World level) {
        if (type == null) {
            return;
        }

        entity = type.create(level);
        if (entity != null) {
            entity.setId(0);
            entity.load(nbt);
            if (!nbt.hasUUID("UUID")) {
                entity.setUUID(Util.NIL_UUID);
            }
        }
    }

    public void toNetwork(PacketBuffer buffer) {
        // noinspection ConstantConditions
        buffer.writeResourceLocation(type.getRegistryName());
        buffer.writeNbt(nbt);
        buffer.writeBoolean(isAnimated);
    }

    public static EntityInfo fromNetwork(PacketBuffer buffer) {
        EntityType<?> type = ForgeRegistries.ENTITIES.getValue(buffer.readResourceLocation());
        return new EntityInfo(type, buffer.readNbt(), buffer.readBoolean());
    }

    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        // noinspection ConstantConditions
        result.addProperty("type", getType().getRegistryName().toString());
        if (!getTag().isEmpty()) {
            result.addProperty("nbt", getTag().toString());
        }
        if (isAnimated()) {
            result.addProperty("animated", isAnimated());
        }
        return result;
    }

    public static EntityInfo fromJson(JsonObject object) {
        ResourceLocation typeID = new ResourceLocation(JSONUtils.getAsString(object, "type"));
        if (!ForgeRegistries.ENTITIES.containsKey(typeID)) {
            throw new JsonParseException(String.format("Unknown entity type %s", typeID));
        }
        EntityType<?> type = ForgeRegistries.ENTITIES.getValue(typeID);
        CompoundNBT nbt = new CompoundNBT();
        if (object.has("nbt")) {
            JsonElement nbtElement = object.get("nbt");
            nbt = Trophy.readNBT(nbtElement);
        }
        boolean isAnimated = false;
        if (object.has("animated")) {
            isAnimated = JSONUtils.getAsBoolean(object, "animated");
        }
        return new EntityInfo(type, nbt, isAnimated);
    }
}
