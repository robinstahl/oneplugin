package dev.unowly.oneplugin.commands;

import dev.unowly.oneplugin.OnePlugin;
import dev.unowly.oneplugin.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class UnloadChunkCommand implements CommandExecutor {

    private final LoadChunkCommand loadChunkCommand;
    private final ScoreboardManager scoreboardManager;
    private final OnePlugin plugin;

    public UnloadChunkCommand(LoadChunkCommand loadChunkCommand, ScoreboardManager scoreboardManager, OnePlugin plugin) {
        this.loadChunkCommand = loadChunkCommand;
        this.scoreboardManager = scoreboardManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player!.");

            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Please provide a valid chunk name. E.g. farm");
            return true;
        }

        String chunkName = args[0];
        boolean chunkFound = false;

        if (plugin.getConfig().contains("loadedChunks." + chunkName)) {
            plugin.getConfig().set("loadedChunks." + chunkName, null);
            plugin.saveConfig();
            chunkFound = true;
        }

        for (Chunk chunk : loadChunkCommand.getLoadedChunks()) {
            if (chunk.isForceLoaded()) {
                chunk.setForceLoaded(false);
            }
        }

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof ArmorStand && entity.getScoreboardTags().contains(chunkName)) {
                    scoreboardManager.removeChunkLoader(chunkName);
                    entity.remove();
                }
            }
        }

        if (chunkFound) {
            player.sendMessage("The area with name " + chunkName + " will be unloaded.");
        } else {
            player.sendMessage("No area with name " + chunkName + " was found.");
        }
        return true;
    }
}
