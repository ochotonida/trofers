package trofers.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.KilledByPlayer;
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

            ILootCondition[] conditions = new ILootCondition[]{
                    KilledByPlayer.killedByPlayer().build(),
                    EntityHasProperty.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityType))).build(),
                    RandomTrophyChanceCondition.randomTrophyChance().build()
            };

            AddItemLootModifier modifier = new AddItemLootModifier(conditions, item);

            add(path, ModLootModifiers.ADD_ITEM.get(), modifier);
        }
    }
}
