package trofers.common.trophy;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

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
            boolean isHidden
    ) {
        this.id = id;
        this.name = name;
        this.display = display;
        this.animation = animation;
        this.item = item;
        this.entity = entity;
        this.colors = colors;
        this.isHidden = isHidden;
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

    public JsonObject toJson() {
        JsonObject result = new JsonObject();

        result.add("name", TextComponent.Serializer.toJsonTree(name()));

        if (!display().equals(DisplayInfo.NONE)) {
            result.add("display", display().toJson());
        }

        if (animation().type() != Animation.Type.FIXED) {
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

        if (isHidden()) {
            result.addProperty("isHidden", true);
        }

        return result;
    }

    private static JsonObject serializeItem(ItemStack item) {
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
                isHidden
        );
    }

    private static CompoundNBT readNBT(JsonElement element) {
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

    private static float readOptionalFloat(JsonObject object, String memberName, int defaultValue) {
        if (object.has(memberName)) {
            return JSONUtils.getAsFloat(object, memberName);
        }
        return defaultValue;
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

    public boolean isHidden() {
        return isHidden;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Trophy trophy = (Trophy) obj;
        return Objects.equals(this.id, trophy.id) &&
                Objects.equals(this.name, trophy.name) &&
                Objects.equals(this.display, trophy.display) &&
                Objects.equals(this.animation, trophy.animation) &&
                Objects.equals(this.item, trophy.item) &&
                Objects.equals(this.entity, trophy.entity) &&
                Objects.equals(this.colors, trophy.colors) &&
                this.isHidden == trophy.isHidden;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, display, animation, item, entity, colors, isHidden);
    }

    @Override
    public String toString() {
        return "Trophy[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "display=" + display + ", " +
                "animation=" + animation + ", " +
                "item=" + item + ", " +
                "entity=" + entity + ", " +
                "colors=" + colors + ", " +
                "isHidden=" + isHidden + ']';
    }


    public static final class Animation {

        public static final Animation STATIC = new Animation(Type.FIXED, 1);
        private final Type type;
        private final float speed;

        public Animation(Type type, float speed) {
            this.type = type;
            this.speed = speed;
        }

        public JsonObject toJson() {
            JsonObject result = new JsonObject();
            result.addProperty("type", type().name());
            if (speed() != 1) {
                result.addProperty("speed", speed());
            }
            return result;
        }

        private static Animation fromJson(JsonObject object) {
            Type type = Type.fromJson(object.get("type"));
            float speed = readOptionalFloat(object, "speed", 1);
            return new Animation(type, speed);
        }

        public Type type() {
            return type;
        }

        public float speed() {
            return speed;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            Animation animation = (Animation) obj;
            return Objects.equals(this.type, animation.type) &&
                    Float.floatToIntBits(this.speed) == Float.floatToIntBits(animation.speed);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, speed);
        }

        @Override
        public String toString() {
            return "Animation[" +
                    "type=" + type + ", " +
                    "speed=" + speed + ']';
        }


        public enum Type {
            FIXED("fixed"),
            SPINNING("spinning"),
            TUMBLING("tumbling");

            private final String name;

            Type(String name) {
                this.name = name;
            }

            public static Type fromJson(JsonElement element) {
                String name = JSONUtils.convertToString(element, "animation");
                for (Type animation : Type.values()) {
                    if (animation.name.equals(name)) {
                        return animation;
                    }
                }
                throw new JsonParseException(String.format("Invalid trophy animation type: %s", name));
            }
        }
    }

    public static final class DisplayInfo {

        public static final DisplayInfo NONE = new DisplayInfo(0, 0, 0, 0, 0, 0, 1);
        private final float xOffset;
        private final float yOffset;
        private final float zOffset;
        private final float xRotation;
        private final float yRotation;
        private final float zRotation;
        private final float scale;

        public DisplayInfo(float xOffset, float yOffset, float zOffset, float xRotation, float yRotation,
                           float zRotation, float scale) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.zOffset = zOffset;
            this.xRotation = xRotation;
            this.yRotation = yRotation;
            this.zRotation = zRotation;
            this.scale = scale;
        }

        public DisplayInfo(float scale) {
            this(0, 0, 0, scale);
        }

        public DisplayInfo(float xOffset, float yOffset, float zOffset, float scale) {
            this(xOffset, yOffset, zOffset, 0, 0, 0, scale);
        }

        public JsonObject toJson() {
            JsonObject result = new JsonObject();
            if (xOffset() != 0 || yOffset() != 0 || zOffset() != 0) {
                result.add("offset", serializeVector(
                        xOffset(),
                        yOffset(),
                        zOffset()
                ));
            }
            if (xRotation() != 0 || yRotation() != 0 || zRotation() != 0) {
                result.add("rotation", serializeVector(xRotation(), yRotation(), zRotation()));
            }
            if (scale() != 0) {
                result.addProperty("scale", scale());
            }
            return result;
        }

        private static JsonObject serializeVector(float x, float y, float z) {
            JsonObject result = new JsonObject();
            if (x != 0) result.addProperty("x", x);
            if (y != 0) result.addProperty("y", y);
            if (z != 0) result.addProperty("z", z);
            return result;
        }

        private static DisplayInfo fromJson(JsonObject object) {
            float xOffset, yOffset, zOffset;
            xOffset = yOffset = zOffset = 0;
            if (object.has("offset")) {
                JsonObject offset = JSONUtils.getAsJsonObject(object, "offset");
                xOffset = readOptionalFloat(offset, "x", 0);
                yOffset = readOptionalFloat(offset, "y", 0);
                zOffset = readOptionalFloat(offset, "z", 0);
            }

            float xRotation, yRotation, zRotation;
            xRotation = yRotation = zRotation = 0;
            if (object.has("rotation")) {
                JsonObject rotation = JSONUtils.getAsJsonObject(object, "rotation");
                xRotation = readOptionalFloat(rotation, "x", 0);
                yRotation = readOptionalFloat(rotation, "y", 0);
                zRotation = readOptionalFloat(rotation, "z", 0);
            }

            float scale = readOptionalFloat(object, "scale", 1);

            return new DisplayInfo(xOffset, yOffset, zOffset, xRotation, yRotation, zRotation, scale);
        }

        public float xOffset() {
            return xOffset;
        }

        public float yOffset() {
            return yOffset;
        }

        public float zOffset() {
            return zOffset;
        }

        public float xRotation() {
            return xRotation;
        }

        public float yRotation() {
            return yRotation;
        }

        public float zRotation() {
            return zRotation;
        }

        public float scale() {
            return scale;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            DisplayInfo displayInfo = (DisplayInfo) obj;
            return Float.floatToIntBits(this.xOffset) == Float.floatToIntBits(displayInfo.xOffset) &&
                    Float.floatToIntBits(this.yOffset) == Float.floatToIntBits(displayInfo.yOffset) &&
                    Float.floatToIntBits(this.zOffset) == Float.floatToIntBits(displayInfo.zOffset) &&
                    Float.floatToIntBits(this.xRotation) == Float.floatToIntBits(displayInfo.xRotation) &&
                    Float.floatToIntBits(this.yRotation) == Float.floatToIntBits(displayInfo.yRotation) &&
                    Float.floatToIntBits(this.zRotation) == Float.floatToIntBits(displayInfo.zRotation) &&
                    Float.floatToIntBits(this.scale) == Float.floatToIntBits(displayInfo.scale);
        }

        @Override
        public int hashCode() {
            return Objects.hash(xOffset, yOffset, zOffset, xRotation, yRotation, zRotation, scale);
        }

        @Override
        public String toString() {
            return "DisplayInfo[" +
                    "xOffset=" + xOffset + ", " +
                    "yOffset=" + yOffset + ", " +
                    "zOffset=" + zOffset + ", " +
                    "xRotation=" + xRotation + ", " +
                    "yRotation=" + yRotation + ", " +
                    "zRotation=" + zRotation + ", " +
                    "scale=" + scale + ']';
        }

    }

    public static final class ColorInfo {

        public static final ColorInfo NONE = new ColorInfo(0xFFFFFF, 0xFFFFFF);
        private final int base;
        private final int accent;

        public ColorInfo(int base, int accent) {
            this.base = base;
            this.accent = accent;
        }

        public JsonObject toJson() {
            JsonObject result = new JsonObject();
            if (base() != 0xFFFFFF) {
                result.add("base", serializeColor(base()));
            }
            if (accent() != base()) {
                result.add("accent", serializeColor(accent()));
            }
            return result;
        }

        private static JsonElement serializeColor(int color) {
            return new JsonPrimitive(String.format("#%06X", color));
        }

        public static ColorInfo fromJson(JsonObject object) {
            int base, accent;
            base = accent = 0xFFFFFF;
            if (object.has("base")) {
                base = accent = readColor(object.get("base"));
            }
            if (object.has("accent")) {
                accent = readColor(object.get("accent"));
            }

            return new ColorInfo(base, accent);
        }

        private static int readColor(JsonElement element) {
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                int red = JSONUtils.getAsInt(object, "red");
                int green = JSONUtils.getAsInt(object, "green");
                int blue = JSONUtils.getAsInt(object, "blue");
                return red << 16 | green << 8 | blue;
            } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                return parseColor(element.getAsString());
            } else {
                throw new JsonParseException(String.format("Expected color to be json object or string, got %s", element));
            }
        }

        private static int parseColor(String string) {
            if (string.startsWith("#")) {
                return Integer.parseInt(string.substring(1), 16);
            }
            throw new JsonParseException(String.format("Couldn't parse color string: %s", string));
        }

        public int base() {
            return base;
        }

        public int accent() {
            return accent;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            ColorInfo colorInfo = (ColorInfo) obj;
            return this.base == colorInfo.base &&
                    this.accent == colorInfo.accent;
        }

        @Override
        public int hashCode() {
            return Objects.hash(base, accent);
        }

        @Override
        public String toString() {
            return "ColorInfo[" +
                    "base=" + base + ", " +
                    "accent=" + accent + ']';
        }

    }

    public static class EntityInfo {

        private final EntityType<?> type;
        private final CompoundNBT nbt;
        private final boolean isAnimated;

        @Nullable
        private Entity entity;

        public EntityInfo(EntityType<?> type, CompoundNBT nbt, boolean isAnimated) {
            this.type = type;
            this.nbt = nbt;
            this.isAnimated = isAnimated;
        }

        @Nullable
        public EntityType<?> getType() {
            return type;
        }

        public CompoundNBT getTag() {
            return nbt;
        }

        public boolean isAnimated() {
            return isAnimated;
        }

        @Nullable
        public Entity getOrCreateEntity(World level) {
            if (entity == null || entity.level != level) {
                createEntity(level);
            }
            return entity;
        }

        private void createEntity(World level) {
            if (type == null) {
                return;
            }

            entity = type.create(level);
            if (entity != null) {
                entity.setId(0);
                entity.load(nbt);
                if (!nbt.hasUUID("UUID")) {
                    entity.setUUID(Util.NIL_UUID);
                }
            }
        }

        public JsonObject toJson() {
            JsonObject result = new JsonObject();
            // noinspection ConstantConditions
            result.addProperty("type", getType().getRegistryName().toString());
            if (!getTag().isEmpty()) {
                result.addProperty("nbt", getTag().toString());
            }
            if (isAnimated()) {
                result.addProperty("animated", isAnimated());
            }
            return result;
        }

        public static EntityInfo fromJson(JsonObject object) {
            ResourceLocation typeID = new ResourceLocation(JSONUtils.getAsString(object, "type"));
            if (!ForgeRegistries.ENTITIES.containsKey(typeID)) {
                throw new JsonParseException(String.format("Unknown entity type %s", typeID));
            }
            EntityType<?> type = ForgeRegistries.ENTITIES.getValue(typeID);
            CompoundNBT nbt = new CompoundNBT();
            if (object.has("nbt")) {
                JsonElement nbtElement = object.get("nbt");
                nbt = readNBT(nbtElement);
            }
            boolean isAnimated = false;
            if (object.has("animated")) {
                isAnimated = JSONUtils.getAsBoolean(object, "animated");
            }
            return new EntityInfo(type, nbt, isAnimated);
        }
    }
}
