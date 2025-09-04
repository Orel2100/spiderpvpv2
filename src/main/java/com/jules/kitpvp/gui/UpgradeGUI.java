package com.jules.kitpvp.gui;

import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.player.MPlayerManager;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class UpgradeGUI implements InventoryHolder {

    private final KitClass kit;
    private final Player player;
    private final Inventory inventory;

    public UpgradeGUI(Player player, KitClass kit) {
        this.player = player;
        this.kit = kit;
        this.inventory = Bukkit.createInventory(this, 9 * 3, "Upgrade " + kit.getName());
        initializeItems();
    }

    private void initializeItems() {
        MPlayer mPlayer = MPlayerManager.getMPlayer(player.getName());
        Upgrade upgrade = new Upgrade(player, kit, UpgradeType.KIT);

        // Current Level Item
        ItemStack currentLevelItem = new ItemStack(Material.BOOK, upgrade.getCurrentUpgrade());
        ItemMeta currentLevelMeta = currentLevelItem.getItemMeta();
        currentLevelMeta.setDisplayName(ChatColor.GREEN + "Current Level");
        currentLevelMeta.setLore(Arrays.asList(ChatColor.GRAY + "You are currently at level " + upgrade.getCurrentUpgrade()));
        currentLevelItem.setItemMeta(currentLevelMeta);
        inventory.setItem(11, currentLevelItem);

        // Next Level Item
        if (upgrade.getCurrentUpgrade() < 9) {
            ItemStack nextLevelItem = new ItemStack(Material.ANVIL);
            ItemMeta nextLevelMeta = nextLevelItem.getItemMeta();
            nextLevelMeta.setDisplayName(ChatColor.GREEN + "Upgrade to Level " + upgrade.getNextUpgrade());
            nextLevelMeta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Cost: " + ChatColor.GOLD + kit.getUpgradePrice(upgrade.getNextUpgrade()) + " coins",
                    ChatColor.YELLOW + "Click to purchase!"
            ));
            nextLevelItem.setItemMeta(nextLevelMeta);
            inventory.setItem(15, nextLevelItem);
        } else {
            ItemStack maxLevelItem = new ItemStack(Material.BARRIER);
            ItemMeta maxLevelMeta = maxLevelItem.getItemMeta();
            maxLevelMeta.setDisplayName(ChatColor.RED + "Max Level Reached");
            maxLevelItem.setItemMeta(maxLevelMeta);
            inventory.setItem(15, maxLevelItem);
        }

        // Player Coins Item
        ItemStack coinsItem = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta coinsMeta = coinsItem.getItemMeta();
        coinsMeta.setDisplayName(ChatColor.GOLD + "Your Coins");
        coinsMeta.setLore(Arrays.asList(ChatColor.GRAY + String.valueOf(mPlayer.getPlayerData().getCoins())));
        coinsItem.setItemMeta(coinsMeta);
        inventory.setItem(22, coinsItem);
    }

    public void open() {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public KitClass getKit() {
        return kit;
    }
}
