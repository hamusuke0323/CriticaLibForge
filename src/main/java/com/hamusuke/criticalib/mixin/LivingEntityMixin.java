package com.hamusuke.criticalib.mixin;

import com.hamusuke.criticalib.invoker.LivingEntityInvoker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityInvoker {
    private static final DataParameter<Boolean> CRIT_FLAG = EntityDataManager.defineId(LivingEntity.class, DataSerializers.BOOLEAN);

    LivingEntityMixin(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void entityInit(CallbackInfo ci) {
        this.entityData.define(CRIT_FLAG, false);
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void attackEntityFromFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!this.level.isClientSide && source instanceof IndirectEntityDamageSource && source.getEntity() instanceof LivingEntityInvoker && source.getDirectEntity() instanceof AbstractArrowEntity) {
            ((LivingEntityInvoker) source.getEntity()).setCritical(((AbstractArrowEntity) source.getDirectEntity()).isCritArrow());
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
        return this.entityData.get(CRIT_FLAG);
    }

    @Override
    public void setCritical(boolean flag) {
        this.entityData.set(CRIT_FLAG, flag);
    }
}
