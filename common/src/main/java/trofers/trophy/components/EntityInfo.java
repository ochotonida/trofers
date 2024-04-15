package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import trofers.data.ModCodecs;

import java.util.UUID;
import java.util.function.Function;

public class EntityInfo {

    private final ResourceLocation id;
    private final CompoundTag tag;

    private static final Codec<ResourceLocation> ENTITY_ID_CODEC = ResourceLocation.CODEC.comapFlatMap(id ->
            BuiltInRegistries.ENTITY_TYPE.containsKey(id)
                    ? DataResult.success(id)
                    : DataResult.error(() -> String.format("Unknown entity type %s", id)),
            Function.identity()
    );

    public static final Codec<EntityInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ModCodecs.requiredField("id", ENTITY_ID_CODEC).forGetter(entityInfo -> entityInfo.id),
            ModCodecs.defaultField("tag", new CompoundTag(), CompoundTag.CODEC).forGetter(entityInfo -> entityInfo.tag)
    ).apply(instance, EntityInfo::new));

    @Nullable
    private Entity entity;

    public EntityInfo(ResourceLocation id, CompoundTag tag) {
        this.id = id;
        this.tag = tag;
    }

    public ResourceLocation id() {
        return id;
    }

    public CompoundTag tag() {
        return tag;
    }

    @Nullable
    public Entity getOrCreateEntity(Level level) {
        if (entity == null || entity.level() != level) {
            createEntity(level);
        }
        return entity;
    }

    private void createEntity(Level level) {
        if (!BuiltInRegistries.ENTITY_TYPE.containsKey(id)) {
            return;
        }
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(id);
        if (!type.requiredFeatures().isSubsetOf(level.enabledFeatures())) {
            return;
        }

        CompoundTag entityTag = this.tag.copy();
        entityTag.putString("id", id.toString());
        if (!entityTag.hasUUID("UUID")) {
            entityTag.putUUID("UUID", new UUID(1L, 1L));
        }

        entity = EntityType.loadEntityRecursive(entityTag, level, Function.identity());
    }
}
