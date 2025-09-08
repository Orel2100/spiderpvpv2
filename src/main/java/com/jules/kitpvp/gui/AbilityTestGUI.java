package com.jules.kitpvp.gui;

import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AbilityTestGUI implements InventoryHolder {

    private final Inventory inventory;

    public AbilityTestGUI() {
        this.inventory = Bukkit.createInventory(this, 9 * 6, "Ability Testing");
        initializeItems();
    }

    private void initializeItems() {
        for (Kit kit : Kit.values()) {
            try {
                KitClass kitInstance = kit.getKitClass().newInstance();
                ItemStack abilityItem = kitInstance.getAbilityItem();

                if (abilityItem != null) {
                    ItemMeta meta = abilityItem.getItemMeta();
                    List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                    lore.add(" ");
                    lore.add("§7Kit: §a" + kitInstance.getName());
                    meta.setLore(lore);
                    abilityItem.setItemMeta(meta);
                    inventory.addItem(abilityItem);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
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
