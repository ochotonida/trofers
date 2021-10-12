package trofers.data.loottables;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import trofers.Trofers;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class LootTableBuilder {

    private final Map<EntityType<?>, LootTable.Builder> lootTables = new HashMap<>();

    public List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getLootTables() {
        lootTables.clear();
        addLootTables();

        List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> result = new ArrayList<>();

        lootTables.forEach((type, lootTable) -> {
            String modid = Trofers.MODID.equals(getModId()) ? "" : getModId() + "/";
            // noinspection ConstantConditions
            ResourceLocation location = new ResourceLocation(Trofers.MODID, String.format("trophies/%s", modid + type.getRegistryName().getPath()));
            result.add(Pair.of(() -> builder -> builder.accept(location, lootTable), LootParameterSets.ALL_PARAMS));

        });

        return result;
    }

    public String getModId() {
        return Trofers.MODID;
    }

    protected abstract void addLootTables();

    protected void add(EntityType<?> type, LootTable.Builder lootTable) {
        lootTables.put(type, lootTable);
    }

    protected void add(EntityType<?> type, IRandomRange rolls, LootEntry.Builder<?>... entries) {
        LootPool.Builder lootPool = LootPool.lootPool();
        if (!ConstantRange.exactly(1).equals(rolls)) {
            lootPool.setRolls(rolls);
        }
        for (LootEntry.Builder<?> entry : entries) {
            lootPool.add(entry);
        }
        add(type, LootTable.lootTable().withPool(lootPool));
    }

    protected void add(EntityType<?> type, IRandomRange rolls, Item... items) {
        add(type, rolls, Arrays.stream(items).map(this::entry).toArray(StandaloneLootEntry.Builder<?>[]::new));
    }

    protected void add(EntityType<?> type, int min, int max, LootEntry.Builder<?>... entries) {
        add(type, RandomValueRange.between(min, max), entries);
    }

    protected void add(EntityType<?> type, int min, int max, Item... items) {
        add(type, RandomValueRange.between(min, max), items);
    }

    protected void add(EntityType<?> type, LootEntry.Builder<?>... entries) {
        add(type, ConstantRange.exactly(1), entries);
    }

    protected void add(EntityType<?> type, Item... items) {
        add(type, ConstantRange.exactly(1), items);
    }

    public StandaloneLootEntry.Builder<?> entry(Item item) {
        return ItemLootEntry.lootTableItem(item);
    }

    public StandaloneLootEntry.Builder<?> entry(Item item, int weight) {
        return entry(item).setWeight(weight);
    }
}
