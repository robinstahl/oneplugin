package dev.unowly.oneplugin.events;

import dev.unowly.oneplugin.OnePlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerBreakListener implements Listener {

    private final OnePlugin plugin;

    public SpawnerBreakListener(OnePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawnerBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if(block.getType() != Material.SPAWNER){
            return;
        }

        Player player = event.getPlayer();

        boolean isEnabled = plugin.getConfig().getBoolean("enable-spawner-break");
        if(!isEnabled){
            player.sendMessage("Breaking spawners is currently disabled.");
            event.setCancelled(true);
            return;
        }

        if(block.getType() == Material.SPAWNER){
            ItemStack tool = player.getInventory().getItemInMainHand();

            if(tool.getType() == Material.NETHERITE_PICKAXE && tool.containsEnchantment(Enchantment.SILK_TOUCH)){
                event.setDropItems(false);

                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                String mobType = spawner.getSpawnedType().toString();

                ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
                ItemMeta meta = spawnerItem.getItemMeta();
                meta.setDisplayName("Custom Spawner");
                meta.getPersistentDataContainer().set(NamespacedKey.minecraft("mob_type"),
                        PersistentDataType.STRING,
                        mobType
                );
                spawnerItem.setItemMeta(meta);

                player.getWorld().dropItemNaturally(player.getLocation(), spawnerItem);
                player.sendMessage("You broke a spawner containing: " + mobType);
            } else {
                player.sendMessage("You need a Netherite Pickaxe with Silk Touch to break the spawner!");
                event.setCancelled(true);
            }
        }
    }

}
