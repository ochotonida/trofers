package trofers.forge.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

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
        return box(8 - width / 2D, 0, 8 - width / 2D, 8 + width / 2D, 2, 8 + width / 2D);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }
}
