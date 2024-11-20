package dev.unowly.oneplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {

    private final Scoreboard scoreboard;

    public ScoreboardManager() {
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Create team for chunk loaders if not already existing
        if (scoreboard.getTeam("chunkloaders") == null) {
            Team chunkLoaderTeam = scoreboard.registerNewTeam("chunkloaders");
            chunkLoaderTeam.setDisplayName("Chunk Loaders");
            chunkLoaderTeam.setColor(ChatColor.GOLD);
            chunkLoaderTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        }
    }

    public void addChunkLoader(String name) {
        Team chunkLoaderTeam = scoreboard.getTeam("chunkloaders");
        if (chunkLoaderTeam != null) {
            chunkLoaderTeam.addEntry(name);
        }
    }

    public void removeChunkLoader(String name) {
        Team chunkLoaderTeam = scoreboard.getTeam("chunkloaders");
        if (chunkLoaderTeam != null) {
            chunkLoaderTeam.removeEntry(name);
        }
    }

    public void addArmorStandToTeam(ArmorStand armorStand, String teamName) {
        Team team = scoreboard.getTeam(teamName);

        if (team != null) {
            team.addEntry(armorStand.getUniqueId().toString());
        }
    }
}
