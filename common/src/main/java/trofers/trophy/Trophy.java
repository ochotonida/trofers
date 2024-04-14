package trofers.trophy;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import trofers.Trofers;
import trofers.registry.ModResourceLoaders;
import trofers.trophy.components.*;
import trofers.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Trophy(
        ResourceLocation id,
        Optional<Component> name,
        List<Component> tooltip,
        DisplayInfo display,
        Animation animation,
        ItemStack item,
        Optional<EntityInfo> entity,
        ColorInfo colors,
        EffectInfo effects,
        boolean isHidden
) {

    @Nullable
    public static Trophy getTrophy(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return null;
        }

        CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");

        if (!blockEntityTag.contains("Trophy", Tag.TAG_STRING)) {
            return null;
        }

        try {
            return ModResourceLoaders.TROPHIES.get(new ResourceLocation(blockEntityTag.getString("Trophy")));
        } catch (ResourceLocationException ignored) {
        }

        return null;
    }

    public ItemStack createItem(ItemLike trophyBase) {
        return createItem(trophyBase, id());
    }

    public static ItemStack createItem(ItemLike trophyBase, ResourceLocation id) {
        ItemStack stack = new ItemStack(trophyBase);
        stack.getOrCreateTagElement("BlockEntityTag").putString("Trophy", id.toString());
        return stack;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(id);
        buffer.writeBoolean(name.isPresent());
        name.ifPresent(buffer::writeComponent);
        buffer.writeJsonWithCodec(DisplayInfo.CODEC, display);
        buffer.writeJsonWithCodec(Animation.CODEC, animation);
        buffer.writeItem(item);
        buffer.writeBoolean(entity.isPresent());
        entity.ifPresent(entityInfo -> buffer.writeJsonWithCodec(EntityInfo.CODEC, entityInfo));
        buffer.writeJsonWithCodec(ColorInfo.CODEC, colors);
        buffer.writeJsonWithCodec(EffectInfo.CODEC, effects);
        for (Component line : tooltip) {
            buffer.writeBoolean(true);
            buffer.writeComponent(line);
        }
        buffer.writeBoolean(false);
        buffer.writeBoolean(isHidden);
    }

    public static Trophy fromNetwork(FriendlyByteBuf buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        Component name = null;
        if (buffer.readBoolean()) {
            name = buffer.readComponent();
        }
        DisplayInfo display = buffer.readJsonWithCodec(DisplayInfo.CODEC);
        Animation animation = buffer.readJsonWithCodec(Animation.CODEC);
        ItemStack item = buffer.readItem();
        EntityInfo entity = null;
        if (buffer.readBoolean()) {
            entity = buffer.readJsonWithCodec(EntityInfo.CODEC);
        }
        ColorInfo colors = buffer.readJsonWithCodec(ColorInfo.CODEC);
        EffectInfo effects = buffer.readJsonWithCodec(EffectInfo.CODEC);
        List<Component> tooltip = new ArrayList<>();
        while (buffer.readBoolean()) {
            tooltip.add(buffer.readComponent());
        }
        boolean isHidden = buffer.readBoolean();
        return new Trophy(
                id,
                Optional.ofNullable(name),
                tooltip,
                display,
                animation,
                item,
                Optional.ofNullable(entity),
                colors,
                effects,
                isHidden
        );
    }

    public static Trophy fromJson(JsonElement element, ResourceLocation id) {
        JsonObject object = GsonHelper.convertToJsonObject(element, "trophy");

        EntityInfo entity = null;
        if (object.has("entity")) {
            entity = EntityInfo.CODEC.decode(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(object, "entity"))
                    .getOrThrow(false, Trofers.LOGGER::error)
                    .getFirst();
        }

        ItemStack item = ItemStack.EMPTY;
        if (object.has("item")) {
            item = JsonHelper.deserializeItem(object, "item");
        }

        Animation animation = Animation.STATIC;
        if (object.has("animation")) {
            animation = Animation.CODEC.decode(JsonOps.INSTANCE, GsonHelper.getAsJsonArray(object, "animation"))
                    .getOrThrow(false, Trofers.LOGGER::error)
                    .getFirst();
        }

        DisplayInfo display = DisplayInfo.NONE;
        if (object.has("display")) {
            display = DisplayInfo.CODEC.decode(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(object, "display"))
                    .getOrThrow(false, Trofers.LOGGER::error)
                    .getFirst();
        }

        ColorInfo colors = ColorInfo.NONE;
        if (object.has("colors")) {
            colors = ColorInfo.CODEC.decode(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(object, "colors"))
                    .getOrThrow(false, Trofers.LOGGER::error)
                    .getFirst();
        }

        Component name = null;
        if (object.has("name")) {
            name = Component.Serializer.fromJson(object.get("name"));
        }

        List<Component> tooltip = new ArrayList<>();
        if (object.has("tooltip")) {
            JsonElement tooltipElement = object.get("tooltip");
            if (!tooltipElement.isJsonArray()) {
                tooltip.add(Component.Serializer.fromJson(tooltipElement));
            } else {
                JsonArray lines = GsonHelper.getAsJsonArray(object, "tooltip");
                for (JsonElement line : lines) {
                    tooltip.add(Component.Serializer.fromJson(line));
                }
            }
        }

        EffectInfo effects = EffectInfo.NONE;
        if (object.has("effects")) {
            effects = EffectInfo.CODEC.decode(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(object, "effects"))
                    .getOrThrow(false, Trofers.LOGGER::error)
                    .getFirst();
        }

        boolean isHidden = false;
        if (object.has("is_hidden")) {
            isHidden = GsonHelper.getAsBoolean(object, "is_hidden");
        }

        return new Trophy(
                id,
                Optional.ofNullable(name),
                tooltip,
                display,
                animation,
                item,
                Optional.ofNullable(entity),
                colors,
                effects,
                isHidden
        );
    }
}
