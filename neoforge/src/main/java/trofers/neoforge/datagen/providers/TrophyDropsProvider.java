package trofers.neoforge.datagen.providers;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
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
import trofers.registry.ModRegistries;
import trofers.util.ConditionsHelper;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
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
            List<LootItemCondition> conditions = List.of(
                    LootItemKilledByPlayerCondition.killedByPlayer().build(),
                    RandomTrophyChanceCondition.randomTrophyChance().build()
            );

            addEntityDrops(modId, EntityDrops.create(conditions, ModBlocks.SMALL_PLATE.get(), entityDropsMap.get(modId), false));
        }

        for (String modId : advancementDropsMap.keySet()) {
            if (advancementDropsMap.get(modId).isEmpty()) {
                continue;
            }
            List<LootItemCondition> conditions = List.of(
                    AdvancementDropsEnabledCondition.advancementDropsEnabled().build()
            );

            addAdvancementDrops(modId, new AdvancementDrops(conditions, ModBlocks.SMALL_PLATE.get(), advancementDropsMap.get(modId)));
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        start();

        Path entityDropsPath = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(Trofers.MOD_ID)
                .resolve(ModRegistries.ENTITY_DROPS.location().toString().replace(':', '/'));
        Path advancementDropsPath = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(Trofers.MOD_ID)
                .resolve(ModRegistries.ADVANCEMENT_DROPS.location().toString().replace(':', '/'));
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
        entityDropsToSerialize.forEach((name, json) ->
                futuresBuilder.add(DataProvider.saveStable(cache, json, entityDropsPath.resolve(name + ".json")))
        );
        advancementDropsToSerialize.forEach((name, json) ->
                futuresBuilder.add(DataProvider.saveStable(cache, json, advancementDropsPath.resolve(name + ".json")))
        );
        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    private void addEntityDrops(String modId, EntityDrops instance) {
        JsonObject object = EntityDrops.CODEC.encodeStart(JsonOps.INSTANCE, instance).getOrThrow().getAsJsonObject();

        if (!modId.equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            ConditionsHelper.addModLoadedConditions(object, modId);
        }
        String name = "%s_trophies".formatted(modId.equals(ResourceLocation.DEFAULT_NAMESPACE) ? "vanilla" : modId);
        entityDropsToSerialize.put(name, object);
    }

    private void addAdvancementDrops(String modId, AdvancementDrops instance) {
        JsonObject object = AdvancementDrops.CODEC.encodeStart(JsonOps.INSTANCE, instance).getOrThrow().getAsJsonObject();

        if (!modId.equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            ConditionsHelper.addModLoadedConditions(object, modId);
        }
        String name = "%s_trophies".formatted(modId.equals(ResourceLocation.DEFAULT_NAMESPACE) ? "vanilla" : modId);
        advancementDropsToSerialize.put(name, object);
    }

    @Override
    public String getName() {
        return "Trophy Drops";
    }
}
