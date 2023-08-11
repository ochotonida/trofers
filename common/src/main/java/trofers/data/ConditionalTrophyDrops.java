package trofers.data;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ConditionalTrophyDrops {

    public final LootItemCondition[] conditions;
    private final Predicate<LootContext> combinedConditions;
    public final Item trophyBase;

    public ConditionalTrophyDrops(LootItemCondition[] conditions, Item trophyBase) {
        this.conditions = conditions;
        this.combinedConditions = LootItemConditions.andConditions(conditions);
        this.trophyBase = trophyBase;
    }

    protected static <T extends ConditionalTrophyDrops> Products.P2<RecordCodecBuilder.Mu<T>, LootItemCondition[], Item> codecStart(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(Codecs.LOOT_CONDITIONS_CODEC
                        .fieldOf("loot_conditions").forGetter(drops -> drops.conditions))
                .and(BuiltInRegistries.ITEM.byNameCodec()
                        .fieldOf("trophyBase").forGetter(drops -> drops.trophyBase));
    }

    public void apply(Consumer<ItemStack> generatedLoot, LootContext context) {
        if (combinedConditions.test(context)) {
            doApply(generatedLoot, context);
        }
    }

    protected abstract void doApply(Consumer<ItemStack> generatedLoot, LootContext context);
}
