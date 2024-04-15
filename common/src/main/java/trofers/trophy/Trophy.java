package trofers.trophy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import trofers.data.ModCodecs;
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
            ModCodecs.optionalField("name", ComponentSerialization.CODEC).forGetter(Trophy::name),
            ModCodecs.defaultField("tooltip", List.of(),
                    ModCodecs.<List<Component>>withAlternative(
                            ComponentSerialization.CODEC.xmap(List::of, list -> list.get(0)),
                            ModCodecs.list(ComponentSerialization.CODEC)
                    )
            ).forGetter(Trophy::tooltip),
            ModCodecs.defaultField("display", DisplayInfo.NONE, DisplayInfo.CODEC).forGetter(Trophy::display),
            ModCodecs.defaultField("animation", Animation.STATIC, Animation.CODEC).forGetter(Trophy::animation),
            ModCodecs.defaultField("item", ItemStack.EMPTY, ItemStack.CODEC).forGetter(Trophy::item),
            ModCodecs.optionalField("entity", EntityInfo.CODEC).forGetter(Trophy::entity),
            ModCodecs.defaultField("colors", ColorInfo.NONE, ColorInfo.CODEC).forGetter(Trophy::colors),
            ModCodecs.defaultField("effects", EffectInfo.NONE, EffectInfo.CODEC).forGetter(Trophy::effects),
            ModCodecs.defaultField("is_hidden", false, Codec.BOOL).forGetter(Trophy::isHidden)
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
