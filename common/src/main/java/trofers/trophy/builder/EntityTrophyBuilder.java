package trofers.trophy.builder;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import trofers.trophy.components.EntityInfo;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused"})
public class EntityTrophyBuilder<T extends EntityTrophyBuilder<T>> extends TrophyBuilder<T> {

    private final ResourceLocation entityId;
    private final CompoundTag entityTag = new CompoundTag();

    protected EntityTrophyBuilder(ResourceLocation entityId) {
        this.entityId = entityId;
    }

    public ResourceLocation getEntityId() {
        return entityId;
    }

    @Override
    protected ItemStack getDisplayItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void displayItemToJson(JsonObject result) {
        // no-op
    }

    @Override
    protected Optional<EntityInfo> getEntityInfo() {
        return Optional.of(new EntityInfo(Registry.ENTITY_TYPE.get(entityId), entityTag, false));
    }

    @Override
    protected void entityInfoToJson(JsonObject result) {
        JsonObject entity = new JsonObject();
        entity.addProperty("type", entityId.toString());
        if (!entityTag.isEmpty()) {
            entity.addProperty("nbt", entityTag.toString());
        }
        result.add("entity", entity);
    }

    public T tag(Consumer<CompoundTag> tagConsumer) {
        tagConsumer.accept(entityTag);
        return (T) this;
    }

    public T tag(String name, Consumer<CompoundTag> tagConsumer) {
        if (!entityTag.contains(name)) {
            entityTag.put(name, new CompoundTag());
        }
        tagConsumer.accept(entityTag.getCompound(name));
        return (T) this;
    }

    public T putTag(String name, CompoundTag nbt) {
        entityTag.put(name, nbt);
        return (T) this;
    }

    public T putByte(String name, byte b) {
        entityTag.putByte(name, b);
        return (T) this;
    }

    public T putInt(String name, int i) {
        entityTag.putInt(name, i);
        return (T) this;
    }

    public T putFloat(String name, float f) {
        entityTag.putFloat(name, f);
        return (T) this;
    }

    public T putBoolean(String name, boolean b) {
        entityTag.putBoolean(name, b);
        return (T) this;
    }

    public T putString(String name, String s) {
        entityTag.putString(name, s);
        return (T) this;
    }

    public T putUUID(String name, UUID uuid) {
        entityTag.putUUID(name, uuid);
        return (T) this;
    }

    public T putCustomName(String customName) {
        return putString("CustomName", Component.Serializer.toJson(Component.literal(customName)));
    }

    public T putItem(String tag, ItemStack stack) {
        return putTag(tag, stack.save(new CompoundTag()));
    }

    public T putItem(String tag, Item item) {
        return putItem(tag, new ItemStack(item));
    }

    public T putEquipment(EquipmentSlot slot, CompoundTag item) {
        String tagName = slot.getType() == EquipmentSlot.Type.HAND ? "HandItems" : "ArmorItems";
        if (!entityTag.contains(tagName, Tag.TAG_LIST)) {
            ListTag slots = new ListTag();
            int size = slot.getType() == EquipmentSlot.Type.HAND ? 2 : 4;
            for (int i = 0; i < size; i++) {
                slots.add(new CompoundTag());
            }
            entityTag.put(tagName, slots);
        }
        ListTag slots = entityTag.getList(tagName, Tag.TAG_COMPOUND);
        slots.set(slot.getIndex(), item);
        return (T) this;
    }

    public T putEquipment(EquipmentSlot slot, ItemStack itemStack) {
        return putEquipment(slot, itemStack.save(new CompoundTag()));
    }

    public T putEquipment(EquipmentSlot slot, Item item) {
        return putEquipment(slot, new ItemStack(item));
    }

    public T putHandItem(Item item) {
        return putEquipment(EquipmentSlot.MAINHAND, item);
    }
}
