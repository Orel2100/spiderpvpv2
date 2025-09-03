package com.jules.kitpvp.listeners;

import com.jules.kitpvp.gui.ClassSelectorGUI;
import com.jules.kitpvp.kits.ClassManager;
import com.jules.kitpvp.kits.KitClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class GUIListener implements Listener {

    private final ClassManager classManager;

    public GUIListener(ClassManager classManager) {
        this.classManager = classManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof ClassSelectorGUI) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            // Logic to determine which class was clicked and assign it to the player
        }
    }
}
