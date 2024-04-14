package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

public record Animation(Type type, double speed) {

    public static final Animation STATIC = new Animation(Type.FIXED, 1);

    public static final Codec<Type> TYPE_CODEC = StringRepresentable.fromEnum(Type::values);
    public static final Codec<Animation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(TYPE_CODEC, "type", Type.FIXED).forGetter(Animation::type),
            ExtraCodecs.strictOptionalField(Codec.DOUBLE, "speed", 1D).forGetter(Animation::speed)
    ).apply(instance, Animation::new));

    public enum Type implements StringRepresentable {
        FIXED("fixed"),
        SPINNING("spinning"),
        TUMBLING("tumbling");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
