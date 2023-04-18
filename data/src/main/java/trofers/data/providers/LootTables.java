package trofers.data.providers;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import trofers.Trofers;
import trofers.block.TrophyBlock;
import trofers.data.providers.trophies.TrophyProvider;
import trofers.registry.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LootTables extends net.minecraft.data.loot.LootTableProvider {

    private final List<SubProviderEntry> lootTables = new ArrayList<>();
    private final TrophyProviders trophyProviders;

    public LootTables(PackOutput packOutput, TrophyProviders trophyProviders) {
        super(packOutput, Set.of(), List.of());
        this.trophyProviders = trophyProviders;
    }

    @Override
    public List<SubProviderEntry> getTables() {
        lootTables.clear();
        addBlockLootTables();
        for (TrophyProvider<?> provider : trophyProviders.getTrophyProviders()) {
            lootTables.addAll(provider.getLootTables());
        }
        return lootTables;
    }

    private void addBlockLootTables() {
        CopyNbtFunction.Builder copyNbtBuilder = CopyNbtFunction
                .copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy("Trophy", "BlockEntityTag.Trophy");

        for (RegistrySupplier<TrophyBlock> trophy : ModBlocks.TROPHIES) {
            ResourceLocation location = Trofers.id("blocks/" + trophy.getId().getPath());
            LootTable.Builder lootTable = LootTable.lootTable().withPool(
                    LootPool.lootPool().add(
                            LootItem.lootTableItem(
                                    trophy.get()
                            ).apply(copyNbtBuilder)
                    )
            );
            lootTables.add(new SubProviderEntry(() -> builder -> builder.accept(location, lootTable), LootContextParamSets.BLOCK));
        }
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {
        map.forEach((location, lootTable) -> net.minecraft.world.level.storage.loot.LootTables.validate(validationTracker, location, lootTable));
    }
}
