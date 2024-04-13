package trofers.neoforge.datagen.providers;

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
import trofers.data.AdvancementDrops;
import trofers.data.EntityDrops;
import trofers.loot.AdvancementDropsEnabledCondition;
import trofers.loot.RandomTrophyChanceCondition;
import trofers.neoforge.datagen.providers.trophies.EntityTrophyProvider;
import trofers.registry.ModBlocks;
import trofers.registry.ModResourceLoaders;
import trofers.util.JsonHelper;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TrophyDropsProvider implements DataProvider {

    private final PackOutput packOutput;
    private final Map<String, JsonElement> entityDropsToSerialize = new HashMap<>();
    private final Map<String, JsonElement> advancementDropsToSerialize = new HashMap<>();

    private final TrophyProviders trophyProviders;

    public TrophyDropsProvider(PackOutput packOutput, TrophyProviders trophyProviders) {
        this.trophyProviders = trophyProviders;
        this.packOutput = packOutput;
    }

    private void start() {
        Map<String, Map<ResourceLocation, ResourceLocation>> entityDropsMap = new HashMap<>();
        Map<String, Map<ResourceLocation, ResourceLocation>> advancementDropsMap = new HashMap<>();

        for (EntityTrophyProvider provider : this.trophyProviders.entityTrophies) {
            entityDropsMap.put(provider.getModId(), provider.getEntityDrops());
            advancementDropsMap.put(provider.getModId(), provider.getAdvancementDrops());
        }

        for (EntityTrophyProvider provider : this.trophyProviders.entityTrophies) {
            provider.addExtraEntityDrops(entityDropsMap);
        }

        for (String modId : entityDropsMap.keySet()) {
            if (entityDropsMap.get(modId).isEmpty()) {
                continue;
            }
            LootItemCondition[] conditions = new LootItemCondition[]{
                    LootItemKilledByPlayerCondition.killedByPlayer().build(),
                    RandomTrophyChanceCondition.randomTrophyChance().build()
            };

            addEntityDrops(modId, EntityDrops.create(conditions, ModBlocks.SMALL_PLATE.get(), entityDropsMap.get(modId)));
        }

        for (String modId : advancementDropsMap.keySet()) {
            if (advancementDropsMap.get(modId).isEmpty()) {
                continue;
            }
            LootItemCondition[] conditions = new LootItemCondition[]{
                    AdvancementDropsEnabledCondition.advancementDropsEnabled().build()
            };

            addAdvancementDrops(modId, new AdvancementDrops(conditions, ModBlocks.SMALL_PLATE.get(), advancementDropsMap.get(modId)));
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        start();

        Path entityDropsPath = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Trofers.MOD_ID).resolve(ModResourceLoaders.ENTITY_DROPS.getDirectory());
        Path advancementDropsPath = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Trofers.MOD_ID).resolve(ModResourceLoaders.ADVANCEMENT_DROPS.getDirectory());
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
        entityDropsToSerialize.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, json) ->
                futuresBuilder.add(DataProvider.saveStable(cache, json, entityDropsPath.resolve(name + ".json")))
        ));
        advancementDropsToSerialize.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, json) ->
                futuresBuilder.add(DataProvider.saveStable(cache, json, advancementDropsPath.resolve(name + ".json")))
        ));
        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    private void addEntityDrops(String modId, EntityDrops instance) {
        JsonObject object = EntityDrops.CODEC.encodeStart(JsonOps.INSTANCE, instance).getOrThrow(false, s -> {}).getAsJsonObject();

        if (!modId.equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            JsonHelper.addModLoadedConditions(object, modId);
        }
        String name = "%s_trophies".formatted(modId.equals(ResourceLocation.DEFAULT_NAMESPACE) ? "vanilla" : modId);
        entityDropsToSerialize.put(name, object);
    }

    private void addAdvancementDrops(String modId, AdvancementDrops instance) {
        JsonObject object = AdvancementDrops.CODEC.encodeStart(JsonOps.INSTANCE, instance).getOrThrow(false, s -> {}).getAsJsonObject();

        if (!modId.equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            JsonHelper.addModLoadedConditions(object, modId);
        }
        String name = "%s_trophies".formatted(modId.equals(ResourceLocation.DEFAULT_NAMESPACE) ? "vanilla" : modId);
        advancementDropsToSerialize.put(name, object);
    }

    @Override
    public String getName() {
        return "Trophy Drops";
    }
}
