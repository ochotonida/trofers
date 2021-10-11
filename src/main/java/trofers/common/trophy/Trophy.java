package trofers.common.trophy;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public record Trophy(
        ResourceLocation id,
        @Nullable
        Component name,
        List<Component> tooltip,
        DisplayInfo display,
        Animation animation,
        ItemStack item,
        @Nullable
        EntityInfo entity,
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

        if (!blockEntityTag.contains("Trophy", Constants.NBT.TAG_STRING)) {
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
        buffer.writeBoolean(name != null);
        if (name != null) {
            buffer.writeComponent(name);
        }
        display.toNetwork(buffer);
        animation.toNetwork(buffer);
        buffer.writeItem(item);
        buffer.writeBoolean(entity != null);
        if (entity != null) {
            entity.toNetwork(buffer);
        }
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
                name,
                tooltip,
                display,
                animation,
                item,
                entity,
                colors,
                effects,
                isHidden
        );
    }

    public JsonObject toJson(ICondition... conditions) {
        JsonObject result = new JsonObject();

        JsonArray array = new JsonArray();
        for (ICondition condition : conditions) {
            array.add(CraftingHelper.serialize(condition));
        }

        result.add("conditions", array);

        return toJson(result);
    }

    public JsonObject toJson() {
        return toJson(new JsonObject());
    }

    public JsonObject toJson(JsonObject result) {
        result.add("name", Component.Serializer.toJsonTree(name()));

        if (!tooltip().isEmpty()) {
            JsonArray tooltip = new JsonArray();
            result.add("tooltip", tooltip);
            for (Component line : tooltip()) {
                tooltip.add(Component.Serializer.toJsonTree(line));
            }
        }

        if (!display().equals(DisplayInfo.NONE)) {
            result.add("display", display().toJson());
        }

        if (!animation().type().equals(Animation.Type.FIXED)) {
            result.add("animation", animation().toJson());
        }

        if (!item().isEmpty()) {
            result.add("item", serializeItem(item()));
        }

        if (entity() != null) {
            result.add("entity", entity().toJson());
        }

        if (!colors().equals(ColorInfo.NONE)) {
            result.add("colors", colors().toJson());
        }

        if (!effects().equals(EffectInfo.NONE)) {
            result.add("effects", effects().toJson());
        }

        if (isHidden()) {
            result.addProperty("isHidden", true);
        }

        return result;
    }

    public static Trophy fromJson(JsonElement element, ResourceLocation id) {
        JsonObject object = GsonHelper.convertToJsonObject(element, "trophy");

        EntityInfo entity = null;
        if (object.has("entity")) {
            entity = EntityInfo.fromJson(GsonHelper.getAsJsonObject(object, "entity"));
        }

        ItemStack item = ItemStack.EMPTY;
        if (object.has("item")) {
            item = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(object, "item"), true);
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
            JsonArray lines = GsonHelper.getAsJsonArray(object, "tooltip");
            for (JsonElement line : lines) {
                tooltip.add(Component.Serializer.fromJson(line));
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
                name,
                tooltip,
                display,
                animation,
                item,
                entity,
                colors,
                effects,
                isHidden
        );
    }

    static JsonObject serializeItem(ItemStack item) {
        JsonObject result = new JsonObject();
        // noinspection ConstantConditions
        result.addProperty("item", item.getItem().getRegistryName().toString());
        if (item.getCount() != 1) {
            result.addProperty("count", item.getCount());
        }
        if (item.hasTag()) {
            // noinspection ConstantConditions
            result.addProperty("nbt", item.getTag().toString());
        }
        return result;
    }

    static CompoundTag readNBT(JsonElement element) {
        try {
            if (element.isJsonObject())
                return TagParser.parseTag(TrophyManager.GSON.toJson(element));
            else {
                return TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
            }
        } catch (CommandSyntaxException exception) {
            throw new JsonSyntaxException(String.format("Invalid NBT Entry: %s", exception));
        }
    }

    static float readOptionalFloat(JsonObject object, String memberName, int defaultValue) {
        if (object.has(memberName)) {
            return GsonHelper.getAsFloat(object, memberName);
        }
        return defaultValue;
    }
}
