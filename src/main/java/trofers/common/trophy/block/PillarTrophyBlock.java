package trofers.common.trophy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class PillarTrophyBlock extends TrophyBlock {

    private final VoxelShape shape;

    public PillarTrophyBlock(Properties properties, int size) {
        super(properties, size);
        this.shape = createShape(size);
    }

    public int getHeight() {
        return getSize();
    }

    private VoxelShape createShape(int size) {
        int width = 2 * (size - 2);
        return VoxelShapes.or(
                centeredBox(width, 0, 2),
                centeredBox(width - 2, 2, size - 2),
                centeredBox(width, size - 2, size)
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
}
