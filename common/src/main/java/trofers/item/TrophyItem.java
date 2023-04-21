package trofers.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import trofers.block.TrophyBlock;
import trofers.block.entity.TrophyBlockEntity;
import trofers.trophy.Trophy;

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

    @Override
    public Component getName(ItemStack stack) {
        Trophy trophy = Trophy.getTrophy(stack);
        if (trophy != null && trophy.name().isPresent()) {
            return trophy.name().get();
        }
        return super.getName(stack);
    }
}
