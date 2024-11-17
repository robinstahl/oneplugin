package dev.unowly.oneplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.unowly.oneplugin.OnePlugin;

public class TimberCommand implements CommandExecutor {

    private final OnePlugin plugin;

    public TimberCommand(OnePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Dieser Befehl kann nur von Spielern verwendet werden!");
            return true;
        }

        Player player = (Player) sender;

        boolean isEnabled = plugin.getTimberEnabled().contains(player.getUniqueId());
        if (isEnabled) {
            plugin.getTimberEnabled().remove(player.getUniqueId());
            player.sendMessage(ChatColor.RED + "Timber-Modus deaktiviert!");
        } else {
            plugin.getTimberEnabled().add(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Timber-Modus aktiviert!");
        }

        return true;
    }
}

