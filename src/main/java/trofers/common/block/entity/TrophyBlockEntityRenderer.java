package trofers.common.block.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import trofers.common.trophy.Animation;
import trofers.common.trophy.Trophy;

public class TrophyBlockEntityRenderer extends TileEntityRenderer<TrophyBlockEntity> {

    public TrophyBlockEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public boolean shouldRenderOffScreen(TrophyBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(TrophyBlockEntity blockEntity, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer multiBufferSource, int light, int overlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0, 0.5);

        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));

        Trophy trophy = blockEntity.getTrophy();

        if (trophy != null && Minecraft.getInstance().player != null) {
            float ticks = (Minecraft.getInstance().player.tickCount + partialTicks + blockEntity.getAnimationOffset());
            render(trophy, ticks, blockEntity.getTrophyHeight(), poseStack, multiBufferSource, light, overlay);
        }
        poseStack.popPose();
    }

    public static void render(Trophy trophy, float ticks, int trophyHeight, MatrixStack poseStack, IRenderTypeBuffer multiBufferSource, int light, int overlay) {
        renderItem(trophy, ticks, trophyHeight, poseStack, multiBufferSource, light, overlay);
        renderEntity(trophy, ticks, trophyHeight, poseStack, multiBufferSource, light);
    }

    private static void renderItem(Trophy trophy, float ticks, int trophyHeight, MatrixStack poseStack, IRenderTypeBuffer multiBufferSource, int light, int overlay) {
        poseStack.pushPose();

        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        IBakedModel model = renderer.getModel(trophy.item(), Minecraft.getInstance().level, null);

        float yOffset = 0.25F;
        translateRotate(poseStack, trophy, trophyHeight, yOffset, ticks);

        poseStack.translate(0, 0.25, 0);

        if (!model.isGui3d()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }

        Minecraft.getInstance().getItemRenderer().renderStatic(trophy.item(), ItemCameraTransforms.TransformType.FIXED, light, overlay, poseStack, multiBufferSource);

        poseStack.popPose();
    }

    private static void renderEntity(Trophy trophy, float ticks, int trophyHeight, MatrixStack poseStack, IRenderTypeBuffer multiBufferSource, int light) {
        if (Minecraft.getInstance().level == null || trophy.entity() == null) {
            return;
        }
        Entity entity = trophy.entity().getOrCreateEntity(Minecraft.getInstance().level);
        if (entity == null) {
            return;
        }
        poseStack.pushPose();

        float entityHeight = entity.getBbHeight();
        translateRotate(poseStack, trophy, trophyHeight, entityHeight / 2, ticks);

        if (!trophy.entity().isAnimated()) {
            ticks = 0;
        }

        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));

        Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, ticks, poseStack, multiBufferSource, light);
        poseStack.popPose();
    }

    private static void translateRotate(MatrixStack poseStack, Trophy trophy, int trophyHeight, float yRotationOffset, float ticks) {
        yRotationOffset *= trophy.display().scale();
        float animationProgress = 6 * ticks * trophy.animation().speed();

        poseStack.translate(0, (trophyHeight + trophy.display().yOffset()) / 16D, 0);
        poseStack.translate(0, yRotationOffset, 0);
        if (trophy.animation().type() == Animation.Type.SPINNING) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(animationProgress));
        } else if (trophy.animation().type() == Animation.Type.TUMBLING) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(animationProgress));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(animationProgress * 0.8F));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(animationProgress * 0.6F));
        }
        poseStack.translate(0, -yRotationOffset, 0);

        poseStack.translate(trophy.display().xOffset() / 16D, 0, 0);
        poseStack.translate(0, 0, trophy.display().zOffset() / 16D);

        poseStack.mulPose(Vector3f.XP.rotationDegrees(trophy.display().xRotation()));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(trophy.display().yRotation()));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(trophy.display().zRotation()));

        float scale = trophy.display().scale();
        poseStack.scale(scale, scale, scale);
    }
}
