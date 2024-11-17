package dev.unowly.oneplugin.commands;

import dev.unowly.oneplugin.OnePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetRankCommand implements CommandExecutor {

    private final OnePlugin plugin;

    public SetRankCommand(OnePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length != 2){
            sender.sendMessage(ChatColor.RED + "Usage: /setrank <player> <rank>");
            return true;
        }

        String playerName = args[0];
        String rankKey = args[1];

        if(plugin.getConfig().getString("ranks." + rankKey) == null){
            sender.sendMessage(ChatColor.RED + "The rank" + rankKey + " does not exist.");
            return true;
        }

        plugin.getConfig().set("players." + playerName, rankKey);
        plugin.saveConfig();

        sender.sendMessage(ChatColor.GREEN + "Rank" + rankKey + " granted to" + playerName);
        return true;
    }
}
