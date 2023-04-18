package trofers.trophy.components;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import trofers.util.JsonHelper;

public record DisplayInfo(float xOffset, float yOffset, float zOffset, float xRotation, float yRotation,
                          float zRotation, float scale) {

    public static final DisplayInfo NONE = new DisplayInfo(1);

    public DisplayInfo(float scale) {
        this(0, 0, 0, scale);
    }

    public DisplayInfo(float xOffset, float yOffset, float zOffset, float scale) {
        this(xOffset, yOffset, zOffset, 0, 0, 0, scale);
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(xOffset);
        buffer.writeFloat(yOffset);
        buffer.writeFloat(zOffset);
        buffer.writeFloat(xRotation);
        buffer.writeFloat(yRotation);
        buffer.writeFloat(zRotation);
        buffer.writeFloat(scale);
    }

    public static DisplayInfo fromNetwork(FriendlyByteBuf buffer) {
        return new DisplayInfo(
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat()
        );
    }

    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        if (xOffset() != 0 || yOffset() != 0 || zOffset() != 0) {
            result.add("offset", serializeVector(
                    xOffset(),
                    yOffset(),
                    zOffset()
            ));
        }
        if (xRotation() != 0 || yRotation() != 0 || zRotation() != 0) {
            result.add("rotation", serializeVector(xRotation(), yRotation(), zRotation()));
        }
        if (scale() != 0) {
            result.addProperty("scale", scale());
        }
        return result;
    }

    private static JsonObject serializeVector(float x, float y, float z) {
        JsonObject result = new JsonObject();
        if (x != 0) result.addProperty("x", x);
        if (y != 0) result.addProperty("y", y);
        if (z != 0) result.addProperty("z", z);
        return result;
    }

    public static DisplayInfo fromJson(JsonObject object) {
        float xOffset, yOffset, zOffset;
        xOffset = yOffset = zOffset = 0;
        if (object.has("offset")) {
            JsonObject offset = GsonHelper.getAsJsonObject(object, "offset");
            xOffset = JsonHelper.readOptionalFloat(offset, "x", 0);
            yOffset = JsonHelper.readOptionalFloat(offset, "y", 0);
            zOffset = JsonHelper.readOptionalFloat(offset, "z", 0);
        }

        float xRotation, yRotation, zRotation;
        xRotation = yRotation = zRotation = 0;
        if (object.has("rotation")) {
            JsonObject rotation = GsonHelper.getAsJsonObject(object, "rotation");
            xRotation = JsonHelper.readOptionalFloat(rotation, "x", 0);
            yRotation = JsonHelper.readOptionalFloat(rotation, "y", 0);
            zRotation = JsonHelper.readOptionalFloat(rotation, "z", 0);
        }

        float scale = JsonHelper.readOptionalFloat(object, "scale", 1);

        return new DisplayInfo(xOffset, yOffset, zOffset, xRotation, yRotation, zRotation, scale);
    }
}
