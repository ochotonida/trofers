package trofers.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
        return Shapes.or(
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }
}
