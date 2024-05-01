package trofers.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import trofers.Trofers;

import java.util.*;
import java.util.function.Consumer;

public class EntityDrops extends ConditionalTrophyDrops {

    public static final MapCodec<EntityDrops> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> codecStart(instance)
            .and(Codec.unboundedMap(ResourceLocation.CODEC, ResourceLocation.CODEC).stable()
                    .fieldOf("trophies").forGetter(m -> m.trophies))
            .apply(instance, EntityDrops::create)
    );

    public static final Codec<EntityDrops> CODEC = MAP_CODEC.codec();

    public static final EntityDrops NONE = new EntityDrops(List.of(), Items.AIR, new HashMap<>(), new HashSet<>());

    private final Map<ResourceLocation, ResourceLocation> trophies;
    private final Set<EntityType<?>> entities;

    private EntityDrops(List<LootItemCondition> conditions, Item trophyBase, Map<ResourceLocation, ResourceLocation> trophies, Set<EntityType<?>> entities) {
        super(conditions, trophyBase);
        this.trophies = trophies;
        this.entities = entities;
    }
    public static EntityDrops create(List<LootItemCondition> conditions, ItemLike trophyBase, Map<ResourceLocation, ResourceLocation> trophies) {
        return create(conditions, trophyBase, trophies, true);
    }

    public static EntityDrops create(List<LootItemCondition> conditions, ItemLike trophyBase, Map<ResourceLocation, ResourceLocation> trophies, boolean validate) {
        Set<EntityType<?>> entities = new HashSet<>();
        for (ResourceLocation entityTypeId : trophies.keySet()) {
            if (BuiltInRegistries.ENTITY_TYPE.containsKey(entityTypeId) || !validate) {
                entities.add(BuiltInRegistries.ENTITY_TYPE.get(entityTypeId));
            } else {
                Trofers.LOGGER.error("Skipping entity trophy drops entry for missing entity '%s'".formatted(entityTypeId));
            }
        }
        return new EntityDrops(conditions, trophyBase.asItem(), trophies, entities);
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
