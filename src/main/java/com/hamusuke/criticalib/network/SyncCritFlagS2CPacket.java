package com.hamusuke.criticalib.network;

import com.hamusuke.criticalib.invoker.LivingEntityInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncCritFlagS2CPacket {
    private final int id;
    private final boolean flag;

    public SyncCritFlagS2CPacket(int id, boolean flag) {
        this.id = id;
        this.flag = flag;
    }

    public SyncCritFlagS2CPacket(FriendlyByteBuf byteBuf) {
        this.id = byteBuf.readVarInt();
        this.flag = byteBuf.readBoolean();
    }

    public void write(FriendlyByteBuf byteBuf) {
        byteBuf.writeVarInt(this.id);
        byteBuf.writeBoolean(this.flag);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.level.getEntity(this.id) instanceof LivingEntityInvoker invoker) {
                invoker.setCritical(this.flag);
            }

            ctx.get().setPacketHandled(true);
        });
    }
}
