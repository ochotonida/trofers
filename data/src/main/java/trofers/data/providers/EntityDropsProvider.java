package trofers.data.providers;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import trofers.Trofers;
import trofers.data.EntityDrops;
import trofers.data.providers.trophies.EntityTrophyProvider;
import trofers.loot.RandomTrophyChanceCondition;
import trofers.registry.ModBlocks;
import trofers.registry.ModResourceLoaders;
import trofers.util.JsonHelper;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EntityDropsProvider implements DataProvider {

    private final PackOutput packOutput;
    private final Map<String, JsonElement> toSerialize = new HashMap<>();

    private final TrophyProviders trophyProviders;

    public EntityDropsProvider(PackOutput packOutput, TrophyProviders trophyProviders) {
        this.trophyProviders = trophyProviders;
        this.packOutput = packOutput;
    }

    protected void start() {
        Map<String, Map<ResourceLocation, ResourceLocation>> trophies = new HashMap<>();
        for (EntityTrophyProvider provider : this.trophyProviders.entityTrophies) {
            Map<ResourceLocation, ResourceLocation> entityToTrophyMap = provider.getEntityToTrophyMap();
            trophies.put(provider.getModId(), entityToTrophyMap);
        }

        for (EntityTrophyProvider provider : this.trophyProviders.entityTrophies) {
            provider.addExtraTrophies(trophies);
        }

        for (String modId : trophies.keySet()) {
            LootItemCondition[] conditions = new LootItemCondition[]{
                    LootItemKilledByPlayerCondition.killedByPlayer().build(),
                    RandomTrophyChanceCondition.randomTrophyChance().build()
            };

            EntityDrops entityDrops = EntityDrops.create(conditions, ModBlocks.SMALL_PLATE.get(), trophies.get(modId));

            add(modId, entityDrops);
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        start();

        Path path = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Trofers.MOD_ID).resolve(ModResourceLoaders.ENTITY_DROPS.getDirectory());
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
        toSerialize.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, json) ->
                futuresBuilder.add(DataProvider.saveStable(cache, json, path.resolve(name + ".json")))
        ));

        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    public void add(String modId, EntityDrops instance) {
        JsonObject json = EntityDrops.CODEC.encodeStart(JsonOps.INSTANCE, instance).getOrThrow(false, s -> {}).getAsJsonObject();
        if (!modId.equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            JsonHelper.addModLoadedConditions(json, modId);
        }
        String name = "%s_trophies".formatted(modId.equals(ResourceLocation.DEFAULT_NAMESPACE) ? "vanilla" : modId);
        toSerialize.put(name, json);
    }

    @Override
    public String getName() {
        return "Entity Trophy Drops : " + Trofers.MOD_ID;
    }
}
