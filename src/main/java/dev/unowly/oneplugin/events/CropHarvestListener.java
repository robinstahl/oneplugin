package dev.unowly.oneplugin.events;

import dev.unowly.oneplugin.OnePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class CropHarvestListener implements Listener {

    @EventHandler
    public void onCropRightClick(PlayerInteractEvent event) {
        if (!event.getAction().equals(org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (!event.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() != Material.SHEARS) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) {
            return;
        }

        Material cropType = clickedBlock.getType();

        if (isSupportedCrop(cropType)) {
            BlockData blockData = clickedBlock.getBlockData();

            if (blockData instanceof Ageable) {
                Ageable ageable = (Ageable) blockData;

                if (ageable.getAge() == ageable.getMaximumAge()) {
                    event.setCancelled(true);
                    Collection<ItemStack> drops = clickedBlock.getDrops();
                    clickedBlock.setType(Material.AIR);

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        clickedBlock.setType(cropType);
                    }, 1L);

                    for (ItemStack drop : drops) {
                        if (isSeedItem(drop.getType())) {
                            if (drop.getAmount() > 1) {
                                drop.setAmount(drop.getAmount() - 1);
                            } else {
                                continue;
                            }
                        }
                        player.getInventory().addItem(drop);
                    }

                    player.playSound(player.getLocation(), "item.crop.harvest", 1.0f, 1.0f);
                }
            }
        }
    }

    private boolean isSupportedCrop(Material material) {
        switch (material) {
            case WHEAT:
            case CARROTS:
            case POTATOES:
            case BEETROOTS:
                return true;
            default:
                return false;
        }
    }

    private boolean isSeedItem(Material material) {
        switch (material) {
            case WHEAT_SEEDS:
            case CARROT:
            case POTATO:
            case BEETROOT_SEEDS:
                return true;
            default:
                return false;
        }
    }

    private final OnePlugin plugin;

    public CropHarvestListener(OnePlugin plugin) {
        this.plugin = plugin;
    }
}
