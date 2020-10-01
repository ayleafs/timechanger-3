package me.leafs.timechanger.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import me.leafs.timechanger.TimeChanger;
import me.leafs.timechanger.handler.config.TimeConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@ChannelHandler.Sharable // bitchass
public class TimeHandler extends ChannelInboundHandlerAdapter {
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        // don't run if the world is null or the game is paused
        if (mc.theWorld == null || mc.isGamePaused()) {
            return;
        }

        ChannelPipeline pipe = mc.thePlayer.sendQueue.getNetworkManager().channel().pipeline();
        // register a packet listener to check for time packets from server
        if (pipe.get(TimeChanger.MODID) == null && pipe.get("packet_handler") != null) {
            pipe.addBefore("packet_handler", TimeChanger.MODID, this);
        }

        TimeConfiguration config = TimeChanger.instance.getConfig();

        if (!config.isEnabled()) {
            return;
        }

        // set the time (depending on mode)
        switch (config.getMode()) {
            case DYNAMIC:
                // revolve the time using current time
                long theTime = (long) (System.currentTimeMillis() % 24000L * config.getSpeedMultiplier());
                mc.theWorld.setWorldTime(theTime);

                break;

            case STATIC:
                mc.theWorld.setWorldTime(config.getWorldTime());
                break;
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // block any time packets if the mod is enabled
        if (msg instanceof S03PacketTimeUpdate && TimeChanger.instance.getConfig().isEnabled()) {
            return;
        }

        super.channelRead(ctx, msg);
    }
}
