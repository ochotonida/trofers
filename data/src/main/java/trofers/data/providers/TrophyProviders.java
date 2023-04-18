package trofers.data.providers;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import trofers.Trofers;
import trofers.data.Util;
import trofers.data.providers.trophies.*;
import trofers.trophy.builder.TrophyBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TrophyProviders implements DataProvider {

    private final PackOutput packOutput;

    public final List<EntityTrophyProvider> entityTrophies = createEntityTrophyProviders();

    public TrophyProviders(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    protected List<EntityTrophyProvider> createEntityTrophyProviders() {
        List<EntityTrophyProvider> result = new ArrayList<>();

        result.add(new VanillaTrophies());
        result.add(new AlexsMobsTrophies());
        result.add(new QuarkTrophies());
        result.add(new ThermalTrophies());
        result.add(new TinkersConstructTrophies());

        return result;
    }

    protected List<TrophyProvider<?>> getTrophyProviders() {
        return new ArrayList<>(entityTrophies);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<TrophyProvider<?>> trophyProviders = getTrophyProviders();
        for (TrophyProvider<?> trophyProvider : trophyProviders) {
            trophyProvider.addTrophies();
            trophyProvider.validateTrophies();
        }

        List<CompletableFuture<?>> futures = new ArrayList<>();

        Path outputFolder = packOutput.getOutputFolder();
        Set<ResourceLocation> resourceLocations = Sets.newHashSet();

        for (TrophyProvider<?> trophyProvider : trophyProviders) {
            Map<ResourceLocation, ? extends TrophyBuilder<?>> trophies = trophyProvider.getTrophies();
            for (ResourceLocation trophyId : trophies.keySet()) {
                if (!resourceLocations.add(trophyId)) {
                    throw new IllegalStateException("Duplicate trophy " + trophyId);
                }
                TrophyBuilder<?> trophy = trophies.get(trophyId);
                Path path = createPath(outputFolder, trophyId);

                JsonObject object;
                String modId = trophyProvider.getModId();
                if (modId.equals("minecraft") || modId.equals(Trofers.MOD_ID)) {
                    object = trophy.toJson();
                } else {
                    object = trophy.toJson(Util.addModLoadedConditions(new JsonObject(), modId));
                }
                futures.add(DataProvider.saveStable(cache, object, path));
            }
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private static Path createPath(Path path, ResourceLocation trophyId) {
        return path.resolve("data/" + trophyId.getNamespace() + "/trofers/" + trophyId.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Trophies";
    }
}
