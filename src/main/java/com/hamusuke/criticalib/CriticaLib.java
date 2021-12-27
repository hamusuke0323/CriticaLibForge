package com.hamusuke.criticalib;

import com.hamusuke.criticalib.invoker.EntityLivingBaseInvoker;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = CriticaLib.MOD_ID, name = CriticaLib.MOD_NAME, version = CriticaLib.VERSION)
public class CriticaLib {
    public static final String MOD_ID = "criticalib";
    public static final String MOD_NAME = "CriticaLib";
    public static final String VERSION = "1.0.0";

    public CriticaLib() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onCritHit(final CriticalHitEvent event) {
        EntityLivingBaseInvoker invoker = (EntityLivingBaseInvoker) event.getEntityPlayer();
        if (event.getResult() == Event.Result.ALLOW || (event.getResult() == Event.Result.DEFAULT && event.isVanillaCritical())) {
            invoker.setCritical(true);
        }
    }
}
