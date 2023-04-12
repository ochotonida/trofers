package trofers.data.providers;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.forge.common.trophy.Trophy;
import trofers.data.providers.trophies.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class Trophies implements DataProvider {

    public final List<Trophy> trophies = new ArrayList<>();
    private final PackOutput packOutput;

    public Trophies(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    protected void addTrophies() {
        trophies.addAll(VanillaTrophies.createTrophies());
        if (ModList.get().isLoaded("alexsmobs"))
            trophies.addAll(AlexsMobsTrophies.createTrophies());
        if (ModList.get().isLoaded("quark"))
            trophies.addAll(QuarkTrophies.createTrophies());
        if (ModList.get().isLoaded("thermal"))
            trophies.addAll(ThermalTrophies.createTrophies());
        if (ModList.get().isLoaded("tinkers_construct"))
            trophies.addAll(TinkersConstructTrophies.createTrophies());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        addTrophies();

        List<CompletableFuture<?>> futures = new ArrayList<>();

        Path outputFolder = packOutput.getOutputFolder();
        Set<ResourceLocation> resourceLocations = Sets.newHashSet();

        for (Trophy trophy : trophies) {
            // noinspection ConstantConditions
            String modId = ForgeRegistries.ENTITY_TYPES.getKey(trophy.entity().getType()).getNamespace();
            if (!resourceLocations.add(trophy.id())) {
                throw new IllegalStateException("Duplicate trophy " + trophy.id());
            } else {
                Path path = createPath(outputFolder, trophy);
                JsonObject object;
                if (modId.equals("minecraft")) {
                    object = trophy.toJson();
                } else {
                    object = trophy.toJson(new ModLoadedCondition(modId));
                }
                futures.add(DataProvider.saveStable(cache, object, path));
            }
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private static Path createPath(Path path, Trophy trophy) {
        return path.resolve("data/" + trophy.id().getNamespace() + "/trofers/" + trophy.id().getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Trophies";
    }
}
