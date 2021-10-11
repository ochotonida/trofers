package trofers.data.trophies;

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

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Trophies implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public final Map<ResourceLocation, Trophy> trophies = new HashMap<>();
    private final DataGenerator generator;

    public Trophies(DataGenerator dataGenerator) {
        this.generator = dataGenerator;
    }

    protected void addTrophies() {
        trophies.clear();

        new VanillaTrophies().createTrophies().forEach(this::addTrophy);
    }

    private void addTrophy(Trophy trophy) {
        trophies.put(trophy.id(), trophy);
    }

    @Override
    public void run(HashCache cache) {
        addTrophies();

        Path outputFolder = generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Trophy> consumer = (trophy) -> {
            if (!set.add(trophy.id())) {
                throw new IllegalStateException("Duplicate trophy " + trophy.id());
            } else {
                Path path = createPath(outputFolder, trophy);
                try {
                    JsonObject object;
                    String s = trophy.id().getPath();
                    if (s.contains("/")) {
                        String modid = s.substring(0, s.indexOf('/'));
                        object = trophy.toJson(new ModLoadedCondition(modid));
                    } else {
                        object = trophy.toJson();
                    }
                    DataProvider.save(GSON, cache, object, path);
                } catch (IOException ioexception) {
                    Trofers.LOGGER.error("Couldn't save trophy {}", path, ioexception);
                }

            }
        };

        trophies.forEach((resourceLocation, trophy) -> consumer.accept(trophy));
    }

    private static Path createPath(Path path, Trophy trophy) {
        return path.resolve("data/" + trophy.id().getNamespace() + "/trofers/" + trophy.id().getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Trophies";
    }
}
