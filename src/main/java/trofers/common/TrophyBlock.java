package trofers.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TrophyBlock extends Block {

    private final int height;
    private final VoxelShape shape;

    public TrophyBlock(Properties properties, int height) {
        super(properties);
        this.height = height;
        this.shape = createShape(height);
        registerDefaultState(
                defaultBlockState()
                        .setValue(BlockStateProperties.WATERLOGGED, false)
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
        );
    }

    public int getHeight() {
        return height;
    }

    public float getDefaultDisplayScale() {
        return (getHeight() - 2 - 2) / 4F;
    }

    private static VoxelShape createShape(int height) {
        int width = 2 * (height - 2);
        return VoxelShapes.or(
                centeredBox(width, 0, 2),
                centeredBox(width - 2, 2, height - 2),
                centeredBox(width, height - 2, height)
        );
    }

    private static VoxelShape centeredBox(int width, int minY, int maxY) {
        return Block.box(8 - width / 2D, minY, 8 - width / 2D, 8 + width / 2D, maxY, 8 + width / 2D);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return shape;
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
        if (player.isCreative()) {
            TileEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TrophyBlockEntity) {
                TrophyBlockEntity trophy = (TrophyBlockEntity) blockEntity;
                boolean usedItem = setColor(trophy, level, pos, player, hand, rayTrace)
                        || setName(trophy, level, pos, player, hand)
                        || setAnimation(trophy, level, pos, player, hand)
                        || setItem(trophy, level, pos, player, hand);

                if (usedItem) {
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    private boolean setColor(TrophyBlockEntity trophy, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof DyeItem)) {
            return false;
        }

        int color = ((DyeItem) stack.getItem()).getDyeColor().getColorValue();
        double height = rayTrace.getLocation().y - pos.getY();
        int index;

        if (height <= 2 / 16D) {
            index = 2;
        } else if (height >= (getHeight() - 2) / 16D) {
            index = 0;
        } else {
            index = 1;
        }

        if (trophy.getColor(index) == color) {
            return false;
        }

        trophy.setColor(index, color);
        level.playSound(player, pos, SoundEvents.SLIME_BLOCK_HIT, SoundCategory.BLOCKS, 1, 1);
        return true;
    }

    private boolean setName(TrophyBlockEntity trophy, World level, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() != Items.NAME_TAG || !stack.hasCustomHoverName()) {
            return false;
        }

        ITextComponent name = stack.getHoverName();
        if (!name.equals(trophy.getName())) {
            trophy.setName(name);
            level.playSound(player, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundCategory.BLOCKS, 1, 1);
        }
        return true;
    }

    private boolean setAnimation(TrophyBlockEntity trophy, World level, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == Items.STICK) {
            trophy.setAnimation(TrophyAnimation.values()[(trophy.getAnimation().ordinal() + 1) % TrophyAnimation.values().length]);
            level.playSound(player, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundCategory.BLOCKS, 1, 1);
            return true;
        }
        return false;
    }

    private boolean setItem(TrophyBlockEntity trophy, World level, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!(stack.isEmpty() && hand == Hand.OFF_HAND) && !ItemStack.matches(stack, trophy.getItem())) {
            trophy.setItem(stack.copy());
            SoundEvent soundEvent = stack.isEmpty() ? SoundEvents.ITEM_FRAME_REMOVE_ITEM : SoundEvents.ITEM_FRAME_ADD_ITEM;
            level.playSound(player, pos, soundEvent, SoundCategory.BLOCKS, 1, 1);
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader level, BlockPos pos, PlayerEntity player) {
        ItemStack stack = super.getPickBlock(state, target, level, pos, player);
        TileEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TrophyBlockEntity) {
            CompoundNBT tag = stack.getOrCreateTag();
            CompoundNBT blockEntityTag = tag.getCompound("BlockEntityTag");
            tag.put("BlockEntityTag", blockEntityTag);

            ((TrophyBlockEntity) blockEntity).saveTrophy(blockEntityTag);
        }
        return stack;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        boolean isWaterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite())
                .setValue(BlockStateProperties.WATERLOGGED, isWaterlogged);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld level, BlockPos pos, BlockPos facingPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, pos, facingPos);
    }

    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader level) {
        return new TrophyBlockEntity();
    }
}
