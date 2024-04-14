package trofers.neoforge.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;
import trofers.Trofers;
import trofers.registry.ModResourceLoaders;
import trofers.trophy.Trophy;

import java.util.function.Supplier;

public class AddTrophy extends LootModifier {

    public static final Supplier<Codec<AddTrophy>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.create(instance -> codecStart(instance)
                    .and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("trophy_base").forGetter(m -> m.trophyBase))
                    .and(ResourceLocation.CODEC.fieldOf("trophy_id").forGetter(m -> m.trophyId))
                    .apply(instance, AddTrophy::new)
            )
    );

    private final Item trophyBase;
    private final ResourceLocation trophyId;

    public AddTrophy(LootItemCondition[] conditions, Item trophyBase, ResourceLocation trophyId) {
        super(conditions);
        this.trophyBase = trophyBase;
        this.trophyId = trophyId;
    }

    @Override
    public Codec<AddTrophy> codec() {
        return CODEC.get();
    }

    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Trophy trophy = ModResourceLoaders.TROPHIES.get(trophyId);
        if (trophy == null) {
            Trofers.LOGGER.error("Failed to find trophy with invalid id '{}'", trophyId);
        } else {
            generatedLoot.add(Trophy.createItem(trophyBase, trophyId));
        }
        return generatedLoot;
    }
}
