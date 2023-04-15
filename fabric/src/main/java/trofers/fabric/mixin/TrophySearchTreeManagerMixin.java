package trofers.fabric.mixin;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import trofers.Trofers;
import trofers.block.entity.TrophySearchTreeManager;

@Mixin(TrophySearchTreeManager.class)
public abstract class TrophySearchTreeManagerMixin implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(Trofers.MOD_ID, "trophy_search_tree_manager");
    }
}
