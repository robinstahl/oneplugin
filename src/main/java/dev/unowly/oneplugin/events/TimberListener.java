package dev.unowly.oneplugin.events;

import dev.unowly.oneplugin.OnePlugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class TimberListener implements Listener {
    private final OnePlugin plugin;

    public TimberListener(OnePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var player = event.getPlayer();
        if (!plugin.getTimberEnabled().contains(player.getUniqueId())) return;

        Block block = event.getBlock();
        if (block.getType() == Material.OAK_LOG || block.getType().toString().endsWith("_LOG")) {
            breakTree(block);
        }
    }

    private void breakTree(Block block) {
        Set<Block> toBreak = new HashSet<>();
        toBreak.add(block);

        findConnectedLogs(block, toBreak);

        for (Block log : toBreak) {
            log.breakNaturally(new ItemStack(Material.DIAMOND_AXE));
        }
    }

    private void findConnectedLogs(Block block, Set<Block> toBreak) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block nearby = block.getRelative(x, y, z);
                    if (!toBreak.contains(nearby) &&
                            (nearby.getType() == Material.OAK_LOG || nearby.getType().toString().endsWith("_LOG"))) {
                        toBreak.add(nearby);
                        findConnectedLogs(nearby, toBreak);
                    }
                }
            }
        }
    }
}
