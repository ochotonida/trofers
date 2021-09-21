package trofers.common.trophy.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.block.TrophyBlock;
import trofers.common.trophy.block.TrophyBlockEntityRenderer;

public class TrophyItemRenderer extends ItemStackTileEntityRenderer {

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemCameraTransforms.TransformType transformType,
            MatrixStack poseStack,
            IRenderTypeBuffer multiBufferSource,
            int light,
            int overlay
    ) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.5, 0.5);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        IBakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(((BlockItem) stack.getItem()).getBlock().defaultBlockState());
        renderer.render(stack, ItemCameraTransforms.TransformType.NONE, false, poseStack, multiBufferSource, light, overlay, model);
        poseStack.translate(0, -0.5, 0);

        renderTrophy(stack, poseStack, multiBufferSource, light, overlay);

        poseStack.popPose();
    }

    private void renderTrophy(
            ItemStack stack,
            MatrixStack poseStack,
            IRenderTypeBuffer multiBufferSource,
            int light,
            int overlay
    ) {
        Trophy trophy = Trophy.getTrophy(stack);

        if (trophy == null || Minecraft.getInstance().player == null) {
            return;
        }

        float partialTicks = Minecraft.getInstance().getFrameTime() * (Minecraft.getInstance().isPaused() ? 0 : 1);
        float ticks = (Minecraft.getInstance().player.tickCount + partialTicks);

        int trophyHeight = ((TrophyBlock) ((BlockItem) stack.getItem()).getBlock()).getHeight();
        TrophyBlockEntityRenderer.render(trophy, ticks, trophyHeight, poseStack, multiBufferSource, light, overlay);
    }
}
