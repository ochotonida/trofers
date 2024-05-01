package trofers.data;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.registry.ModRegistries;
import trofers.trophy.Trophy;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ConditionalTrophyDrops {

    public final List<LootItemCondition> conditions;
    private final Predicate<LootContext> combinedConditions;
    public final Item trophyBase;

    public ConditionalTrophyDrops(List<LootItemCondition> conditions, Item trophyBase) {
        this.conditions = conditions;
        this.combinedConditions = Util.allOf(conditions);
        this.trophyBase = trophyBase;
    }

    protected static <T extends ConditionalTrophyDrops> Products.P2<RecordCodecBuilder.Mu<T>, List<LootItemCondition>, Item> codecStart(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(LootItemConditions.DIRECT_CODEC.listOf()
                        .fieldOf("conditions").forGetter(drops -> drops.conditions))
                .and(BuiltInRegistries.ITEM.byNameCodec()
                        .fieldOf("trophy_base").forGetter(drops -> drops.trophyBase));
    }

    public boolean matchesConditions(LootContext lootContext) {
        return combinedConditions.test(lootContext);
    }

    public void awardTrophy(@Nullable ResourceLocation trophyId, Consumer<ItemStack> consumer) {
        if (trophyId != null) {
            Trophy trophy = ModRegistries.get(ModRegistries.TROPHIES, trophyId);
            if (trophy == null) {
                Trofers.LOGGER.error("Failed to find trophy with invalid id '{}'", trophyId);
            } else {
                consumer.accept(Trophy.createItem(trophyBase, trophyId));
            }
        }
    }
}
