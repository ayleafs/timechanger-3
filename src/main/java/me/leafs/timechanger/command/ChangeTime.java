package me.leafs.timechanger.command;

import me.leafs.timechanger.TimeChanger;
import me.leafs.timechanger.handler.config.TimeConfiguration;
import me.leafs.timechanger.utils.ChatUtils;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.Arrays;
import java.util.List;

public class ChangeTime implements ICommand {
    @Override
    public String getCommandName() {
        return "settime";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/settime <'toggle' | time | speed> [static | dynamic]";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("timechange", "changetime", "timechanger");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            ChatUtils.printChat("§cUsage: " + getCommandUsage(sender));
            return;
        }

        TimeConfiguration config = TimeChanger.instance.getConfig();

        if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            boolean enabled = !config.isEnabled();
            config.setEnabled(enabled);

            ChatUtils.printChat(String.format("§7You toggled time changer %s§7, that's crazy.", (enabled) ? "§aon" : "§coff"));
            return;
        }

        // make the default mode the one they're currently using
        TimeMode mode = config.getMode();

        // check if they specified a mode
        if (args.length >= 2) {
            mode = TimeMode.valueOr(args[1], mode);
        }

        // I hate how indented this block is
        // but... shut up
        try {
            // treat the first argument differently
            // depending on what mode was specified
            switch (mode) {
                case STATIC:
                    long l = Long.parseLong(args[0]);
                    config.setWorldTime(l);

                    if (l < 0) {
                        ChatUtils.printChat("§cThat's not funny.");
                        return;
                    }

                    ChatUtils.printChat(String.format("§7The world time is now set to §d%,d§7.", l));

                    break;

                case DYNAMIC:
                    float f = Float.parseFloat(args[0]);
                    config.setSpeedMultiplier(f);

                    ChatUtils.printChat(String.format("§7The world time will now revolve at §d%.2fx speed§7.", f));

                    break;

                default:
                    // first person that makes it here (no changing code)
                    // somehow, I will paypal them $5
                    System.out.println("idjhau9wdhad");

                    break;
            }

            // update the mode being used
            config.setMode(mode);
        } catch (NumberFormatException e) {
            ChatUtils.printChat("§cYou know what a number looks like right? This ain't it chief.");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
