package trofers.forge.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.trophy.TrophyManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class AddEntityTrophy extends LootModifier {

    public static final Supplier<Codec<AddEntityTrophy>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.create(instance -> LootModifier.codecStart(instance)
                    .and(BuiltInRegistries.ITEM.byNameCodec()
                            .fieldOf("trophyBase").forGetter(m -> m.trophyBase))
                    .and(Codec.unboundedMap(ResourceLocation.CODEC, ResourceLocation.CODEC)
                            .fieldOf("trophies").forGetter(m -> m.trophies))
                    .apply(instance, AddEntityTrophy::new)
            )
    );

    private final Item trophyBase;
    private final Map<ResourceLocation, ResourceLocation> trophies;
    private final Set<EntityType<?>> entities;

    public AddEntityTrophy(LootItemCondition[] conditions, ItemLike trophyBase, Map<ResourceLocation, ResourceLocation> trophies) {
        super(conditions);
        this.trophyBase = trophyBase.asItem();
        this.trophies = trophies;
        entities = new HashSet<>();
        for (ResourceLocation entityTypeId : trophies.keySet()) {
            if (ForgeRegistries.ENTITY_TYPES.containsKey(entityTypeId)) {
                entities.add(ForgeRegistries.ENTITY_TYPES.getValue(entityTypeId));
            } else {
                Trofers.LOGGER.debug("Skipping trophy loot modifier entry for missing entity type " + entityTypeId);
            }
        }
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.hasParam(LootContextParams.THIS_ENTITY)) {
            EntityType<?> entityTypeId = context.getParam(LootContextParams.THIS_ENTITY).getType();
            if (entities.contains(entityTypeId)) {
                ResourceLocation trophyId = trophies.get(ForgeRegistries.ENTITY_TYPES.getKey(entityTypeId));
                if (trophyId != null) {
                    if (TrophyManager.get(trophyId) == null) {
                        Trofers.LOGGER.error("Failed to find trophy with invalid id '{}'", trophyId);
                    } else {
                        ItemStack stack = new ItemStack(trophyBase);
                        stack.getOrCreateTagElement("BlockEntityTag").putString("Trophy", trophyId.toString());
                        generatedLoot.add(stack);
                    }
                }
            }
        }
        return generatedLoot;
    }
}
