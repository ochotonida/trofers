package trofers.neoforge.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import trofers.registry.ModRegistries;

public class AddEntityTrophies extends LootModifier {

    public static final MapCodec<AddEntityTrophies> CODEC = RecordCodecBuilder.mapCodec(instance -> codecStart(instance).apply(instance, AddEntityTrophies::new));

    protected AddEntityTrophies(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.hasParam(LootContextParams.THIS_ENTITY) && ModRegistries.entityDrops() != null) {
            ModRegistries.entityDrops().forEach(entityDrops -> entityDrops.apply(generatedLoot::add, context));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
