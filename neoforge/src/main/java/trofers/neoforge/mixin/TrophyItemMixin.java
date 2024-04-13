package trofers.neoforge.mixin;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import trofers.neoforge.item.TrophyItemRendererNeoForge;
import trofers.item.TrophyItem;

import java.util.function.Consumer;

@Mixin(TrophyItem.class)
public abstract class TrophyItemMixin extends Item {

    public TrophyItemMixin(Properties properties) {
        super(properties);
        throw new IllegalStateException();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final TrophyItemRendererNeoForge renderer = new TrophyItemRendererNeoForge();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}
