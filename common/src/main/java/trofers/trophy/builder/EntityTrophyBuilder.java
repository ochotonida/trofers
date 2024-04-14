package trofers.trophy.builder;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import trofers.Trofers;
import trofers.trophy.components.EntityInfo;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "UnusedReturnValue"})
public class EntityTrophyBuilder<T extends EntityTrophyBuilder<T>> extends TrophyBuilder<T> {

    private final EntityInfo entityInfo;

    protected EntityTrophyBuilder(ResourceLocation entityId) {
        this.entityInfo = new EntityInfo(entityId, new CompoundTag());
    }

    public ResourceLocation getEntityId() {
        return entityInfo.id();
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
        return Optional.of(entityInfo);
    }

    @Override
    protected void entityInfoToJson(JsonObject result) {
        result.add("entity", EntityInfo.CODEC
                .encodeStart(JsonOps.INSTANCE, entityInfo)
                .getOrThrow(false, Trofers.LOGGER::error)
        );
    }

    public T tag(Consumer<CompoundTag> tagConsumer) {
        tagConsumer.accept(entityInfo.tag());
        return (T) this;
    }

    public T tag(String name, Consumer<CompoundTag> tagConsumer) {
        if (!entityInfo.tag().contains(name)) {
            entityInfo.tag().put(name, new CompoundTag());
        }
        tagConsumer.accept(entityInfo.tag().getCompound(name));
        return (T) this;
    }

    public T putTag(String name, CompoundTag nbt) {
        entityInfo.tag().put(name, nbt);
        return (T) this;
    }

    public T putByte(String name, byte b) {
        entityInfo.tag().putByte(name, b);
        return (T) this;
    }

    public T putInt(String name, int i) {
        entityInfo.tag().putInt(name, i);
        return (T) this;
    }

    public T putFloat(String name, float f) {
        entityInfo.tag().putFloat(name, f);
        return (T) this;
    }

    public T putBoolean(String name, boolean b) {
        entityInfo.tag().putBoolean(name, b);
        return (T) this;
    }

    public T putString(String name, String s) {
        entityInfo.tag().putString(name, s);
        return (T) this;
    }

    public T putUUID(String name, UUID uuid) {
        entityInfo.tag().putUUID(name, uuid);
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
        if (!entityInfo.tag().contains(tagName, Tag.TAG_LIST)) {
            ListTag slots = new ListTag();
            int size = slot.getType() == EquipmentSlot.Type.HAND ? 2 : 4;
            for (int i = 0; i < size; i++) {
                slots.add(new CompoundTag());
            }
            entityInfo.tag().put(tagName, slots);
        }
        ListTag slots = entityInfo.tag().getList(tagName, Tag.TAG_COMPOUND);
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
