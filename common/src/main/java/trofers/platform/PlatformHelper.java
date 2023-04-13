package trofers.platform;

import com.google.gson.JsonObject;

public interface PlatformHelper {

    boolean matchesConditions(JsonObject object);
}
