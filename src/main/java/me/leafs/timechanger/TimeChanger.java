package me.leafs.timechanger;

import lombok.Getter;
import me.leafs.timechanger.command.ChangeTime;
import me.leafs.timechanger.handler.TimeHandler;
import me.leafs.timechanger.handler.config.TimeConfiguration;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TimeChanger.MODID, version = TimeChanger.VERSION)
public class TimeChanger {
    public static final String MODID = "timechanger";
    public static final String VERSION = "3.0";

    @Mod.Instance
    public static TimeChanger instance;

    @Getter private TimeConfiguration config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new TimeConfiguration();

        // TODO: 9/30/2020 config saving and loading and shit
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // register the time faker
        MinecraftForge.EVENT_BUS.register(new TimeHandler());

        // register change time command
        ClientCommandHandler.instance.registerCommand(new ChangeTime());
    }
}
