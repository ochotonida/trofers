package trofers.common.trophy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public final class Trophy {
    private final ResourceLocation id;
    @Nullable
    private final ITextComponent name;
    private final DisplayInfo display;
    private final Animation animation;
    private final ItemStack item;
    @Nullable
    private final EntityInfo entity;
    private final ColorInfo colors;
    private final EffectInfo effects;
    private final boolean isHidden;

    public Trophy(
            ResourceLocation id,
            @Nullable
            ITextComponent name,
            DisplayInfo display,
            Animation animation,
            ItemStack item,
            @Nullable
            EntityInfo entity,
            ColorInfo colors,
            EffectInfo effects,
            boolean isHidden
    ) {
        this.id = id;
        this.name = name;
        this.display = display;
        this.animation = animation;
        this.item = item;
        this.entity = entity;
        this.colors = colors;
        this.effects = effects;
        this.isHidden = isHidden;
    }

    public ResourceLocation id() {
        return id;
    }

    @Nullable
    public ITextComponent name() {
        return name;
    }

    public DisplayInfo display() {
        return display;
    }

    public Animation animation() {
        return animation;
    }

    public ItemStack item() {
        return item;
    }

    @Nullable
    public EntityInfo entity() {
        return entity;
    }

    public ColorInfo colors() {
        return colors;
    }

    public EffectInfo effects() {
        return effects;
    }

    public boolean isHidden() {
        return isHidden;
    }

    @Nullable
    public static Trophy getTrophy(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag == null) {
            return null;
        }

        CompoundNBT blockEntityTag = tag.getCompound("BlockEntityTag");

        if (!blockEntityTag.contains("Trophy", Constants.NBT.TAG_STRING)) {
            return null;
        }

        try {
            return TrophyManager.get(new ResourceLocation(blockEntityTag.getString("Trophy")));
        } catch (ResourceLocationException ignored) {
        }

        return null;
    }

    public void toNetwork(PacketBuffer buffer) {
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
        buffer.writeBoolean(isHidden);
    }

    public static Trophy fromNetwork(PacketBuffer buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        ITextComponent name = null;
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
        boolean isHidden = buffer.readBoolean();
        return new Trophy(
                id,
                name,
                display,
                animation,
                item,
                entity,
                colors,
                effects,
                isHidden
        );
    }

    public JsonObject toJson() {
        JsonObject result = new JsonObject();

        result.add("name", TextComponent.Serializer.toJsonTree(name()));

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
        JsonObject object = JSONUtils.convertToJsonObject(element, "trophy");

        EntityInfo entity = null;
        if (object.has("entity")) {
            entity = EntityInfo.fromJson(JSONUtils.getAsJsonObject(object, "entity"));
        }

        ItemStack item = ItemStack.EMPTY;
        if (object.has("item")) {
            item = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(object, "item"), true);
        }

        Animation animation = Animation.STATIC;
        if (object.has("animation")) {
            animation = Animation.fromJson(JSONUtils.getAsJsonObject(object, "animation"));
        }

        DisplayInfo display = DisplayInfo.NONE;
        if (object.has("display")) {
            display = DisplayInfo.fromJson(JSONUtils.getAsJsonObject(object, "display"));
        }

        ColorInfo colors = ColorInfo.NONE;
        if (object.has("colors")) {
            colors = ColorInfo.fromJson(JSONUtils.getAsJsonObject(object, "colors"));
        }

        ITextComponent name = null;
        if (object.has("name")) {
            name = TextComponent.Serializer.fromJson(object.get("name"));
        }

        EffectInfo effects = EffectInfo.NONE;
        if (object.has("effects")) {
            effects = EffectInfo.fromJson(JSONUtils.getAsJsonObject(object, "effects"));
        }

        boolean isHidden = false;
        if (object.has("isHidden")) {
            isHidden = JSONUtils.getAsBoolean(object, "isHidden");
        }

        return new Trophy(
                id,
                name,
                display,
                animation,
                item,
                entity,
                colors,
                effects,
                isHidden
        );
    }

    protected static JsonObject serializeItem(ItemStack item) {
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

    protected static CompoundNBT readNBT(JsonElement element) {
        try {
            if (element.isJsonObject())
                return JsonToNBT.parseTag(TrophyManager.GSON.toJson(element));
            else {
                return JsonToNBT.parseTag(JSONUtils.convertToString(element, "nbt"));
            }
        } catch (CommandSyntaxException exception) {
            throw new JsonSyntaxException(String.format("Invalid NBT Entry: %s", exception));
        }
    }

    protected static float readOptionalFloat(JsonObject object, String memberName, int defaultValue) {
        if (object.has(memberName)) {
            return JSONUtils.getAsFloat(object, memberName);
        }
        return defaultValue;
    }
}
