package dev.unowly.oneplugin.events;

import dev.unowly.oneplugin.OnePlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener  implements Listener {
    private final OnePlugin plugin;

    public PlayerJoinListener(OnePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        String rankKey = plugin.getConfig().getString("players." + player.getName(), "player");
        String rankPrefix = plugin.getConfig().getString("ranks." + rankKey, "&7[Player]");

        player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', rankPrefix +  " " + player.getName()));
    }
}
