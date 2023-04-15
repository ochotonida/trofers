package trofers.loot;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import trofers.platform.PlatformServices;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public abstract class AbstractLootModifier {

    public final LootItemCondition[] conditions;
    private final Predicate<LootContext> combinedConditions;

    protected static <T extends AbstractLootModifier> Products.P1<RecordCodecBuilder.Mu<T>, LootItemCondition[]> codecStart(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(PlatformServices.platformHelper.getLootConditionsCodec().fieldOf("conditions").forGetter((modifier) -> modifier.conditions));
    }

    protected AbstractLootModifier(LootItemCondition[] conditionsIn) {
        this.conditions = conditionsIn;
        this.combinedConditions = LootItemConditions.andConditions(conditionsIn);
    }

    public final ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return this.combinedConditions.test(context) ? this.doApply(generatedLoot, context) : generatedLoot;
    }

    protected abstract ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context);

    public abstract Codec<? extends AbstractLootModifier> codec();
}
