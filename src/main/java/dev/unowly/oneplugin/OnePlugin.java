package dev.unowly.oneplugin;

import dev.unowly.oneplugin.commands.LoadChunkCommand;
import dev.unowly.oneplugin.commands.SetRankCommand;
import dev.unowly.oneplugin.commands.TimberCommand;
import dev.unowly.oneplugin.commands.UnloadChunkCommand;
import dev.unowly.oneplugin.events.ArmorStandProtectionListener;
import dev.unowly.oneplugin.events.ChunkNotificationListener;
import dev.unowly.oneplugin.events.PlayerJoinListener;
import dev.unowly.oneplugin.events.TimberListener;
import org.bukkit.Chunk;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class OnePlugin extends JavaPlugin {

    private final Set<UUID> timberEnabled = new HashSet<>();
    private final Set<Chunk> loadedChunks = new HashSet<>();
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.scoreboardManager = new ScoreboardManager();

        // Commands
        LoadChunkCommand loadChunkCommand = new LoadChunkCommand(this);
        getCommand("loadchunk").setExecutor(loadChunkCommand);
        getCommand("unloadchunk").setExecutor(new UnloadChunkCommand(loadChunkCommand, scoreboardManager, this));
        getCommand("setrank").setExecutor(new SetRankCommand(this));
        getCommand("timber").setExecutor(new TimberCommand(this));

        // Events
        getServer().getPluginManager().registerEvents(new ArmorStandProtectionListener(), this);
        getServer().getPluginManager().registerEvents(new ChunkNotificationListener(loadedChunks), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new TimberListener(this), this);
    }

    public Set<UUID> getTimberEnabled() {
        return timberEnabled;
    }

    public Set<Chunk> getLoadedChunks() {
        return loadedChunks;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}
