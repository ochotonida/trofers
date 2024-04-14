package trofers.trophy.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;

public record Animation(Type type, double speed) {

    public static final Animation STATIC = new Animation(Type.FIXED, 1);

    public static final Codec<Type> TYPE_CODEC = StringRepresentable.fromEnum(Type::values);
    public static final Codec<Animation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TYPE_CODEC.optionalFieldOf("type", Type.FIXED).forGetter(Animation::type),
            Codec.DOUBLE.optionalFieldOf("speed", 1D).forGetter(Animation::speed)
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
