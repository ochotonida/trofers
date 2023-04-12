package trofers.forge.common.trophy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record Animation(Type type, float speed) {

    public static final Animation STATIC = new Animation(Type.FIXED, 1);

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeByte(type.ordinal());
        buffer.writeFloat(speed);
    }

    public static Animation fromNetwork(FriendlyByteBuf buffer) {
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

    public static Animation fromJson(JsonObject object) {
        Type type = Type.fromJson(object.get("type"));
        float speed = Trophy.readOptionalFloat(object, "speed", 1);
        return new Animation(type, speed);
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
            String name = GsonHelper.convertToString(element, "animation");
            for (Type animation : Type.values()) {
                if (animation.name.equals(name)) {
                    return animation;
                }
            }
            throw new JsonParseException(String.format("Invalid trophy animation type: %s", name));
        }
    }
}
