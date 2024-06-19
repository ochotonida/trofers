package trofers.neoforge.datagen.providers.trophies;

import com.google.common.collect.ImmutableMap;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import trofers.Trofers;
import trofers.trophy.builder.TrophyBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameParameterValue")
public abstract class TrophyProvider {

    private final Map<ResourceLocation, TrophyBuilder<?>> trophies = new HashMap<>();

    public abstract void addTrophies();

    public List<LootTableProvider.SubProviderEntry> getLootTables() {
        return Collections.emptyList();
    }

    protected void addTrophy(ResourceLocation id, TrophyBuilder<?> builder) {
        if (trophies.containsKey(id)) {
            throw new IllegalStateException("Duplicate trophy: " + id.toString());
        }
        trophies.put(id, builder);
    }

    public Map<ResourceLocation, TrophyBuilder<?>> getTrophies() {
        return ImmutableMap.copyOf(trophies);
    }

    public String getModId() {
        return Trofers.MOD_ID;
    }

    protected ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(getModId(), path);
    }

    protected CompoundTag item(String itemName) {
        CompoundTag result = new CompoundTag();
        result.putString("id", id(itemName).toString());
        return result;
    }
}
