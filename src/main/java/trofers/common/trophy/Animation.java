package trofers.common.trophy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

import java.util.Objects;

public final class Animation {

    public static final Animation STATIC = new Animation(Type.FIXED, 1);
    private final Type type;
    private final float speed;

    public Animation(Type type, float speed) {
        this.type = type;
        this.speed = speed;
    }

    public void toNetwork(PacketBuffer buffer) {
        buffer.writeByte(type.ordinal());
        buffer.writeFloat(speed);
    }

    public static Animation fromNetwork(PacketBuffer buffer) {
        Type type = Type.values()[buffer.readByte()];
        float speed = buffer.readFloat();
        if (type == Type.FIXED) {
            return STATIC;
        }
        return new Animation(type, speed);
    }

    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        result.addProperty("type", type().name());
        if (speed() != 1) {
            result.addProperty("speed", speed());
        }
        return result;
    }

    protected static Animation fromJson(JsonObject object) {
        Type type = Type.fromJson(object.get("type"));
        float speed = Trophy.readOptionalFloat(object, "speed", 1);
        return new Animation(type, speed);
    }

    public Type type() {
        return type;
    }

    public float speed() {
        return speed;
    }

    public enum Type {
        FIXED("fixed"),
        SPINNING("spinning"),
        TUMBLING("tumbling");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public static Type fromJson(JsonElement element) {
            String name = JSONUtils.convertToString(element, "animation");
            for (Type animation : Type.values()) {
                if (animation.name.equals(name)) {
                    return animation;
                }
            }
            throw new JsonParseException(String.format("Invalid trophy animation type: %s", name));
        }
    }
}
