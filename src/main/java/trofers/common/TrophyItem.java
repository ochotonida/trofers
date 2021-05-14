package trofers.common;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
    public String getDescriptionId() {
        return "item.memorabilia.empty_trophy";
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            // noinspection ConstantConditions
            if (tag.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT blockEntityTag = tag.getCompound("BlockEntityTag");
                if (blockEntityTag.contains("Name", Constants.NBT.TAG_STRING)) {
                    return new StringTextComponent(blockEntityTag.getString("Name"));
                }
            }
        }
        // noinspection ConstantConditions
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (handler instanceof TrophyItemHandler) {
            ItemStack displayStack = ((TrophyItemHandler) handler).getItem();
            if (!displayStack.isEmpty()) {
                ITextComponent name;
                if (displayStack.hasCustomHoverName()) {
                    name = displayStack.getHoverName();
                } else {
                    name = displayStack.getItem().getName(displayStack);
                }
                return new TranslationTextComponent("item.memorabilia.trophy", name.getString());
            }
        }
        return super.getName(stack);
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        if (super.placeBlock(context, state)) {
            TileEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            CompoundNBT tag = context.getItemInHand().getTag();
            if (blockEntity instanceof TrophyBlockEntity && tag != null) {
                ((TrophyBlockEntity) blockEntity).loadTrophy(tag.getCompound("BlockEntityTag"), state);
            }
            return true;
        }
        return false;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT compoundNBT) {
        return new ICapabilityProvider() {

            private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

            private IItemHandler createHandler() {
                ItemStackHandler handler = new TrophyItemHandler();
                if (stack.hasTag()) {
                    CompoundNBT tag = stack.getTag();
                    // noinspection ConstantConditions
                    if (tag.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
                        CompoundNBT blockEntityTag = tag.getCompound("BlockEntityTag");
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
        };
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
        public CompoundNBT serializeNBT() {
            return getItem().serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT tag) {
            stacks.set(0, ItemStack.of(tag));
            onLoad();
        }
    }
}
