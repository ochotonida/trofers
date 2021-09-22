package trofers.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import trofers.Trofers;
import trofers.common.init.ModItems;
import trofers.common.loot.RandomTrophyChanceCondition;
import trofers.common.block.TrophyBlock;
import trofers.common.init.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTables extends LootTableProvider {

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> lootTables = new ArrayList<>();
    private final Trophies trophies;

    public LootTables(DataGenerator generator, Trophies trophies) {
        super(generator);
        this.trophies = trophies;
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        lootTables.clear();

        addBlockLootTables();
        addEntityLootTables();

        return lootTables;
    }

    private void addBlockLootTables() {
        CopyNbt.Builder copyNbtBuilder = CopyNbt
                .copyData(CopyNbt.Source.BLOCK_ENTITY)
                .copy("Trophy", "BlockEntityTag.Trophy");

        for (RegistryObject<TrophyBlock> trophy : ModBlocks.TROPHIES) {
            ResourceLocation location = new ResourceLocation(Trofers.MODID, "blocks/" + trophy.getId().getPath());
            LootTable.Builder lootTable = LootTable.lootTable().withPool(
                    LootPool.lootPool().add(
                            ItemLootEntry.lootTableItem(
                                    trophy.get()
                            ).apply(copyNbtBuilder)
                    )
            );
            lootTables.add(Pair.of(() -> builder -> builder.accept(location, lootTable), LootParameterSets.BLOCK));
        }
    }

    private void addEntityLootTables() {
        trophies.trophies.forEach((id, trophy) -> {
            // noinspection ConstantConditions
            String entityName = trophy.entity().getType().getRegistryName().getPath();
            ResourceLocation location = new ResourceLocation(Trofers.MODID, "inject/entities/" + entityName);
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("BlockEntityTag", new CompoundNBT());
            nbt.getCompound("BlockEntityTag").putString("Trophy", String.format("%s:%s", Trofers.MODID, entityName));
            LootTable.Builder lootTable = LootTable.lootTable().withPool(
                    LootPool.lootPool()
                            .name("main")
                            .when(RandomTrophyChanceCondition.randomTrophyChance())
                            .when(KilledByPlayer.killedByPlayer())
                            .add(
                                    ItemLootEntry.lootTableItem(ModItems.SMALL_PLATE.get())
                            )
                            .apply(SetNBT.setTag(nbt))
            );
            lootTables.add(Pair.of(() -> builder -> builder.accept(location, lootTable), LootParameterSets.ENTITY));
        });
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationTracker) {
        map.forEach((location, lootTable) -> LootTableManager.validate(validationTracker, location, lootTable));
    }
}
