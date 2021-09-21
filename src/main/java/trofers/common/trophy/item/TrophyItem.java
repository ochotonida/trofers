package trofers.common.trophy.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.block.TrophyBlock;
import trofers.common.trophy.block.TrophyBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TrophyItem extends BlockItem {

    public TrophyItem(TrophyBlock block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        if (super.placeBlock(context, state)) {
            TileEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            CompoundNBT tag = context.getItemInHand().getTag();
            if (blockEntity instanceof TrophyBlockEntity trophy && tag != null) {
                trophy.loadTrophy(tag.getCompound("BlockEntityTag"));
            }
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public ITextComponent getName(ItemStack stack) {
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
}
