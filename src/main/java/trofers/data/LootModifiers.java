package trofers.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import trofers.Trofers;
import trofers.common.init.ModItems;
import trofers.common.init.ModLootModifiers;
import trofers.common.loot.AddEntityTrophy;
import trofers.common.loot.RandomTrophyChanceCondition;
import trofers.common.trophy.Trophy;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LootModifiers implements IDataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;
    private final String modid;
    private final Map<String, Tuple<GlobalLootModifierSerializer<?>, JsonObject>> toSerialize = new HashMap<>();

    private final Trophies trophies;

    public LootModifiers(DataGenerator generator, Trophies trophies) {
        this.trophies = trophies;
        this.generator = generator;
        this.modid = Trofers.MODID;
    }

    protected void start() {
        for (String modid : trophies.trophies.keySet()) {
            HashMap<EntityType<?>, ResourceLocation> trophyMap = new HashMap<>();

            for (Trophy trophy : trophies.trophies.get(modid)) {
                // noinspection ConstantConditions
                EntityType<?> entityType = trophy.entity().getType();
                trophyMap.put(entityType, trophy.id());
            }

            ILootCondition[] conditions = new ILootCondition[]{
                    KilledByPlayer.killedByPlayer().build(),
                    RandomTrophyChanceCondition.randomTrophyChance().build()
            };

            AddEntityTrophy modifier = new AddEntityTrophy(conditions, ModItems.SMALL_PLATE.get(), trophyMap);

            String name = Trofers.MODID.equals(modid) ? "vanilla" : modid;
            name = name + "_trophies";
            add(name, ModLootModifiers.ADD_ENTITY_TROPHY.get(), modifier);
        }
    }

    @Override
    public void run(DirectoryCache cache) {
        start();

        String modPath = "data/" + modid + "/loot_modifiers/";

        toSerialize.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, pair) -> {
            Path modifierPath = generator.getOutputFolder().resolve(modPath + name + ".json");

            JsonObject json = pair.getB();
            // noinspection ConstantConditions
            json.addProperty("type", pair.getA().getRegistryName().toString());

            IDataProvider.save(GSON, cache, json, modifierPath);
        }));
    }

    public <T extends IGlobalLootModifier> void add(String modifier, GlobalLootModifierSerializer<T> serializer, T instance) {
        this.toSerialize.put(modifier, new Tuple<>(serializer, serializer.write(instance)));
    }

    @Override
    public String getName() {
        return "Global Loot Modifiers : " + modid;
    }
}
