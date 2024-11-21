package dev.unowly.oneplugin;

import dev.unowly.oneplugin.commands.*;
import dev.unowly.oneplugin.events.*;
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

        // Commands
        getCommand("setrank").setExecutor(new SetRankCommand(this));
        getCommand("timber").setExecutor(new TimberCommand(this));
        getCommand("togglespawnerbreak").setExecutor(new ToggleSpawnerBreakCommand(this));

        // Events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new TimberListener(this), this);
        getServer().getPluginManager().registerEvents(new SpawnerBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new CropHarvestListener(this), this);
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
