package trofers.data;

import com.mojang.datafixers.util.Pair;
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

public class LootModifiers extends GlobalLootModifierProvider {

    private final Trophies trophies;

    public LootModifiers(DataGenerator generator, Trophies trophies) {
        super(generator, Trofers.MODID);
        this.trophies = trophies;
    }

    @Override
    protected void start() {
        HashMap<EntityType<?>, ResourceLocation> trophyMap = new HashMap<>();

        for (Pair<Trophy, String> pair : trophies.trophies) {
            Trophy trophy = pair.getFirst();
            // noinspection ConstantConditions
            EntityType<?> entityType = trophy.entity().getType();
            trophyMap.put(entityType, trophy.id());
        }

        LootItemCondition[] conditions = new LootItemCondition[]{
                LootItemKilledByPlayerCondition.killedByPlayer().build(),
                RandomTrophyChanceCondition.randomTrophyChance().build()
        };

        AddEntityTrophy modifier = new AddEntityTrophy(conditions, ModItems.SMALL_PLATE.get(), trophyMap);

        add("add_entity_trophy", ModLootModifiers.ADD_ENTITY_TROPHY.get(), modifier);
    }
}
