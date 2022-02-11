package com.hamusuke.criticalib;

import com.hamusuke.criticalib.invoker.LivingEntityInvoker;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(CriticaLib.MOD_ID)
public class CriticaLib {
    public static final String MOD_ID = "criticalib";

    public CriticaLib() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onCritHit(final CriticalHitEvent event) {
        if (!event.getPlayer().level.isClientSide) {
            LivingEntityInvoker invoker = (LivingEntityInvoker) event.getPlayer();
            if (event.getResult() == Event.Result.ALLOW || (event.getResult() == Event.Result.DEFAULT && event.isVanillaCritical())) {
                invoker.setCritical(true);
            }
        }
    }
}
