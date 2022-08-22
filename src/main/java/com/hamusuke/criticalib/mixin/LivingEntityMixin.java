package com.hamusuke.criticalib.mixin;

import com.hamusuke.criticalib.invoker.LivingEntityInvoker;
import com.hamusuke.criticalib.network.Network;
import com.hamusuke.criticalib.network.SyncCritFlagS2CPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityInvoker {
    private boolean crit;

    LivingEntityMixin(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void attackEntityFromFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!this.level.isClientSide && source instanceof IndirectEntityDamageSource && source.getEntity() instanceof LivingEntityInvoker && source.getDirectEntity() instanceof AbstractArrow) {
            ((LivingEntityInvoker) source.getEntity()).setCritical(((AbstractArrow) source.getDirectEntity()).isCritArrow());
        }
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void attackEntityFrom$Return(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!this.level.isClientSide && source.getEntity() instanceof LivingEntityInvoker) {
            ((LivingEntityInvoker) source.getEntity()).setCritical(false);
        }
    }

    @Override
    public boolean isCritical() {
        return this.crit;
    }

    @Override
    public void setCritical(boolean flag) {
        this.crit = flag;

        if (!this.level.isClientSide) {
            SyncCritFlagS2CPacket packet = new SyncCritFlagS2CPacket(this.getId(), flag);
            this.getServer().getPlayerList().getPlayers().forEach(serverPlayer -> Network.send2C(packet, serverPlayer));
        }
    }
}
