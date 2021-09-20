package trofers.common.trophy;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public record Trophy(
        ResourceLocation id,
        @Nullable
        Component name,
        DisplayInfo display,
        Animation animation,
        ItemStack item,
        @Nullable
        EntityInfo entity,
        ColorInfo colors,
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

    public JsonObject toJson() {
        JsonObject result = new JsonObject();

        result.add("name", Component.Serializer.toJsonTree(name()));

        if (!display().equals(Trophy.DisplayInfo.NONE)) {
            result.add("display", display().toJson());
        }

        if (animation().type() != Trophy.Animation.Type.FIXED) {
            result.add("animation", animation().toJson());
        }

        if (!item().isEmpty()) {
            result.add("item", serializeItem(item()));
        }

        if (entity() != null) {
            result.add("entity", entity().toJson());
        }

        if (!colors().equals(Trophy.ColorInfo.NONE)) {
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

        boolean isHidden = false;
        if (object.has("isHidden")) {
            isHidden = GsonHelper.getAsBoolean(object, "isHidden");
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

    private static CompoundTag readNBT(JsonElement element) {
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

    private static float readOptionalFloat(JsonObject object, String memberName, int defaultValue) {
        if (object.has(memberName)) {
            return GsonHelper.getAsFloat(object, memberName);
        }
        return defaultValue;
    }

    public record Animation(Type type, float speed) {

        public static final Animation STATIC = new Animation(Type.FIXED, 1);

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

        public enum Type {
            FIXED("fixed"),
            SPINNING("spinning");

            private final String name;

            Type(String name) {
                this.name = name;
            }

            public static Type fromJson(JsonElement element) {
                String name = GsonHelper.convertToString(element, "animation");
                for (Type animation : Type.values()) {
                    if (animation.name.equals(name)) {
                        return animation;
                    }
                }
                throw new JsonParseException(String.format("Invalid trophy animation type: %s", name));
            }
        }
    }

    public record DisplayInfo(float xOffset, float yOffset, float zOffset, float xRotation, float yRotation,
                              float zRotation, float scale) {

        public static final DisplayInfo NONE = new DisplayInfo(0, 0, 0, 0, 0, 0, 1);

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
                JsonObject offset = GsonHelper.getAsJsonObject(object, "offset");
                xOffset = readOptionalFloat(offset, "x", 0);
                yOffset = readOptionalFloat(offset, "y", 0);
                zOffset = readOptionalFloat(offset, "z", 0);
            }

            float xRotation, yRotation, zRotation;
            xRotation = yRotation = zRotation = 0;
            if (object.has("rotation")) {
                JsonObject rotation = GsonHelper.getAsJsonObject(object, "rotation");
                xRotation = readOptionalFloat(rotation, "x", 0);
                yRotation = readOptionalFloat(rotation, "y", 0);
                zRotation = readOptionalFloat(rotation, "z", 0);
            }

            float scale = readOptionalFloat(object, "scale", 1);

            return new DisplayInfo(xOffset, yOffset, zOffset, xRotation, yRotation, zRotation, scale);
        }
    }

    public record ColorInfo(int base, int accent) {

        public static final ColorInfo NONE = new ColorInfo(0xFFFFFF, 0xFFFFFF);

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

        private static JsonObject serializeColor(int color) {
            int red = color >> 16 & 255;
            int green = color >> 8 & 255;
            int blue = color & 255;
            JsonObject result = new JsonObject();
            result.addProperty("red", red);
            result.addProperty("green", green);
            result.addProperty("blue", blue);
            return result;
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
                int red = GsonHelper.getAsInt(object, "red");
                int green = GsonHelper.getAsInt(object, "green");
                int blue = GsonHelper.getAsInt(object, "blue");
                return red << 16 | green << 8 | blue;
            } else if (element.isJsonPrimitive()) {
                return element.getAsInt();
            } else {
                throw new JsonParseException(String.format("Expected color to be json object or integer, got %s", element));
            }
        }
    }

    public static class EntityInfo {

        private final EntityType<?> type;
        private final CompoundTag nbt;
        private final boolean isAnimated;

        @Nullable
        private Entity entity;

        public EntityInfo(EntityType<?> type, CompoundTag nbt, boolean isAnimated) {
            this.type = type;
            this.nbt = nbt;
            this.isAnimated = isAnimated;
        }

        @Nullable
        public EntityType<?> getType() {
            return type;
        }

        public CompoundTag getTag() {
            return nbt;
        }

        public boolean isAnimated() {
            return isAnimated;
        }

        @Nullable
        public Entity getOrCreateEntity(Level level) {
            if (entity == null || entity.level != level) {
                createEntity(level);
            }
            return entity;
        }

        private void createEntity(Level level) {
            if (type == null) {
                return;
            }

            entity = type.create(level);
            if (entity != null) {
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
            ResourceLocation typeID = new ResourceLocation(GsonHelper.getAsString(object, "type"));
            if (!ForgeRegistries.ENTITIES.containsKey(typeID)) {
                throw new JsonParseException(String.format("Unknown entity type %s", typeID));
            }
            EntityType<?> type = ForgeRegistries.ENTITIES.getValue(typeID);
            CompoundTag nbt = new CompoundTag();
            if (object.has("nbt")) {
                JsonElement nbtElement = object.get("nbt");
                nbt = readNBT(nbtElement);
            }
            boolean isAnimated = false;
            if (object.has("animated")) {
                isAnimated = GsonHelper.getAsBoolean(object, "animated");
            }
            return new EntityInfo(type, nbt, isAnimated);
        }
    }
}
