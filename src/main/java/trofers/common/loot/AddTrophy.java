package trofers.common.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.common.trophy.TrophyManager;

import java.util.function.Supplier;

public class AddTrophy extends LootModifier {

    public static final Supplier<Codec<AddTrophy>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.create(instance -> codecStart(instance)
                    .and(ForgeRegistries.ITEMS.getCodec().fieldOf("trophyBase").forGetter(m -> m.trophyBase))
                    .and(ResourceLocation.CODEC.fieldOf("trophyId").forGetter(m -> m.trophyId))
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
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (TrophyManager.get(trophyId) == null) {
            Trofers.LOGGER.error("Failed to find trophy with invalid id '{}'", trophyId);
        } else {
            ItemStack stack = new ItemStack(trophyBase);
            stack.getOrCreateTagElement("BlockEntityTag").putString("Trophy", trophyId.toString());
            generatedLoot.add(stack);
        }
        return generatedLoot;
    }
}
