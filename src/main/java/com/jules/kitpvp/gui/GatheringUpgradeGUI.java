package com.jules.kitpvp.gui;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GatheringUpgradeGUI implements InventoryHolder {

    private final Inventory inventory;
    private final MPlayer mPlayer;

    public GatheringUpgradeGUI(Player player) {
        this.mPlayer = MPlayer.getMPlayer(player.getUniqueId());
        this.inventory = Bukkit.createInventory(this, 9 * 3, "Gathering Upgrades");
        initializeItems();
    }

    private void initializeItems() {
        int currentLevel = mPlayer.getGatheringLevel();
        List<Integer> costs = KitPVP.getInstance().getConfig().getIntegerList("global-upgrades.gathering.costs");

        ItemStack upgradeItem = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta upgradeMeta = upgradeItem.getItemMeta();
        upgradeMeta.setDisplayName(ChatColor.GOLD + "Gathering Upgrade");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Current Level: " + ChatColor.GREEN + currentLevel);
        lore.add("");

        if (currentLevel < costs.size()) {
            lore.add(ChatColor.GRAY + "Next Level Cost: " + ChatColor.GOLD + costs.get(currentLevel) + " coins");
            lore.add(ChatColor.YELLOW + "Click to upgrade!");
        } else {
            lore.add(ChatColor.GREEN + "You are at the maximum level!");
        }

        upgradeMeta.setLore(lore);
        upgradeItem.setItemMeta(upgradeMeta);
        inventory.setItem(13, upgradeItem);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
