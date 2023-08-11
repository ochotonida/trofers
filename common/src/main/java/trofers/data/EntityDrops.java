package trofers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import trofers.Trofers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class EntityDrops extends ConditionalTrophyDrops {

    public static final Codec<EntityDrops> CODEC = RecordCodecBuilder.create(instance -> codecStart(instance)
            .and(Codec.unboundedMap(ResourceLocation.CODEC, ResourceLocation.CODEC)
                    .fieldOf("trophies").forGetter(m -> m.trophies))
            .apply(instance, EntityDrops::create)
    );

    private final Map<ResourceLocation, ResourceLocation> trophies;
    private final Set<EntityType<?>> entities;

    private EntityDrops(LootItemCondition[] conditions, Item trophyBase, Map<ResourceLocation, ResourceLocation> trophies, Set<EntityType<?>> entities) {
        super(conditions, trophyBase);
        this.trophies = trophies;
        this.entities = entities;
    }

    public static EntityDrops create(LootItemCondition[] conditions, ItemLike trophyBase, Map<ResourceLocation, ResourceLocation> trophies, boolean logMissingEntities) {
        Set<EntityType<?>> entities = new HashSet<>();
        for (ResourceLocation entityTypeId : trophies.keySet()) {
            if (BuiltInRegistries.ENTITY_TYPE.containsKey(entityTypeId)) {
                entities.add(BuiltInRegistries.ENTITY_TYPE.get(entityTypeId));
            } else if (logMissingEntities) {
                Trofers.LOGGER.debug("Skipping entity trophy drops entry for missing entity type " + entityTypeId);
            }
        }
        return new EntityDrops(conditions, trophyBase.asItem(), trophies, entities);
    }

    public static EntityDrops create(LootItemCondition[] conditions, ItemLike trophyBase, Map<ResourceLocation, ResourceLocation> trophies) {
        return create(conditions, trophyBase, trophies, true);
    }

    public void apply(Consumer<ItemStack> generatedLoot, LootContext context) {
        if (matchesConditions(context)) {
            doApply(generatedLoot, context);
        }
    }

    private void doApply(Consumer<ItemStack> generatedLoot, LootContext context) {
        if (context.hasParam(LootContextParams.THIS_ENTITY)) {
            EntityType<?> entityType = context.getParam(LootContextParams.THIS_ENTITY).getType();
            if (entities.contains(entityType)) {
                ResourceLocation trophyId = trophies.get(BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
                awardTrophy(trophyId, generatedLoot);
            }
        }
    }
}
