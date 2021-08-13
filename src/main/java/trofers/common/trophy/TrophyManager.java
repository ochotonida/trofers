package trofers.common.trophy;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.*;

import java.util.Map;

public class TrophyManager extends SimpleJsonResourceReloadListener {

    private static Map<ResourceLocation, Trophy> trophies = ImmutableMap.of();

    public TrophyManager() {
        super((new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create(), "trofers");
    }

    public Trophy get(ResourceLocation id) {
        return trophies.getOrDefault(id, null);
    }

    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        ImmutableMap.Builder<ResourceLocation, Trophy> builder = ImmutableMap.builder();

        resources.forEach((id, element) -> {
            try {
                builder.put(id, Trophy.fromJson(element));
            } catch (JsonParseException exception) {
                LOGGER.error("Couldn't parse trophy {}", id, exception);
            }

        });
        builder.put(BuiltInLootTables.EMPTY, LootTable.EMPTY);
        ImmutableMap<ResourceLocation, Trophy> immutablemap = builder.build();
        trophies = immutablemap;
    }

    /**
     * Validate the given LootTable with the given ID using the given ValidationContext.
     */
    public static void validate(ValidationContext pValidator, ResourceLocation pId, LootTable pLootTable) {
        pLootTable.validate(pValidator.setParams(pLootTable.getParamSet()).enterTable("{" + pId + "}", pId));
    }
}
