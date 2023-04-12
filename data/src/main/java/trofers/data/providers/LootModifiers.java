package trofers.data.providers;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.forge.common.init.ModItems;
import trofers.forge.common.loot.AddEntityTrophy;
import trofers.forge.common.loot.RandomTrophyChanceCondition;
import trofers.forge.common.trophy.Trophy;
import trofers.data.providers.trophies.TinkersConstructTrophies;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LootModifiers implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final PackOutput output;
    private final String modid;
    private final Map<String, JsonElement> toSerialize = new HashMap<>();

    private final Trophies trophies;

    public LootModifiers(PackOutput packOutput, Trophies trophies) {
        this.trophies = trophies;
        this.output = packOutput;
        this.modid = Trofers.MOD_ID;
    }

    @SuppressWarnings("ConstantConditions")
    protected void start() {
        Map<String, Map<EntityType<?>, Trophy>> trophies = new HashMap<>();
        for (Trophy trophy : this.trophies.trophies) {
            EntityType<?> entityType = trophy.entity().getType();
            String modid = ForgeRegistries.ENTITY_TYPES.getKey(trophy.entity().getType()).getNamespace();

            if (!trophies.containsKey(modid)) {
                trophies.put(modid, new HashMap<>());
            }
            trophies.get(modid).put(entityType, trophy);
        }

        if (ModList.get().isLoaded("tinkers_construct"))
            TinkersConstructTrophies.addExtraTrophies(trophies);

        for (String modId : trophies.keySet()) {
            LootItemCondition[] conditions = new LootItemCondition[]{
                    LootItemKilledByPlayerCondition.killedByPlayer().build(),
                    RandomTrophyChanceCondition.randomTrophyChance().build()
            };

            Map<ResourceLocation, ResourceLocation> trophyIds = trophies.get(modId).entrySet().stream().collect(Collectors.toMap(entry -> ForgeRegistries.ENTITY_TYPES.getKey(entry.getKey()), entry -> entry.getValue().id()));
            AddEntityTrophy modifier = new AddEntityTrophy(conditions, ModItems.SMALL_PLATE.get(), trophyIds);

            String name = modId.equals("minecraft") ? "vanilla" : modId;
            name = name + "_trophies";
            add(name, modifier);
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        start();

        Path forgePath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve("forge").resolve("loot_modifiers").resolve("global_loot_modifiers.json");
        Path modifierFolderPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve("loot_modifiers");
        List<ResourceLocation> entries = new ArrayList<>();

        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

        toSerialize.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, json) ->
        {
            entries.add(new ResourceLocation(modid, name));
            Path modifierPath = modifierFolderPath.resolve(name + ".json");
            futuresBuilder.add(DataProvider.saveStable(cache, json, modifierPath));
        }));

        JsonObject forgeJson = new JsonObject();
        forgeJson.addProperty("replace", false);
        forgeJson.add("entries", GSON.toJsonTree(entries.stream().map(ResourceLocation::toString).collect(Collectors.toList())));

        // futuresBuilder.add(DataProvider.saveStable(cache, forgeJson, forgePath));

        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    public <T extends IGlobalLootModifier> void add(String modifier, T instance) {
        JsonElement json = IGlobalLootModifier.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, instance).getOrThrow(false, s -> {});
        this.toSerialize.put(modifier, json);
    }

    @Override
    public String getName() {
        return "Global Loot Modifiers : " + modid;
    }
}
