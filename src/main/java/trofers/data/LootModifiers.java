package trofers.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import trofers.Trofers;
import trofers.common.init.ModItems;
import trofers.common.init.ModLootModifiers;
import trofers.common.loot.AddEntityTrophy;
import trofers.common.loot.RandomTrophyChanceCondition;
import trofers.common.trophy.Trophy;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LootModifiers extends GlobalLootModifierProvider {

    private final Trophies trophies;

    public LootModifiers(DataGenerator generator, Trophies trophies) {
        super(generator, Trofers.MODID);
        this.trophies = trophies;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void start() {
        Map<String, Map<EntityType<?>, Trophy>> trophies = new HashMap<>();
        for (Trophy trophy : this.trophies.trophies) {
            EntityType<?> entityType = trophy.entity().getType();
            String modid = trophy.entity().getType().getRegistryName().getNamespace();

            if (!trophies.containsKey(modid)) {
                trophies.put(modid, new HashMap<>());
            }
            trophies.get(modid).put(entityType, trophy);
        }

        for (String modId : trophies.keySet()) {
            LootItemCondition[] conditions = new LootItemCondition[]{
                    LootItemKilledByPlayerCondition.killedByPlayer().build(),
                    RandomTrophyChanceCondition.randomTrophyChance().build()
            };

            Map<EntityType<?>, ResourceLocation> trophyIds = trophies.get(modId).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().id()));
            AddEntityTrophy modifier = new AddEntityTrophy(conditions, ModItems.SMALL_PLATE.get(), trophyIds);

            String name = modId.equals("minecraft") ? "vanilla" : modId;
            name = name + "_trophies";
            add(name, ModLootModifiers.ADD_ENTITY_TROPHY.get(), modifier);
        }
    }
}
