package trofers.common.trophy;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;

public enum TrophyAnimation {
    FIXED("fixed"),
    SPINNING("spinning"),
    TUMBLING("tumbling");

    private final String name;

    TrophyAnimation(String name) {
        this.name = name;
    }

    public static TrophyAnimation fromJson(JsonElement element) {
        String name = GsonHelper.convertToString(element, "animation");
        for (TrophyAnimation animation : values()) {
            if (animation.name.equals(name.toLowerCase())) {
                return animation;
            }
        }
        throw new JsonParseException(String.format("Invalid trophy animation: %s", name));
    }
}
