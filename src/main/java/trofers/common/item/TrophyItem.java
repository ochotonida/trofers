package trofers.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.IItemRenderProperties;
import trofers.common.block.TrophyBlock;
import trofers.common.block.entity.TrophyBlockEntity;
import trofers.common.trophy.Trophy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TrophyItem extends BlockItem {

    public TrophyItem(TrophyBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        if (super.placeBlock(context, state)) {
            BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            CompoundTag tag = context.getItemInHand().getTag();
            if (blockEntity instanceof TrophyBlockEntity trophy && tag != null) {
                trophy.loadTrophy(tag.getCompound("BlockEntityTag"));
                if (!context.getLevel().isClientSide()) {
                    trophy.restartRewardCooldown();
                }
            }
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public Component getName(ItemStack stack) {
        Trophy trophy = Trophy.getTrophy(stack);
        if (trophy != null && trophy.name() != null) {
            return trophy.name();
        }
        return super.getName(stack);
    }

    @Nullable
    @Override
    public String getCreatorModId(ItemStack stack) {
        Trophy trophy = Trophy.getTrophy(stack);
        if (trophy != null) {
            return trophy.id().getNamespace();
        }
        return super.getCreatorModId(stack);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            private final BlockEntityWithoutLevelRenderer renderer = new TrophyItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }
}
