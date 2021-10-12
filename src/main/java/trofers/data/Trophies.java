package trofers.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import trofers.Trofers;
import trofers.common.trophy.Trophy;
import trofers.data.trophies.TrophyProvider;
import trofers.data.trophies.VanillaTrophies;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class Trophies implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public final Set<Pair<Trophy, String>> trophies = new HashSet<>();
    private final DataGenerator generator;

    public Trophies(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    protected void addTrophies() {
        trophies.clear();

        addTrophies(new VanillaTrophies());
    }

    private void addTrophies(TrophyProvider trophyProvider) {
        for (Trophy trophy : trophyProvider.createTrophies()) {
            addTrophy(trophy, trophyProvider.getModId());
        }
    }

    private void addTrophy(Trophy trophy, String modid) {
        trophies.add(Pair.of(trophy, modid));
    }

    @Override
    public void run(HashCache cache) {
        addTrophies();

        Path outputFolder = generator.getOutputFolder();
        Set<ResourceLocation> resourceLocations = Sets.newHashSet();

        for (Pair<Trophy, String> pair : trophies) {
            Trophy trophy = pair.getFirst();
            String modid = pair.getSecond();

            if (!resourceLocations.add(trophy.id())) {
                throw new IllegalStateException("Duplicate trophy " + trophy.id());
            } else {
                Path path = createPath(outputFolder, trophy);
                try {
                    JsonObject object;
                    if (Trofers.MODID.equals(modid)) {
                        object = trophy.toJson();
                    } else {
                        object = trophy.toJson(new ModLoadedCondition(modid));
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
