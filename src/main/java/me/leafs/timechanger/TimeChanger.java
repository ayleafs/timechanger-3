package me.leafs.timechanger;

import lombok.Getter;
import me.leafs.timechanger.command.ChangeTime;
import me.leafs.timechanger.handler.TimeHandler;
import me.leafs.timechanger.handler.config.ConfigHandler;
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
    @Getter private ConfigHandler configHandler;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configHandler = new ConfigHandler(event.getSuggestedConfigurationFile());
        config = configHandler.readConfig();

        // make sure the config saves on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> configHandler.populateConfig(config)));
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // register the time faker
        MinecraftForge.EVENT_BUS.register(new TimeHandler());

        // register change time command
        ClientCommandHandler.instance.registerCommand(new ChangeTime());
    }
}
