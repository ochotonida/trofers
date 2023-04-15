package trofers.forge.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import trofers.item.TrophyItemRenderer;

public class TrophyItemRendererForge extends BlockEntityWithoutLevelRenderer {

    @SuppressWarnings("ConstantConditions")
    public TrophyItemRendererForge() {
        super(null, null);
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {

    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        TrophyItemRenderer.render(stack, displayContext, poseStack, multiBufferSource, light, overlay);
    }
}
