package dev.unowly.oneplugin.events;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;

public class ChunkNotificationListener implements Listener {

    private final Set<Chunk> loadedChunks;
    private final Set<Player> notifiedPlayers = new HashSet<>();

    public ChunkNotificationListener(Set<Chunk> loadedChunks) {
        this.loadedChunks = loadedChunks;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Chunk fromChunk = event.getFrom().getChunk();
        Chunk toChunk = event.getTo().getChunk();

        if (fromChunk != toChunk) {
            if (loadedChunks.contains(toChunk) && !notifiedPlayers.contains(player)) {
                player.sendMessage("You entered a loaded chunk!");
                notifiedPlayers.add(player);
            } else if (!loadedChunks.contains(toChunk) && notifiedPlayers.contains(player)) {
                player.sendMessage("You left a loaded chunk!");
                notifiedPlayers.remove(player);
            }
        }
    }
}
