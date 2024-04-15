package trofers.data;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.registry.ModResourceLoaders;
import trofers.trophy.Trophy;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ConditionalTrophyDrops {

    public final LootItemCondition[] conditions;
    private final Predicate<LootContext> combinedConditions;
    public final Item trophyBase;

    public ConditionalTrophyDrops(LootItemCondition[] conditions, Item trophyBase) {
        this.conditions = conditions;
        this.combinedConditions = LootItemConditions.andConditions(List.of(conditions));
        this.trophyBase = trophyBase;
    }

    protected static <T extends ConditionalTrophyDrops> Products.P2<RecordCodecBuilder.Mu<T>, LootItemCondition[], Item> codecStart(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(ModCodecs.LOOT_CONDITIONS_CODEC
                        .fieldOf("conditions").forGetter(drops -> drops.conditions))
                .and(BuiltInRegistries.ITEM.byNameCodec()
                        .fieldOf("trophy_base").forGetter(drops -> drops.trophyBase));
    }

    public boolean matchesConditions(LootContext lootContext) {
        return combinedConditions.test(lootContext);
    }

    public void awardTrophy(@Nullable ResourceLocation trophyId, Consumer<ItemStack> consumer) {
        if (trophyId != null) {
            Trophy trophy = ModResourceLoaders.TROPHIES.get(trophyId);
            if (trophy == null) {
                Trofers.LOGGER.error("Failed to find trophy with invalid id '{}'", trophyId);
            } else {
                consumer.accept(Trophy.createItem(trophyBase, trophyId));
            }
        }
    }
}
