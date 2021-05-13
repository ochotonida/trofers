package trofers.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import trofers.common.TrophyBlock;
import trofers.common.TrophyItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.CapabilityItemHandler;

public class TrophyItemRenderer extends ItemStackTileEntityRenderer {

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
        matrixStack.pushPose();

        matrixStack.translate(0.5, 0.5, 0.5);

        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        IBakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(((BlockItem) stack.getItem()).getBlock().defaultBlockState());
        renderer.render(stack, ItemCameraTransforms.TransformType.NONE, false, matrixStack, buffer, light, overlay, model);

        matrixStack.translate(0, -0.5, 0);

        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            if (handler.getSlots() == 1 && stack.getTag() != null && stack.getItem() instanceof TrophyItem) {
                TrophyBlock block = (TrophyBlock) ((TrophyItem) stack.getItem()).getBlock();
                CompoundNBT tag = stack.getTag().getCompound("BlockEntityTag");
                float displayHeight = tag.getFloat("DisplayHeight") + block.getHeight();
                float displayScale = tag.contains("DisplayScale") ? tag.getFloat("DisplayScale") : (block.getHeight() - 2 - 2) / 4F;
                TrophyBlockEntityRenderer.render(handler.getStackInSlot(0), displayHeight, displayScale, matrixStack, buffer, light, overlay);
            }
        });

        matrixStack.popPose();
    }
}
