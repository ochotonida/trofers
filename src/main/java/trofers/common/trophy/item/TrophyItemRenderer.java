package trofers.common.trophy.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.block.TrophyBlock;
import trofers.common.trophy.block.TrophyBlockEntityRenderer;

public class TrophyItemRenderer extends BlockEntityWithoutLevelRenderer {

    @SuppressWarnings("ConstantConditions")
    public TrophyItemRenderer() {
        super(null, null);
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) { }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemTransforms.TransformType transformType,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int light,
            int overlay
    ) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.5, 0.5);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(((BlockItem) stack.getItem()).getBlock().defaultBlockState());
        renderer.render(stack, ItemTransforms.TransformType.NONE, false, poseStack, multiBufferSource, light, overlay, model);
        poseStack.translate(0, -0.5, 0);

        renderTrophy(stack, poseStack, multiBufferSource, light, overlay);

        poseStack.popPose();
    }

    private void renderTrophy(
            ItemStack stack,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
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
