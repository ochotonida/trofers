package trofers.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import trofers.Trofers;
import trofers.data.providers.*;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Trofers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TrofersData {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        Trophies trophies = new Trophies(packOutput);
        generator.addProvider(event.includeServer(), trophies);
        generator.addProvider(event.includeServer(), new LootTables(packOutput));
        generator.addProvider(event.includeServer(), new BlockTags(packOutput, lookupProvider, helper));
        generator.addProvider(event.includeServer(), new LootModifiers(packOutput, trophies));

        BlockStates blockStates = new BlockStates(packOutput, helper);
        generator.addProvider(event.includeClient(), blockStates);
        generator.addProvider(event.includeClient(), new ItemModels(packOutput, blockStates.models().existingFileHelper));
    }
}
