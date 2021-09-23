package trofers.common.trophy;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public final class DisplayInfo {

    public static final DisplayInfo NONE = new DisplayInfo(0, 0, 0, 0, 0, 0, 1);
    private final float xOffset;
    private final float yOffset;
    private final float zOffset;
    private final float xRotation;
    private final float yRotation;
    private final float zRotation;
    private final float scale;

    public DisplayInfo(float xOffset, float yOffset, float zOffset, float xRotation, float yRotation,
                       float zRotation, float scale) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.xRotation = xRotation;
        this.yRotation = yRotation;
        this.zRotation = zRotation;
        this.scale = scale;
    }

    public DisplayInfo(float scale) {
        this(0, 0, 0, scale);
    }

    public DisplayInfo(float xOffset, float yOffset, float zOffset, float scale) {
        this(xOffset, yOffset, zOffset, 0, 0, 0, scale);
    }

    public void toNetwork(PacketBuffer buffer) {
        buffer.writeFloat(xOffset);
        buffer.writeFloat(yOffset);
        buffer.writeFloat(zOffset);
        buffer.writeFloat(xRotation);
        buffer.writeFloat(yRotation);
        buffer.writeFloat(zRotation);
        buffer.writeFloat(scale);
    }

    public static DisplayInfo fromNetwork(PacketBuffer buffer) {
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

    protected static DisplayInfo fromJson(JsonObject object) {
        float xOffset, yOffset, zOffset;
        xOffset = yOffset = zOffset = 0;
        if (object.has("offset")) {
            JsonObject offset = JSONUtils.getAsJsonObject(object, "offset");
            xOffset = Trophy.readOptionalFloat(offset, "x", 0);
            yOffset = Trophy.readOptionalFloat(offset, "y", 0);
            zOffset = Trophy.readOptionalFloat(offset, "z", 0);
        }

        float xRotation, yRotation, zRotation;
        xRotation = yRotation = zRotation = 0;
        if (object.has("rotation")) {
            JsonObject rotation = JSONUtils.getAsJsonObject(object, "rotation");
            xRotation = Trophy.readOptionalFloat(rotation, "x", 0);
            yRotation = Trophy.readOptionalFloat(rotation, "y", 0);
            zRotation = Trophy.readOptionalFloat(rotation, "z", 0);
        }

        float scale = Trophy.readOptionalFloat(object, "scale", 1);

        return new DisplayInfo(xOffset, yOffset, zOffset, xRotation, yRotation, zRotation, scale);
    }

    public float xOffset() {
        return xOffset;
    }

    public float yOffset() {
        return yOffset;
    }

    public float zOffset() {
        return zOffset;
    }

    public float xRotation() {
        return xRotation;
    }

    public float yRotation() {
        return yRotation;
    }

    public float zRotation() {
        return zRotation;
    }

    public float scale() {
        return scale;
    }
}
