package trofers.neoforge;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import trofers.neoforge.datagen.providers.*;

import java.util.concurrent.CompletableFuture;

public class TrofersData {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        TrophyProviders trophyProviders = new TrophyProviders(packOutput);
        generator.addProvider(event.includeServer(), trophyProviders);
        generator.addProvider(event.includeServer(), new LootTables(packOutput, trophyProviders, lookupProvider));
        generator.addProvider(event.includeServer(), new TrophyDropsProvider(packOutput, trophyProviders));
        generator.addProvider(event.includeServer(), new BlockTags(packOutput, lookupProvider, helper));

        BlockStates blockStates = new BlockStates(packOutput, helper);
        generator.addProvider(event.includeClient(), blockStates);
        generator.addProvider(event.includeClient(), new ItemModels(packOutput, blockStates.models().existingFileHelper));
    }
}
