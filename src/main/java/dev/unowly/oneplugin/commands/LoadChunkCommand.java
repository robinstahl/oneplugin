package dev.unowly.oneplugin.commands;

import dev.unowly.oneplugin.OnePlugin;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Set;

public class LoadChunkCommand implements CommandExecutor {

    private final OnePlugin plugin;

    public LoadChunkCommand(OnePlugin plugin) {
        this.plugin = plugin;
    }

    public Set<Chunk> getLoadedChunks() {
        return plugin.getLoadedChunks();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von Spielern verwendet werden.");
            return true;
        }

        Player player = (Player) sender;
        World world = player.getWorld();
        Location location = player.getLocation();
        Chunk playerChunk = location.getChunk();

        String chunkName = (args.length > 0) ? args[0] : "ChunkLoader(" + playerChunk.getX() + "," + playerChunk.getZ() + ")";
        int radius = 8;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                Chunk currentChunk = world.getChunkAt(playerChunk.getX() + dx, playerChunk.getZ() + dz);
                currentChunk.setForceLoaded(true);
                plugin.getLoadedChunks().add(currentChunk);
            }
        }

        ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setCustomName(chunkName);
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        armorStand.addScoreboardTag(chunkName);

        plugin.getScoreboardManager().addArmorStandToTeam(armorStand, "chunkloaders");

        saveChunkToConfig(chunkName, playerChunk);

        player.sendMessage("Chunks in einem Gebiet wurden geladen. Name: " + chunkName);
        return true;
    }

    private void saveChunkToConfig(String name, Chunk chunk) {
        String path = "loadedChunks." + name;
        plugin.getConfig().set(path + ".world", chunk.getWorld().getName());
        plugin.getConfig().set(path + ".x", chunk.getX());
        plugin.getConfig().set(path + ".z", chunk.getZ());
        plugin.saveConfig();
    }
}
