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
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Please specify the name of the chunk to unload (e.g., farm).");
            return true;
        }

        String chunkName = args[0];
        boolean chunkFound = false;

        // Remove chunk from config.yml
        if (plugin.getConfig().contains("loadedChunks." + chunkName)) {
            plugin.getConfig().set("loadedChunks." + chunkName, null);
            plugin.saveConfig();
            chunkFound = true;
        }

        // Unload chunks
        for (Chunk chunk : loadChunkCommand.getLoadedChunks()) {
            if (chunk.isForceLoaded()) {
                chunk.setForceLoaded(false);
            }
        }

        // Remove associated ArmorStands
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof ArmorStand && entity.getScoreboardTags().contains(chunkName)) {
                    scoreboardManager.removeChunkLoader(chunkName);
                    entity.remove();
                }
            }
        }

        if (chunkFound) {
            player.sendMessage("The chunk with the name " + chunkName + " has been unloaded.");
        } else {
            player.sendMessage("No loaded chunk found with the name " + chunkName + ".");
        }

        return true;
    }
}
