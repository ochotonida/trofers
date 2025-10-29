package trofers.fabric.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import trofers.registry.ModRegistries;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    protected Player lastHurtByPlayer;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
        throw new IllegalStateException();
    }

    @Inject(method = "dropFromLootTable", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    protected void dropFromLootTable(DamageSource damageSource, boolean bl, CallbackInfo ci) {
        LootParams.Builder builder = (new LootParams.Builder((ServerLevel)this.level())).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.DAMAGE_SOURCE, damageSource).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity());
        if (bl && this.lastHurtByPlayer != null) {
            builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
        }
        LootParams lootParams = builder.create(LootContextParamSets.ENTITY);
        LootContext context = (new LootContext.Builder(lootParams)).create(Optional.empty());
        ModRegistries.entityDrops().ifPresent(
                entityDrops -> entityDrops.forEach(entry -> entry.apply(this::spawnAtLocation, context))
        );
    }
}
