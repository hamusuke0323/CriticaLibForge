package com.hamusuke.criticalib.network;

import com.hamusuke.criticalib.CriticaLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class Network {
    private static final String PROTOCOL = "1";
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(CriticaLib.MOD_ID, "main"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    static {
        INSTANCE.registerMessage(0, SyncCritFlagS2CPacket.class, SyncCritFlagS2CPacket::write, SyncCritFlagS2CPacket::new, SyncCritFlagS2CPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void loadClass() {
    }

    public static void send2C(Object MSG, ServerPlayer serverPlayer) {
        INSTANCE.sendTo(MSG, serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
