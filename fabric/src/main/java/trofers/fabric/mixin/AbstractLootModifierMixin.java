package trofers.fabric.mixin;

import io.github.fabricators_of_create.porting_lib.loot.IGlobalLootModifier;
import org.spongepowered.asm.mixin.Mixin;
import trofers.loot.AbstractLootModifier;

@Mixin(AbstractLootModifier.class)
public abstract class AbstractLootModifierMixin implements IGlobalLootModifier {

}
