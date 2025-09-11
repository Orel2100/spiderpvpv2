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
import java.util.Arrays;
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

                    // Set display name
                    String displayName = "§a" + kitClass.getName();
                    if (kitClass.getName().equalsIgnoreCase("arcanist")) {
                        displayName += " §e- RECOMMENDED";
                    }
                    meta.setDisplayName(displayName);

                    List<String> lore = new ArrayList<>();
                    lore.add("§fDifficulty: §a" + kitClass.getDifficulty());
                    lore.add("§fPlay Styles: §a" + kitClass.getPlayStyle());
                    lore.add(" ");
                    lore.add("§6Skill - " + kitClass.getSkillName());

                    // Split skill description and format it
                    String[] skillDesc = kitClass.getSkillDescription().split("\\\\n");
                    for(String line : skillDesc) {
                        lore.add("§7" + line);
                    }

                    lore.add(" ");

                    if (mPlayer.hasKit(kit)) {
                        lore.add("§aUNLOCKED");
                    } else {
                        lore.add("§cLOCKED");
                        lore.add("§7Unlock cost: §6" + kitClass.getPrice());
                    }

                    lore.add(" ");
                    lore.add("§eClick to view more!");

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
