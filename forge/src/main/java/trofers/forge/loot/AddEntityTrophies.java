package trofers.forge.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import trofers.data.EntityDrops;
import trofers.registry.ModResourceLoaders;

public class AddEntityTrophies extends LootModifier {

    public static final Codec<AddEntityTrophies> CODEC = RecordCodecBuilder.create(instance -> codecStart(instance).apply(instance, AddEntityTrophies::new));

    protected AddEntityTrophies(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.hasParam(LootContextParams.THIS_ENTITY)) {
            for (EntityDrops entityDrops : ModResourceLoaders.ENTITY_DROPS.getAllResources()) {
                entityDrops.apply(generatedLoot::add, context);
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
