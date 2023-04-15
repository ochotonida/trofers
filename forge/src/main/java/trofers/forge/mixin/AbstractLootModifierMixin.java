package trofers.forge.mixin;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import org.spongepowered.asm.mixin.Mixin;
import trofers.loot.AbstractLootModifier;

@Mixin(AbstractLootModifier.class)
public abstract class AbstractLootModifierMixin implements IGlobalLootModifier {

}
