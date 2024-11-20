package dev.unowly.oneplugin.events;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class ArmorStandProtectionListener implements Listener {

    @EventHandler
    public void onArmorStandDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ArmorStand) {
            for (String tag : entity.getScoreboardTags()) {
                if (tag.startsWith("ChunkLoader(")) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onArmorStandInteract(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof ArmorStand) {
            for (String tag : entity.getScoreboardTags()) {
                if (tag.startsWith("ChunkLoader(")) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
