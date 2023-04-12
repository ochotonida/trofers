package trofers.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.common.init.ModItems;
import trofers.common.loot.AddEntityTrophy;
import trofers.common.loot.RandomTrophyChanceCondition;
import trofers.common.trophy.Trophy;
import trofers.data.trophies.TinkersConstructTrophies;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LootModifiers extends GlobalLootModifierProvider {

    private final Trophies trophies;

    public LootModifiers(PackOutput packOutput, Trophies trophies) {
        super(packOutput, Trofers.MODID);
        this.trophies = trophies;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
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
}
