package trofers.fabric.mixin;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import trofers.Trofers;
import trofers.trophy.TrophyManager;

@Mixin(TrophyManager.class)
public abstract class TrophyManagerMixin implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return Trofers.id("trophy_manager");
    }
}
