package trofers.common;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

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
            }
            return true;
        }
        return false;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag tag) {
        return new TrophyItemHandlerProvider(stack);
    }

    private static class TrophyItemHandlerProvider implements ICapabilityProvider {

        private final ItemStack stack;
        private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

        private TrophyItemHandlerProvider(ItemStack stack) {
            this.stack = stack;
        }

        private IItemHandler createHandler() {
            ItemStackHandler handler = new TrophyItemHandler();
            if (stack.hasTag()) {
                CompoundTag tag = stack.getTag();
                // noinspection ConstantConditions
                if (tag.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
                    CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");
                    if (blockEntityTag.contains("Item", Constants.NBT.TAG_COMPOUND)) {
                        handler.deserializeNBT(blockEntityTag.getCompound("Item"));
                    }
                }
            }
            return handler;
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                return handler.cast();
            }
            return LazyOptional.empty();
        }
    }

    private static class TrophyItemHandler extends ItemStackHandler {

        private TrophyItemHandler() {
            super(1);
        }

        private ItemStack getItem() {
            return stacks.get(0);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public CompoundTag serializeNBT() {
            return getItem().serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            stacks.set(0, ItemStack.of(tag));
            onLoad();
        }
    }
}
