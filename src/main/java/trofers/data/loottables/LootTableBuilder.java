package trofers.data.loottables;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import trofers.Trofers;
import trofers.common.loot.OptionalLootItem;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class LootTableBuilder {

    private final Map<EntityType<?>, LootTable.Builder> lootTables = new HashMap<>();

    public List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getLootTables() {
        lootTables.clear();
        addLootTables();

        List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> result = new ArrayList<>();

        lootTables.forEach((type, lootTable) -> {
            String modid = Trofers.MODID.equals(getModId()) ? "" : getModId() + "/";
            // noinspection ConstantConditions
            ResourceLocation location = new ResourceLocation(Trofers.MODID, String.format("trophies/%s", modid + type.getRegistryName().getPath()));
            result.add(Pair.of(() -> builder -> builder.accept(location, lootTable), LootContextParamSets.ALL_PARAMS));

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

    protected void add(EntityType<?> type, NumberProvider rolls, LootPoolEntryContainer.Builder<?>... entries) {
        LootPool.Builder lootPool = LootPool.lootPool();
        if (!ConstantValue.exactly(1).equals(rolls)) {
            lootPool.setRolls(rolls);
        }
        for (var entry : entries) {
            lootPool.add(entry);
        }
        add(type, LootTable.lootTable().withPool(lootPool));
    }

    protected void add(EntityType<?> type, NumberProvider rolls, Item... items) {
        add(type, rolls, Arrays.stream(items).map(this::entry).toArray(LootPoolSingletonContainer.Builder<?>[]::new));
    }

    protected void add(EntityType<?> type, int min, int max, LootPoolEntryContainer.Builder<?>... entries) {
        add(type, UniformGenerator.between(min, max), entries);
    }

    protected void add(EntityType<?> type, int min, int max, Item... items) {
        add(type, UniformGenerator.between(min, max), items);
    }

    protected void add(EntityType<?> type, LootPoolEntryContainer.Builder<?>... entries) {
        add(type, ConstantValue.exactly(1), entries);
    }

    protected void add(EntityType<?> type, Item... items) {
        add(type, ConstantValue.exactly(1), items);
    }

    public LootPoolSingletonContainer.Builder<?> entry(Item item) {
        // noinspection ConstantConditions
        String modid = item.getRegistryName().getNamespace();
        if ("minecraft".equals(modid) || Trofers.MODID.equals(modid)) {
            return LootItem.lootTableItem(item);
        } else {
            return OptionalLootItem.optionalLootItem(item, new ModLoadedCondition(modid));
        }
    }

    public LootPoolEntryContainer.Builder<?> entry(Item item, int weight) {
        return entry(item).setWeight(weight);
    }
}
