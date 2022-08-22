package com.hamusuke.criticalib;

import com.hamusuke.criticalib.invoker.LivingEntityInvoker;
import com.hamusuke.criticalib.network.Network;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CriticaLib.MOD_ID)
public class CriticaLib {
    public static final String MOD_ID = "criticalib";

    public CriticaLib() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> Network.loadClass());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onCritHit(final CriticalHitEvent event) {
        Player player = event.getEntity();
        if (!player.level.isClientSide && player instanceof LivingEntityInvoker invoker) {
            if (event.getResult() == Event.Result.ALLOW || (event.getResult() == Event.Result.DEFAULT && event.isVanillaCritical())) {
                invoker.setCritical(true);
            }
        }
    }
}
