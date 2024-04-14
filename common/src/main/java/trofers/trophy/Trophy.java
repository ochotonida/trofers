package trofers.trophy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import trofers.registry.ModResourceLoaders;
import trofers.trophy.components.*;

import java.util.List;
import java.util.Optional;

public record Trophy(
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

    public static final Codec<Trophy> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(ComponentSerialization.CODEC, "name").forGetter(Trophy::name),
            ExtraCodecs.strictOptionalField(
                    ExtraCodecs.<List<Component>>withAlternative(
                            ComponentSerialization.CODEC.xmap(List::of, list -> list.get(0)),
                            ComponentSerialization.CODEC.listOf()
                    ), "tooltip", List.of()
            ).forGetter(Trophy::tooltip),
            ExtraCodecs.strictOptionalField(DisplayInfo.CODEC, "display", DisplayInfo.NONE).forGetter(Trophy::display),
            ExtraCodecs.strictOptionalField(Animation.CODEC, "animation", Animation.STATIC).forGetter(Trophy::animation),
            ExtraCodecs.strictOptionalField(ItemStack.CODEC, "item", ItemStack.EMPTY).forGetter(Trophy::item),
            ExtraCodecs.strictOptionalField(EntityInfo.CODEC, "entity").forGetter(Trophy::entity),
            ExtraCodecs.strictOptionalField(ColorInfo.CODEC, "colors", ColorInfo.NONE).forGetter(Trophy::colors),
            ExtraCodecs.strictOptionalField(EffectInfo.CODEC, "effects", EffectInfo.NONE).forGetter(Trophy::effects),
            ExtraCodecs.strictOptionalField(Codec.BOOL, "is_hidden", false).forGetter(Trophy::isHidden)
    ).apply(instance, Trophy::new));

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

    public static ItemStack createItem(ItemLike trophyBase, ResourceLocation id) {
        ItemStack stack = new ItemStack(trophyBase);
        stack.getOrCreateTagElement("BlockEntityTag").putString("Trophy", id.toString());
        return stack;
    }
}
