package com.jules.kitpvp.gui;

import com.jules.kitpvp.kits.ClassManager;
import com.jules.kitpvp.kits.KitClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClassSelectorGUI implements InventoryHolder {

    private final ClassManager classManager;
    private final Inventory inventory;

    public ClassSelectorGUI(ClassManager classManager) {
        this.classManager = classManager;
        this.inventory = Bukkit.createInventory(this, 9 * 3, "Select a Class");
        initializeItems();
    }

    private void initializeItems() {
        for (KitClass kit : classManager.getClasses().values()) {
            ItemStack item = new ItemStack(kit.getIcon().getType());
            item.setAmount(1);
            org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(kit.getName());
            List<String> lore = new ArrayList<>();
            lore.add(kit.getDescription());
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.addItem(item);
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
