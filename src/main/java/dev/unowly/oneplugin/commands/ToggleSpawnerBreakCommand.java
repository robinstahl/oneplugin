package dev.unowly.oneplugin.commands;

import dev.unowly.oneplugin.OnePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleSpawnerBreakCommand implements CommandExecutor {
    private final OnePlugin plugin;

    public ToggleSpawnerBreakCommand(OnePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean currentState = plugin.getConfig().getBoolean("spawner.break.enabled", true);
        boolean newState = !currentState;

        plugin.getConfig().set("spawner.break.enabled", newState);
        plugin.saveConfig();

        sender.sendMessage("Spawner breaking is now " + (newState ? "enabled" : "disabled"));
        return true;
    }
}
