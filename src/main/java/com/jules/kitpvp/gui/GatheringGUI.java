package com.jules.kitpvp.gui;

import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GatheringGUI implements InventoryHolder {

    private final Inventory inv;
    private final MPlayer mPlayer;

    public GatheringGUI(MPlayer mPlayer) {
        this.mPlayer = mPlayer;
        this.inv = Bukkit.createInventory(this, 54, "Gathering Upgrades");
        setupGUI();
    }

    private void setupGUI() {
        // Fill borders with glass panes
        ItemStack blackPane = new ItemStackCreator(Material.BLACK_STAINED_GLASS_PANE, " ").build();
        for (int i = 0; i < 54; i++) {
            if (i < 10 || i > 43 || i % 9 == 0 || i % 9 == 8) {
                inv.setItem(i, blackPane);
            }
        }

        // Back button
        inv.setItem(49, new ItemStackCreator(Material.ARROW, "§aBack to Upgrades").build());

        // Set up upgrades
        List<Upgrade> upgrades = com.jules.kitpvp.KitPVP.getInstance().getGatheringManager().getGatheringUpgrades();
        for (int i = 0; i < upgrades.size(); i++) {
            Upgrade upgrade = upgrades.get(i);
            int currentLevel = mPlayer.getUpgradeLevel(upgrade);
            for (int tier = 1; tier <= upgrade.getMaxLevel(); tier++) {
                int slot = (i + 1) * 9 + 2 + (tier - 1);
                inv.setItem(slot, createUpgradeItem(upgrade, tier, currentLevel));
            }
        }
    }

    private ItemStack createUpgradeItem(Upgrade upgrade, int tier, int currentLevel) {
        boolean unlocked = tier <= currentLevel;
        boolean nextUnlock = tier == currentLevel + 1;
        boolean canAfford = mPlayer.getCoins() >= upgrade.getCost(tier);

        Material material;
        String name;
        List<String> lore = new java.util.ArrayList<>();
        lore.add(upgrade.getDescription());
        if (upgrade.getName().equals("Coin Gathering")) {
            int coins = (int) (10 * Math.pow(2, tier - 1));
            lore.add("§7Coins per ore: §6" + coins);
        }
        ItemStackCreator creator;

        if (unlocked) {
            material = Material.GREEN_STAINED_GLASS_PANE;
            name = "§a" + upgrade.getName() + " " + tier;
            lore.add(" ");
            lore.add("§aUNLOCKED");
            creator = new ItemStackCreator(material, name).setDurability((short) 5);
            creator.addEnchantment(Enchantment.DURABILITY, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS);
        } else if (nextUnlock) {
            material = upgrade.getMaterial(tier);
            name = (canAfford ? "§a" : "§c") + upgrade.getName() + " " + tier;
            lore.add(" ");
            lore.add("§7Cost: §6" + upgrade.getCost(tier));
            if (canAfford) {
                lore.add("§aClick to purchase!");
            } else {
                lore.add("§cYou cannot afford this!");
            }
            creator = new ItemStackCreator(material, name);
        } else {
            material = Material.RED_STAINED_GLASS_PANE;
            name = "§c" + upgrade.getName() + " " + tier;
            lore.add(" ");
            lore.add("§7Cost: §6" + upgrade.getCost(tier));
            lore.add("§cLocked!");
            creator = new ItemStackCreator(material, name).setDurability((short) 14);
        }

        creator.setLore(lore);
        return creator.build();
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
