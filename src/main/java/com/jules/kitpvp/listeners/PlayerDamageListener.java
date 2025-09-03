package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {

    private final KitPVP plugin;

    public PlayerDamageListener(KitPVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        // Handle custom damage for abilities, passives, etc.
    }
}
