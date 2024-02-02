package trofers.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import trofers.Trofers;
import trofers.trophy.Trophy;
import trofers.trophy.TrophyManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class AddEntityTrophy extends AbstractLootModifier {

    public static final Supplier<Codec<AddEntityTrophy>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.create(instance -> codecStart(instance)
                    .and(Registry.ITEM.byNameCodec()
                            .fieldOf("trophyBase").forGetter(m -> m.trophyBase))
                    .and(Codec.unboundedMap(ResourceLocation.CODEC, ResourceLocation.CODEC)
                            .fieldOf("trophies").forGetter(m -> m.trophies))
                    .apply(instance, AddEntityTrophy::create)
            )
    );

    private final Item trophyBase;
    private final Map<ResourceLocation, ResourceLocation> trophies;
    private final Set<EntityType<?>> entities;

    private AddEntityTrophy(LootItemCondition[] conditions, Item trophyBase, Map<ResourceLocation, ResourceLocation> trophies, Set<EntityType<?>> entities) {
        super(conditions);
        this.trophyBase = trophyBase;
        this.trophies = trophies;
        this.entities = entities;
    }

    public static AddEntityTrophy create(LootItemCondition[] conditions, ItemLike trophyBase, Map<ResourceLocation, ResourceLocation> trophies, boolean logMissingEntities) {
        Set<EntityType<?>> entities = new HashSet<>();
        for (ResourceLocation entityTypeId : trophies.keySet()) {
            if (Registry.ENTITY_TYPE.containsKey(entityTypeId)) {
                entities.add(Registry.ENTITY_TYPE.get(entityTypeId));
            } else if (logMissingEntities) {
                Trofers.LOGGER.debug("Skipping trophy loot modifier entry for missing entity type " + entityTypeId);
            }
        }
        return new AddEntityTrophy(conditions, trophyBase.asItem(), trophies, entities);
    }

    public static AddEntityTrophy create(LootItemCondition[] conditions, ItemLike trophyBase, Map<ResourceLocation, ResourceLocation> trophies) {
        return create(conditions, trophyBase, trophies, true);
    }

    @Override
    public Codec<? extends AbstractLootModifier> codec() {
        return CODEC.get();
    }

    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.hasParam(LootContextParams.THIS_ENTITY)) {
            EntityType<?> entityTypeId = context.getParam(LootContextParams.THIS_ENTITY).getType();
            if (entities.contains(entityTypeId)) {
                ResourceLocation trophyId = trophies.get(Registry.ENTITY_TYPE.getKey(entityTypeId));
                if (trophyId != null) {
                    Trophy trophy = TrophyManager.get(trophyId);
                    if (trophy == null) {
                        Trofers.LOGGER.error("Failed to find trophy with invalid id '{}'", trophyId);
                    } else {
                        generatedLoot.add(trophy.createItem(trophyBase));
                    }
                }
            }
        }
        return generatedLoot;
    }
}
