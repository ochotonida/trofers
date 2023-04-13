package trofers.forge.platform;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import trofers.platform.PlatformHelper;

public class ForgePlatformHelper implements PlatformHelper {

    @Override
    public boolean matchesConditions(JsonObject object) {
        return CraftingHelper.processConditions(GsonHelper.getAsJsonArray(object, "conditions"), ICondition.IContext.EMPTY);
    }
}
