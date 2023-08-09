package trofers.fabric.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import trofers.Trofers;
import trofers.registry.ModBlocks;
import trofers.trophy.Trophy;
import trofers.trophy.TrophyManager;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    protected Player lastHurtByPlayer;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
        throw new IllegalStateException();
    }

    @Inject(method = "dropFromLootTable", at = @At("TAIL"))
    protected void dropFromLootTable(DamageSource damageSource, boolean bl, CallbackInfo ci) {
        ResourceLocation type = BuiltInRegistries.ENTITY_TYPE.getKey(getType());
        if (!type.getNamespace().equals("minecraft")) {
            return;
        }

        ResourceLocation trophyId = Trofers.id(type.getPath());
        Trophy trophy = TrophyManager.get(trophyId);

        if (trophy != null && lastHurtByPlayer != null && random.nextDouble() < Trofers.CONFIG.general.getTrophyChance()) {
            ItemStack stack = new ItemStack(ModBlocks.SMALL_PLATE.get());
            stack.getOrCreateTagElement("BlockEntityTag").putString("Trophy", trophyId.toString());
            spawnAtLocation(stack);
        }
    }
}
