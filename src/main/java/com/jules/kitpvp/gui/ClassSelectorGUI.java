package com.jules.kitpvp.gui;

import com.jules.kitpvp.kits.ClassManager;
import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClassSelectorGUI implements InventoryHolder {

    private final Inventory inventory;

    public ClassSelectorGUI(ClassManager classManager, ClassType type) {
        if (type == ClassType.NORMAL) {
            this.inventory = Bukkit.createInventory(this, 9 * 4, "Normal Kits");
        } else {
            this.inventory = Bukkit.createInventory(this, 9 * 4, "Hero Kits");
        }
        initializeItems(classManager, type);
    }

    private void initializeItems(ClassManager classManager, ClassType type) {
        for (KitClass kit : classManager.getClasses()) {
            if (kit.getType() == type) {
                ItemStack item = kit.getIcon();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(kit.getName());
                List<String> lore = new ArrayList<>();
                lore.addAll(kit.getDescription());
                lore.add(" ");
                lore.add("Price: " + kit.getPrice());
                meta.setLore(lore);
                item.setItemMeta(meta);
                inventory.addItem(item);
            }
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
