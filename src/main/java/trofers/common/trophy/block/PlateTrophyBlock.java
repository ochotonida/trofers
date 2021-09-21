package trofers.common.trophy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class PlateTrophyBlock extends TrophyBlock {

    private final VoxelShape shape;

    public PlateTrophyBlock(Properties properties, int size) {
        super(properties, size);
        this.shape = createShape(size);
    }

    public int getHeight() {
        return 2;
    }

    private static VoxelShape createShape(int size) {
        int width = 2 * (size - 2);
        return Block.box(8 - width / 2D, 0, 8 - width / 2D, 8 + width / 2D, 2, 8 + width / 2D);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return shape;
    }
}
