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
            sender.sendMessage("Dieser Befehl kann nur von Spielern verwendet werden.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Bitte gib den Namen des Chunks an, den du entladen möchtest (z.B. farm).");
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
            player.sendMessage("Das Gebiet mit dem Namen " + chunkName + " wurde entladen.");
        } else {
            player.sendMessage("Kein geladenes Gebiet mit dem Namen " + chunkName + " gefunden.");
        }

        return true;
    }
}
