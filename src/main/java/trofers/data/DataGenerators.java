package trofers.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import trofers.Trofers;

@Mod.EventBusSubscriber(modid = Trofers.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if (event.includeServer()) {
            Trophies trophies = new Trophies(generator);
            generator.addProvider(trophies);
            generator.addProvider(new LootTables(generator, trophies));
            generator.addProvider(new BlockTags(generator, helper));
        }
        if (event.includeClient()) {
            BlockStates blockStates = new BlockStates(generator, helper);
            generator.addProvider(blockStates);
            generator.addProvider(new ItemModels(generator, blockStates.models().existingFileHelper));
        }
    }
}
