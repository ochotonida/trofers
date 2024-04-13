package trofers.neoforge.datagen.providers.trophies;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.trophy.builder.EntityTrophyBuilder;
import trofers.trophy.builder.ItemTrophyBuilder;
import trofers.trophy.builder.TrophyBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntityTrophyProvider extends TrophyProvider {

    private static final String AMBIENT = "ambient";
    private static final String HURT = "hurt";
    private static final String ATTACK = "attack";

    private final List<LootTableProvider.SubProviderEntry> lootTables = new ArrayList<>();
    private final String modId;

    private final Map<ResourceLocation, ResourceLocation> entityDrops = new HashMap<>();
    private final Map<ResourceLocation, ResourceLocation> advancementDrops = new HashMap<>();

    public EntityTrophyProvider(String modId) {
        this.modId = modId;
    }

    @Override
    public String getModId() {
        return modId;
    }

    public Map<ResourceLocation, ResourceLocation> getEntityDrops() {
        return new HashMap<>(entityDrops);
    }

    public Map<ResourceLocation, ResourceLocation> getAdvancementDrops() {
        return new HashMap<>(advancementDrops);
    }

    @Override
    public List<LootTableProvider.SubProviderEntry> getLootTables() {
        return lootTables;
    }

    protected void addTrophy(ResourceLocation id, TrophyBuilder<?> builder, ResourceLocation entityTypeId) {
        entityDrops.put(entityTypeId, id);
        addTrophy(id, builder);
    }

    public void addExtraEntityDrops(Map<String, Map<ResourceLocation, ResourceLocation>> trophies) {

    }

    protected ResourceLocation getEntitySound(String entityName, String entitySoundName) {
        return id("entity.%s.%s".formatted(entityName, entitySoundName));
    }

    protected String getDefaultSoundName() {
        return AMBIENT;
    }

    protected int getDefaultLootCooldown() {
        return 8 * 60;
    }

    protected EntityTrophyWithLootBuilder builder(String entityName) {
        return builder(id(entityName));
    }

    protected EntityTrophyWithLootBuilder builder(EntityType<?> entityType) {
        return builder(BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
    }

    protected EntityTrophyWithLootBuilder builder(ResourceLocation entityType) {
        EntityTrophyWithLootBuilder builder = new EntityTrophyWithLootBuilder(entityType);
        addTrophy(createEntityTrophyId(entityType), builder, entityType);
        return builder;
    }

    @SuppressWarnings({"ConstantConditions", "unused"})
    protected ItemTrophyBuilder itemTrophyBuilder(EntityType<?> entityType) {
        ResourceLocation entityTypeId = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        ItemTrophyBuilder builder = new ItemTrophyBuilder();
        addTrophy(createEntityTrophyId(entityTypeId), builder, entityTypeId);
        defaultTrophySettings(builder, entityTypeId);
        return builder;
    }

    @SuppressWarnings("unused")
    protected ItemTrophyBuilder itemTrophyBuilder(ResourceLocation entityType) {
        ItemTrophyBuilder builder = new ItemTrophyBuilder();
        addTrophy(createEntityTrophyId(entityType), builder, entityType);
        defaultTrophySettings(builder, entityType);
        return builder;
    }

    protected ResourceLocation createEntityTrophyId(ResourceLocation entityId) {
        String folder = "";
        if (!entityId.getNamespace().equals("minecraft")) {
            folder = entityId.getNamespace() + "/";
        }
        return Trofers.id(folder + entityId.getPath());
    }

    public static Component getEntityDescription(ResourceLocation entityId) {
        return Component.translatable(Util.makeDescriptionId("entity", entityId));
    }

    protected void defaultTrophySettings(TrophyBuilder<?> builder, ResourceLocation entityId) {
        builder.baseColor(0x606060);
        builder.scale(0.25);
        builder.requiresMod(modId);
        defaultSound(builder, entityId);
    }

    protected void defaultSound(TrophyBuilder<?> builder, ResourceLocation entityId) {
        builder.sound(getEntitySound(entityId.getPath(), getDefaultSoundName()));
    }

    protected void dropsFromAdvancement(EntityType<?> entityType, ResourceLocation advancementId) {
        dropsFromAdvancement(BuiltInRegistries.ENTITY_TYPE.getKey(entityType), advancementId);
    }

    protected void dropsFromAdvancement(ResourceLocation entityId, ResourceLocation advancementId) {
        if (!entityDrops.containsKey(entityId)) {
            throw new IllegalStateException();
        }
        advancementDrops.put(advancementId, entityDrops.get(entityId));
        entityDrops.remove(entityId);
    }

    @SuppressWarnings("UnusedReturnValue")
    public class EntityTrophyWithLootBuilder extends EntityTrophyBuilder<EntityTrophyWithLootBuilder> {

        protected EntityTrophyWithLootBuilder(ResourceLocation entityId) {
            super(entityId);
            defaultTrophySettings(this, entityId);
        }

        public EntityTrophyWithLootBuilder entitySound(String entitySoundName) {
            return sound(getEntitySound(getEntityId().getPath(), entitySoundName));
        }

        public EntityTrophyWithLootBuilder ambientSound() {
            return entitySound(AMBIENT);
        }

        public EntityTrophyWithLootBuilder hurtSound() {
            return entitySound(HURT);
        }

        public EntityTrophyWithLootBuilder attackSound() {
            return entitySound(ATTACK);
        }

        @Override
        public EntityTrophyWithLootBuilder lootTable(@Nullable ResourceLocation lootTable) {
            cooldown(getDefaultLootCooldown());
            return super.lootTable(lootTable);
        }

        public EntityTrophyWithLootBuilder loot(LootTable.Builder lootTable) {
            String modId = getEntityId().getNamespace().equals("minecraft") ? "" : getModId() + "/";
            ResourceLocation location = Trofers.id(String.format("trophies/%s", modId + getEntityId().getPath()));

            lootTables.add(new LootTableProvider.SubProviderEntry(
                    () -> builder -> builder.accept(location, lootTable), LootContextParamSets.ALL_PARAMS)
            );
            lootTable(location);
            return this;
        }

        public EntityTrophyWithLootBuilder loot(LootPoolEntryContainer.Builder<?> entry) {
            return loot(LootTable.lootTable().withPool(LootPool.lootPool().add(entry)));
        }

        public EntityTrophyWithLootBuilder loot(Item item) {
            return loot(LootItem.lootTableItem(item));
        }

        @Override
        public EntityTrophyWithLootBuilder color(int baseColor, int accentColor) {
            name(getTrophyName(getEntityId(), accentColor));
            return super.color(baseColor, accentColor);
        }

        protected Component getTrophyName(ResourceLocation entityId, int color) {
            return ComponentUtils.mergeStyles(
                    Component.translatable(
                            "trophy.trofers.composed",
                            getEntityDescription(entityId)
                    ),
                    Style.EMPTY.withColor(TextColor.fromRgb(color))
            );
        }
    }
}
