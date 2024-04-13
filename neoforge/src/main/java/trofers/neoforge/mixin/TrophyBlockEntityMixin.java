package trofers.neoforge.mixin;

import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.extensions.IBlockEntityRendererExtension;
import org.spongepowered.asm.mixin.Mixin;
import trofers.block.entity.TrophyBlockEntity;
import trofers.block.entity.TrophyBlockEntityRenderer;

@Mixin(TrophyBlockEntityRenderer.class)
public abstract class TrophyBlockEntityMixin implements IBlockEntityRendererExtension<TrophyBlockEntity> {


    @Override
    public AABB getRenderBoundingBox(TrophyBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos().offset(-1, 0, -1).getCenter(), blockEntity.getBlockPos().offset(1, 16, 1).getCenter());
    }
}
