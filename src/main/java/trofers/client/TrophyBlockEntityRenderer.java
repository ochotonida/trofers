package trofers.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import trofers.common.TrophyBlockEntity;
import net.minecraft.client.Minecraft;
import trofers.common.TrophyAnimation;

public class TrophyBlockEntityRenderer implements BlockEntityRenderer<TrophyBlockEntity> {

    @Override
    public void render(TrophyBlockEntity trophy, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0, 0.5);

        Direction direction = trophy.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-direction.toYRot()));



        if (Minecraft.getInstance().player != null) {
            float animationProgress = (Minecraft.getInstance().player.tickCount + partialTicks) * trophy.getAnimationSpeed() + trophy.getAnimationOffset();
            render(trophy.getItem(), animationProgress, trophy.getAnimation(), trophy.getDisplayHeight(), trophy.getDisplayScale(), poseStack, multiBufferSource, light, overlay);
        }
        poseStack.popPose();
    }

    protected static void render(ItemStack displayItem, float animationProgress, TrophyAnimation animation, float displayHeight, float displayScale, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = renderer.getModel(displayItem, Minecraft.getInstance().level, null, 0);

        poseStack.translate(0, displayHeight / 16D + 0.25, 0);

        poseStack.translate(0, -0.25, 0);
        poseStack.scale(displayScale, displayScale, displayScale);
        if (!model.isGui3d()) {
            poseStack.scale(4/3F, 4/3F, 4/3F);
        }
        poseStack.translate(0, 0.25, 0);
        if (!model.isGui3d()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
        applyRotation(animationProgress, animation, poseStack);

        Minecraft.getInstance().getItemRenderer().renderStatic(displayItem, ItemTransforms.TransformType.FIXED, light, overlay, poseStack, buffer, 0);
    }

    private static void applyRotation(float animationProgress, TrophyAnimation animation, PoseStack poseStack) {
        if (animation == TrophyAnimation.SPINNING) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(animationProgress * 6));
        } else if (animation == TrophyAnimation.TUMBLING) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(animationProgress * 6 + 45));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(animationProgress * 4.5F + 45));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(animationProgress * 2.25F + 45));
        }
    }
}
