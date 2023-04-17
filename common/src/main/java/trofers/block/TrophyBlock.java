package trofers.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import trofers.Trofers;
import trofers.block.entity.TrophyBlockEntity;
import trofers.screen.TrophySelectionScreen;
import trofers.registry.ModBlockEntityTypes;
import trofers.trophy.Trophy;

import javax.annotation.Nullable;
import java.util.List;

public class TrophyBlock extends BaseEntityBlock {

    public static final String DESCRIPTION_ID = Util.makeDescriptionId("block", Trofers.id("trophy"));

    private final int height;
    private final VoxelShape shape;

    private TrophyBlock(Properties properties, int height, VoxelShape shape) {
        super(properties);
        this.height = height;
        this.shape = shape;
        registerDefaultState(
                defaultBlockState()
                        .setValue(BlockStateProperties.WATERLOGGED, false)
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
        );
    }

    public static TrophyBlock createPillarTrophy(Properties properties, int size) {
        return new TrophyBlock(properties, size, createPillarShape(size));
    }

    public static TrophyBlock createPlateTrophy(Properties properties, int size) {
        return new TrophyBlock(properties, 2, createPlateShape(size));
    }

    private static VoxelShape createPillarShape(int size) {
        int width = 2 * (size - 2);
        return Shapes.or(
                centeredBox(width, 0, 2),
                centeredBox(width - 2, 2, size - 2),
                centeredBox(width, size - 2, size)
        );
    }

    private static VoxelShape createPlateShape(int size) {
        int width = 2 * (size - 2);
        return box(8 - width / 2D, 0, 8 - width / 2D, 8 + width / 2D, 2, 8 + width / 2D);
    }

    private static VoxelShape centeredBox(int width, int minY, int maxY) {
        return box(8 - width / 2D, minY, 8 - width / 2D, 8 + width / 2D, maxY, 8 + width / 2D);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String getDescriptionId() {
        return DESCRIPTION_ID;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        Trophy trophy = Trophy.getTrophy(stack);
        if (trophy != null) {
            tooltip.addAll(trophy.tooltip());
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity
                && placer instanceof Player player
                && !level.isClientSide()
                && player.isCreative()) {
            blockEntity.removeCooldown();
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.WATERLOGGED);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
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
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, pos, facingPos);
    }

    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.TROPHY.get().create(pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack result = super.getCloneItemStack(level, pos, state);
        if (level.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity) {
            if (blockEntity.getTrophyID() != null) {
                result.getOrCreateTagElement("BlockEntityTag").putString("Trophy", blockEntity.getTrophyID().toString());
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isCreative()) {
            if (level.isClientSide()) {
                TrophySelectionScreen.open(state, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        if (level.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity) {
            if (blockEntity.applyEffect(player, hand)) {
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, ModBlockEntityTypes.TROPHY.get(), TrophyBlockEntity.TICKER);
    }
}
