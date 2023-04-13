package trofers.fabric.platform;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import trofers.platform.PlatformHelper;

public class FabricPlatformHelper implements PlatformHelper {

    @Override
    public boolean matchesConditions(JsonObject object) {
        return ResourceConditions.objectMatchesConditions(object);
    }
}
