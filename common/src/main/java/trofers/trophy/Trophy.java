package trofers.trophy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import trofers.data.ModCodecs;
import trofers.registry.ModDataComponents;
import trofers.registry.ModRegistries;
import trofers.trophy.builder.ItemTrophyBuilder;
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

    public static final Trophy EMPTY = new ItemTrophyBuilder().setItem(Items.AIR).setHidden().build();

    public static final MapCodec<Trophy> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ModCodecs.optionalField("name", ComponentSerialization.CODEC).forGetter(Trophy::name),
            ModCodecs.defaultField("tooltip", List.of(),
                    ModCodecs.<List<Component>>withAlternative(
                            ComponentSerialization.CODEC.xmap(List::of, List::getFirst),
                            ModCodecs.list(ComponentSerialization.CODEC)
                    )
            ).forGetter(Trophy::tooltip),
            ModCodecs.defaultField("display", DisplayInfo.NONE, DisplayInfo.CODEC).forGetter(Trophy::display),
            ModCodecs.defaultField("animation", Animation.STATIC, Animation.CODEC).forGetter(Trophy::animation),
            ModCodecs.defaultField("item", ItemStack.EMPTY, ModCodecs.ITEM_STACK).forGetter(Trophy::item),
            ModCodecs.optionalField("entity", EntityInfo.CODEC).forGetter(Trophy::entity),
            ModCodecs.defaultField("colors", ColorInfo.NONE, ColorInfo.CODEC).forGetter(Trophy::colors),
            ModCodecs.defaultField("effects", EffectInfo.NONE, EffectInfo.CODEC).forGetter(Trophy::effects),
            ModCodecs.defaultField("is_hidden", false, Codec.BOOL).forGetter(Trophy::isHidden)
    ).apply(instance, Trophy::new));

    public static final Codec<Trophy> CODEC = MAP_CODEC.codec();

    @Nullable
    public static Trophy getTrophy(ItemStack stack) {
        return ModRegistries.get(ModRegistries.TROPHIES, stack.get(ModDataComponents.TROPHY.get()));
    }

    public static ItemStack createItem(ItemLike trophyBase, ResourceLocation id) {
        ItemStack stack = new ItemStack(trophyBase);
        stack.set(ModDataComponents.TROPHY.get(), id);
        return stack;
    }
}
