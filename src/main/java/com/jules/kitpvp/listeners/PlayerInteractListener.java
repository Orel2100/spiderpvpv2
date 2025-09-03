package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.ClassManager;
import com.jules.kitpvp.kits.KitClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final KitPVP plugin;
    private final ClassManager classManager;

    public PlayerInteractListener(KitPVP plugin, ClassManager classManager) {
        this.plugin = plugin;
        this.classManager = classManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        KitClass kit = classManager.getPlayerClass(player);
        if (kit != null) {
            // Check for right-click with a specific item to trigger ability
        }
    }
}
