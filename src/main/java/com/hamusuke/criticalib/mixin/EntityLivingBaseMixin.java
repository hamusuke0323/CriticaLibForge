package com.hamusuke.criticalib.mixin;

import com.hamusuke.criticalib.invoker.EntityLivingBaseInvoker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity implements EntityLivingBaseInvoker {
    private static final DataParameter<Boolean> CRIT_FLAG = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BOOLEAN);

    EntityLivingBaseMixin(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "entityInit", at = @At("TAIL"))
    private void entityInit(CallbackInfo ci) {
        this.dataManager.register(CRIT_FLAG, false);
    }

    @Inject(method = "attackEntityFrom", at = @At("HEAD"))
    private void attackEntityFromFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source instanceof EntityDamageSourceIndirect && source.getTrueSource() instanceof EntityLivingBaseInvoker && source.getImmediateSource() instanceof EntityArrow) {
            ((EntityLivingBaseInvoker) source.getTrueSource()).setCritical(((EntityArrow) source.getImmediateSource()).getIsCritical());
        }
    }

    @Inject(method = "attackEntityFrom", at = @At("RETURN"))
    private void attackEntityFrom$Return(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getTrueSource() instanceof EntityLivingBaseInvoker) {
            ((EntityLivingBaseInvoker) source.getTrueSource()).setCritical(false);
        }
    }

    @Override
    public boolean isCritical() {
        return this.dataManager.get(CRIT_FLAG);
    }

    @Override
    public void setCritical(boolean flag) {
        this.dataManager.set(CRIT_FLAG, flag);
    }
}
