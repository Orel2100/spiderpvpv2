package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    private final KitPVP plugin;

    public PlayerDamageListener(KitPVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && com.jules.kitpvp.abilities.Leap.leaping.contains(player)) {
                event.setCancelled(true);
                com.jules.kitpvp.abilities.Leap.leaping.remove(player);
            }
            MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
            if (mPlayer.getKitClass() != null) {
                mPlayer.getKitClass().onDamage(event);
            }
        }

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            Entity damager = e.getDamager();
            if (damager instanceof Player) {
                MPlayer mDamager = MPlayer.getMPlayer(damager.getUniqueId());
                if (mDamager.getKitClass() != null) {
                    mDamager.getKitClass().onDamageByEntity(e);
                }
            }
        }
    }
}
