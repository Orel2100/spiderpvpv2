package com.jules.kitpvp.gui;

import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.ClassType;
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

    public ClassSelectorGUI(ClassType type, Player player) {
        if (type == ClassType.NORMAL) {
            this.inventory = Bukkit.createInventory(this, 9 * 4, "Normal Kits");
        } else {
            this.inventory = Bukkit.createInventory(this, 9 * 4, "Hero Kits");
        }
        initializeItems(type, player);
    }

    private void initializeItems(ClassType type, Player player) {
        com.jules.kitpvp.player.MPlayer mPlayer = com.jules.kitpvp.player.MPlayer.getMPlayer(player.getUniqueId());
        for (Kit kit : Kit.values()) {
            if (kit.getClassType() == type) {
                try {
                    KitClass kitClass = kit.getKitClass().newInstance();
                    ItemStack item = kitClass.getIcon();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(kitClass.getName());
                    List<String> lore = new ArrayList<>();
                    lore.addAll(kitClass.getDescription());
                    lore.add(" ");
                    if (mPlayer.hasKit(kit)) {
                        lore.add("§aOwned");
                    } else {
                        lore.add("§ePrice: §6" + kitClass.getPrice());
                    }
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    inventory.addItem(item);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
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
