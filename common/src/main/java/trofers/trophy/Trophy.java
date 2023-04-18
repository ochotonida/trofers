package trofers.trophy;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import trofers.trophy.components.*;
import trofers.util.JsonHelper;

import javax.annotation.Nullable;
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
            return TrophyManager.get(new ResourceLocation(blockEntityTag.getString("Trophy")));
        } catch (ResourceLocationException ignored) {
        }

        return null;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(id);
        buffer.writeBoolean(name.isPresent());
        name.ifPresent(buffer::writeComponent);
        display.toNetwork(buffer);
        animation.toNetwork(buffer);
        buffer.writeItem(item);
        buffer.writeBoolean(entity.isPresent());
        entity.ifPresent(entityInfo -> entityInfo.toNetwork(buffer));
        colors.toNetwork(buffer);
        effects.toNetwork(buffer);
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
        DisplayInfo display = DisplayInfo.fromNetwork(buffer);
        Animation animation = Animation.fromNetwork(buffer);
        ItemStack item = buffer.readItem();
        EntityInfo entity = null;
        if (buffer.readBoolean()) {
            entity = EntityInfo.fromNetwork(buffer);
        }
        ColorInfo colors = ColorInfo.fromNetwork(buffer);
        EffectInfo effects = EffectInfo.fromNetwork(buffer);
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
            entity = EntityInfo.fromJson(GsonHelper.getAsJsonObject(object, "entity"));
        }

        ItemStack item = ItemStack.EMPTY;
        if (object.has("item")) {
            item = JsonHelper.deserializeItem(object, "item");
        }

        Animation animation = Animation.STATIC;
        if (object.has("animation")) {
            animation = Animation.fromJson(GsonHelper.getAsJsonObject(object, "animation"));
        }

        DisplayInfo display = DisplayInfo.NONE;
        if (object.has("display")) {
            display = DisplayInfo.fromJson(GsonHelper.getAsJsonObject(object, "display"));
        }

        ColorInfo colors = ColorInfo.NONE;
        if (object.has("colors")) {
            colors = ColorInfo.fromJson(GsonHelper.getAsJsonObject(object, "colors"));
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
            effects = EffectInfo.fromJson(GsonHelper.getAsJsonObject(object, "effects"));
        }

        boolean isHidden = false;
        if (object.has("isHidden")) {
            isHidden = GsonHelper.getAsBoolean(object, "isHidden");
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
