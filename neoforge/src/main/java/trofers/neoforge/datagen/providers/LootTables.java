package trofers.neoforge.datagen.providers;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import trofers.Trofers;
import trofers.block.TrophyBlock;
import trofers.neoforge.datagen.providers.trophies.TrophyProvider;
import trofers.registry.ModBlocks;
import trofers.registry.ModDataComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LootTables extends net.minecraft.data.loot.LootTableProvider {

    private final List<SubProviderEntry> lootTables = new ArrayList<>();
    private final TrophyProviders trophyProviders;

    public LootTables(PackOutput packOutput, TrophyProviders trophyProviders, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, Set.of(), List.of(), provider);
        this.trophyProviders = trophyProviders;
    }

    @Override
    public List<SubProviderEntry> getTables() {
        lootTables.clear();
        addBlockLootTables();
        for (TrophyProvider provider : trophyProviders.getTrophyProviders()) {
            lootTables.addAll(provider.getLootTables());
        }
        return lootTables;
    }

    private void addBlockLootTables() {
        CopyComponentsFunction.Builder copyNbtBuilder = CopyComponentsFunction
                .copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                .include(ModDataComponents.TROPHY.get());

        for (RegistrySupplier<TrophyBlock> trophy : ModBlocks.TROPHIES) {
            ResourceKey<LootTable> location = ResourceKey.create(Registries.LOOT_TABLE, Trofers.id("blocks/" + trophy.getId().getPath()));
            LootTable.Builder lootTable = LootTable.lootTable().withPool(
                    LootPool.lootPool().add(
                            LootItem.lootTableItem(
                                    trophy.get()
                            ).apply(copyNbtBuilder)
                    )
            );
            lootTables.add(new SubProviderEntry(() -> (provider, builder) -> builder.accept(location, lootTable), LootContextParamSets.BLOCK));
        }
    }
}
