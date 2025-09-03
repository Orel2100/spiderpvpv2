package com.jules.kitpvp.gui;

import com.jules.kitpvp.kits.ClassManager;
import com.jules.kitpvp.kits.KitClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ClassSelectorGUI implements InventoryHolder {

    private final ClassManager classManager;
    private final Inventory inventory;

    public ClassSelectorGUI(ClassManager classManager) {
        this.classManager = classManager;
        this.inventory = Bukkit.createInventory(this, 9 * 3, "Select a Class");
        initializeItems();
    }

    private void initializeItems() {
        // This will be populated with the available classes
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
