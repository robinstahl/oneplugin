package dev.unowly.oneplugin;

import dev.unowly.oneplugin.commands.SetRankCommand;
import dev.unowly.oneplugin.commands.TimberCommand;
import dev.unowly.oneplugin.events.PlayerJoinListener;
import dev.unowly.oneplugin.events.TimberListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class OnePlugin extends JavaPlugin {

    private final Set<UUID> timberEnabled = new HashSet<>();

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getCommand("setrank").setExecutor(new SetRankCommand(this));

        getServer().getPluginManager().registerEvents(new TimberListener(this), this);
        getCommand("timber").setExecutor(new TimberCommand(this));

    }

    @Override
    public void onDisable() {
    }

    public Set<UUID> getTimberEnabled() {
        return timberEnabled;
    }
}
