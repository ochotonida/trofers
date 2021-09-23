package trofers.common.trophy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

import java.util.Objects;

public final class ColorInfo {

    public static final ColorInfo NONE = new ColorInfo(0xFFFFFF, 0xFFFFFF);
    private final int base;
    private final int accent;

    public ColorInfo(int base, int accent) {
        this.base = base;
        this.accent = accent;
    }

    public void toNetwork(PacketBuffer buffer) {
        buffer.writeInt(base);
        buffer.writeInt(accent);
    }

    public static ColorInfo fromNetwork(PacketBuffer buffer) {
        return new ColorInfo(buffer.readInt(), buffer.readInt());
    }

    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        if (base() != 0xFFFFFF) {
            result.add("base", serializeColor(base()));
        }
        if (accent() != base()) {
            result.add("accent", serializeColor(accent()));
        }
        return result;
    }

    private static JsonElement serializeColor(int color) {
        return new JsonPrimitive(String.format("#%06X", color));
    }

    public static ColorInfo fromJson(JsonObject object) {
        int base, accent;
        base = accent = 0xFFFFFF;
        if (object.has("base")) {
            base = accent = readColor(object.get("base"));
        }
        if (object.has("accent")) {
            accent = readColor(object.get("accent"));
        }

        return new ColorInfo(base, accent);
    }

    private static int readColor(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            int red = JSONUtils.getAsInt(object, "red");
            int green = JSONUtils.getAsInt(object, "green");
            int blue = JSONUtils.getAsInt(object, "blue");
            return red << 16 | green << 8 | blue;
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return parseColor(element.getAsString());
        } else {
            throw new JsonParseException(String.format("Expected color to be json object or string, got %s", element));
        }
    }

    private static int parseColor(String string) {
        if (string.startsWith("#")) {
            return Integer.parseInt(string.substring(1), 16);
        }
        throw new JsonParseException(String.format("Couldn't parse color string: %s", string));
    }

    public int base() {
        return base;
    }

    public int accent() {
        return accent;
    }
}
