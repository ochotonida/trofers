package trofers.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import trofers.Trofers;
import trofers.common.trophy.Trophy;
import trofers.data.trophies.AlexsMobsTrophies;
import trofers.data.trophies.VanillaTrophies;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Trophies implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public final List<Trophy> trophies = new ArrayList<>();
    private final DataGenerator generator;

    public Trophies(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    protected void addTrophies() {
        trophies.addAll(VanillaTrophies.createTrophies());
        trophies.addAll(AlexsMobsTrophies.createTrophies());
    }

    @Override
    public void run(HashCache cache) {
        addTrophies();

        Path outputFolder = generator.getOutputFolder();
        Set<ResourceLocation> resourceLocations = Sets.newHashSet();

        for (Trophy trophy : trophies) {
            // noinspection ConstantConditions
            String modId = trophy.entity().getType().getRegistryName().getNamespace();
            if (!resourceLocations.add(trophy.id())) {
                throw new IllegalStateException("Duplicate trophy " + trophy.id());
            } else {
                Path path = createPath(outputFolder, trophy);
                try {
                    JsonObject object;
                    if (modId.equals("minecraft")) {
                        object = trophy.toJson();
                    } else {
                        object = trophy.toJson(new ModLoadedCondition(modId));
                    }
                    DataProvider.save(GSON, cache, object, path);
                } catch (IOException ioexception) {
                    Trofers.LOGGER.error("Couldn't save trophy {}", path, ioexception);
                }
            }
        }
    }

    private static Path createPath(Path path, Trophy trophy) {
        return path.resolve("data/" + trophy.id().getNamespace() + "/trofers/" + trophy.id().getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Trophies";
    }
}
