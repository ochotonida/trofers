package trofers.common.trophy.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.client.Minecraft;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyAnimation;

public class TrophyBlockEntityRenderer implements BlockEntityRenderer<TrophyBlockEntity> {

    @SuppressWarnings("unused")
    public TrophyBlockEntityRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public boolean shouldRenderOffScreen(TrophyBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 128;
    }

    @Override
    public void render(TrophyBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0, 0.5);

        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));

        Trophy trophy = blockEntity.getTrophy();

        if (trophy != null && Minecraft.getInstance().player != null) {
            float ticks = (Minecraft.getInstance().player.tickCount + partialTicks + blockEntity.getAnimationOffset());
            render(trophy, ticks, blockEntity.getDisplayHeight(), poseStack, multiBufferSource, light, overlay);
        }
        poseStack.popPose();
    }

    public static void render(Trophy trophy, float ticks, double displayHeight, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        double animationProgress = ticks * trophy.getAnimationSpeed();

        poseStack.translate(0, displayHeight / 16D, 0);

        if (!trophy.getItem().isEmpty()) {
            renderItem(trophy, animationProgress, poseStack, multiBufferSource, light, overlay);
        }
        if (trophy.hasEntity()) {
            renderEntity(trophy, ticks, animationProgress, poseStack, multiBufferSource, light);
        }
    }

    private static void renderItem(Trophy trophy, double animationProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        poseStack.pushPose();

        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = renderer.getModel(trophy.getItem(), Minecraft.getInstance().level, null, 0);

        float displayScale = (float) trophy.getDisplayScale();
        poseStack.scale(displayScale, displayScale, displayScale);

        poseStack.translate(0, 0.25, 0);

        if (!model.isGui3d()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
        applyRotation(animationProgress, trophy.getAnimation(), poseStack);

        Minecraft.getInstance().getItemRenderer().renderStatic(trophy.getItem(), ItemTransforms.TransformType.FIXED, light, overlay, poseStack, multiBufferSource, 0);

        poseStack.popPose();
    }

    private static void renderEntity(Trophy trophy, float ticks, double animationProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        if (Minecraft.getInstance().level == null) {
            return;
        }

        Entity entity = trophy.getOrCreateEntity(Minecraft.getInstance().level);
        if (entity == null) {
            return;
        }

        if (!trophy.shouldAnimateEntity()) {
            ticks = 0;
        }

        float displayScale = (float) trophy.getDisplayScale();
        poseStack.scale(displayScale, displayScale, displayScale);

        poseStack.translate(0, entity.getBbHeight() / 2, 0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        applyRotation(animationProgress, trophy.getAnimation(), poseStack);
        poseStack.translate(0, -entity.getBbHeight() / 2, 0);

        Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, ticks, poseStack, multiBufferSource, light);
    }

    private static void applyRotation(double animationProgress, TrophyAnimation animation, PoseStack poseStack) {
        if (animation == TrophyAnimation.SPINNING) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees((float) (animationProgress * 6)));
        } else if (animation == TrophyAnimation.TUMBLING) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees((float) (animationProgress * 6 + 45)));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees((float) (animationProgress * 4.5F + 45)));
            poseStack.mulPose(Vector3f.XP.rotationDegrees((float) (animationProgress * 2.25F + 45)));
        }
    }
}
