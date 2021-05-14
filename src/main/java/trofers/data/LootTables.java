package trofers.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import trofers.Trofers;
import trofers.common.TrophyBlock;
import trofers.common.init.ModBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTables extends LootTableProvider {

    public LootTables(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> lootTables = new ArrayList<>();

        List<String> tags = Arrays.asList(
                "DisplayHeight",
                "DisplayScale",
                "AnimationSpeed",
                "Item",
                "Name",
                "Animation",
                "Colors"
        );
        CopyNbt.Builder copyNbtBuilder = CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY);
        tags.forEach(tag -> copyNbtBuilder.copy(tag, "BlockEntityTag." + tag));

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

        return lootTables;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((location, lootTable) -> LootTableManager.validate(validationtracker, location, lootTable));
    }
}
