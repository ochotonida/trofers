package trofers.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraftforge.fmllegacy.RegistryObject;
import trofers.Trofers;
import trofers.common.init.ModItems;
import trofers.common.loot.RandomTrophyChanceCondition;
import trofers.common.trophy.block.TrophyBlock;
import trofers.common.init.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTables extends LootTableProvider {

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> lootTables = new ArrayList<>();
    private final Trophies trophies;

    public LootTables(DataGenerator generator, Trophies trophies) {
        super(generator);
        this.trophies = trophies;
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        lootTables.clear();

        addBlockLootTables();
        addEntityLootTables();

        return lootTables;
    }

    private void addBlockLootTables() {
        CopyNbtFunction.Builder copyNbtBuilder = CopyNbtFunction
                .copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy("Trophy", "BlockEntityTag.Trophy");

        for (RegistryObject<TrophyBlock> trophy : ModBlocks.TROPHIES) {
            ResourceLocation location = new ResourceLocation(Trofers.MODID, "blocks/" + trophy.getId().getPath());
            LootTable.Builder lootTable = LootTable.lootTable().withPool(
                    LootPool.lootPool().add(
                            LootItem.lootTableItem(
                                    trophy.get()
                            ).apply(copyNbtBuilder)
                    )
            );
            lootTables.add(Pair.of(() -> builder -> builder.accept(location, lootTable), LootContextParamSets.BLOCK));
        }
    }

    private void addEntityLootTables() {
        trophies.trophies.forEach((id, trophy) -> {
            // noinspection ConstantConditions
            String entityName = trophy.entity().getType().getRegistryName().getPath();
            ResourceLocation location = new ResourceLocation(Trofers.MODID, "inject/entities/" + entityName);
            CompoundTag nbt = new CompoundTag();
            nbt.put("BlockEntityTag", new CompoundTag());
            nbt.getCompound("BlockEntityTag").putString("Trophy", String.format("%s:%s", Trofers.MODID, entityName));
            LootTable.Builder lootTable = LootTable.lootTable().withPool(
                    LootPool.lootPool()
                            .name("main")
                            .when(RandomTrophyChanceCondition.randomTrophyChance())
                            .when(LootItemKilledByPlayerCondition.killedByPlayer())
                            .add(
                                    LootItem.lootTableItem(ModItems.SMALL_PLATE.get())
                            )
                            .apply(SetNbtFunction.setTag(nbt))
            );
            lootTables.add(Pair.of(() -> builder -> builder.accept(location, lootTable), LootContextParamSets.ENTITY));
        });
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {
        map.forEach((location, lootTable) -> net.minecraft.world.level.storage.loot.LootTables.validate(validationTracker, location, lootTable));
    }
}
