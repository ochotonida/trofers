package trofers.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import trofers.Trofers;
import trofers.common.init.ModItems;
import trofers.common.init.ModLootModifiers;
import trofers.common.loot.AddItemLootModifier;
import trofers.common.loot.RandomTrophyChanceCondition;
import trofers.common.trophy.Trophy;

public class LootModifiers extends GlobalLootModifierProvider {

    private final Trophies trophies;

    public LootModifiers(DataGenerator generator, Trophies trophies) {
        super(generator, Trofers.MODID);
        this.trophies = trophies;
    }

    @Override
    protected void start() {
        for (Pair<Trophy, String> pair : trophies.trophies) {
            String modid = pair.getSecond();

            // noinspection ConstantConditions
            EntityType<?> entityType = pair.getFirst().entity().getType();
            // noinspection ConstantConditions
            String entityName = entityType.getRegistryName().getPath();
            String path = (Trofers.MODID.equals(modid) ? "" : modid + "/") + entityName;

            ItemStack item = new ItemStack(ModItems.SMALL_PLATE.get());
            item.getOrCreateTagElement("BlockEntityTag").putString("Trophy", String.format("%s:%s", Trofers.MODID, path));

            LootItemCondition[] conditions = new LootItemCondition[]{
                    LootItemKilledByPlayerCondition.killedByPlayer().build(),
                    LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityType))).build(),
                    RandomTrophyChanceCondition.randomTrophyChance().build()
            };

            AddItemLootModifier modifier = new AddItemLootModifier(conditions, item);

            add(path, ModLootModifiers.ADD_ITEM.get(), modifier);
        }
    }
}
