package trofers.data.providers.trophies;

import com.google.common.collect.ImmutableMap;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import trofers.Trofers;
import trofers.trophy.builder.TrophyBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameParameterValue")
public abstract class TrophyProvider<T extends TrophyBuilder<T>> {

    private final Map<ResourceLocation, T> trophies = new HashMap<>();

    public abstract void addTrophies();

    public void validateTrophies() {
        if (ModList.get().isLoaded(getModId())) {
            for (ResourceLocation trophyId : trophies.keySet()) {
                trophies.get(trophyId).build(trophyId);
            }
        }
    }

    public List<LootTableProvider.SubProviderEntry> getLootTables() {
        return Collections.emptyList();
    }

    protected void addTrophy(ResourceLocation id, T builder) {
        if (trophies.containsKey(id)) {
            throw new IllegalStateException("Duplicate trophy: " + id.toString());
        }
        trophies.put(id, builder);
    }

    public Map<ResourceLocation, T> getTrophies() {
        return ImmutableMap.copyOf(trophies);
    }

    public String getModId() {
        return Trofers.MOD_ID;
    }

    protected ResourceLocation id(String path) {
        return new ResourceLocation(getModId(), path);
    }

    protected CompoundTag item(String itemName) {
        CompoundTag result = new CompoundTag();
        result.putByte("Count", (byte) 1);
        result.putString("id", id(itemName).toString());
        return result;
    }

    protected CompoundTag damageableItem(String itemName) {
        CompoundTag result = item(itemName);
        CompoundTag tag = new CompoundTag();
        result.put("tag", tag);
        tag.putInt("Damage", 0);
        return result;
    }
}
