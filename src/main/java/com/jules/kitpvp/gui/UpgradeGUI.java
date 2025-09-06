package com.jules.kitpvp.gui;

import com.jules.kitpvp.kits.KitStat;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeCategory;
import com.jules.kitpvp.kits.classes.MegaWallsClass;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpgradeGUI implements InventoryHolder {

    private final Inventory inv;
    private final MPlayer mPlayer;
    private final MegaWallsClass kit;

    public UpgradeGUI(MPlayer mPlayer) {
        this.mPlayer = mPlayer;
        this.kit = (MegaWallsClass) mPlayer.getKitClass();
        this.inv = Bukkit.createInventory(this, 54, "Upgrades for " + kit.getName());
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

        // Set up upgrades
        Map<UpgradeCategory, List<Upgrade>> upgrades = kit.getUpgrades();
        for (UpgradeCategory category : upgrades.keySet()) {
            inv.setItem(category.getRow() * 9 + 1, new ItemStackCreator(getCategoryMaterial(category), "§a" + category.getName()).build());
            List<Upgrade> categoryUpgrades = upgrades.get(category);
            for (int i = 0; i < categoryUpgrades.size(); i++) {
                Upgrade upgrade = categoryUpgrades.get(i);
                int level = mPlayer.getUpgradeLevel(upgrade);
                int slot = category.getRow() * 9 + 3 + i;
                inv.setItem(slot, createUpgradeItem(upgrade, level));
            }
        }

        // Set up stats and prestige
        setupStats();
        setupPrestige();
    }

    private ItemStack createUpgradeItem(Upgrade upgrade, int level) {
        boolean maxed = level >= upgrade.getMaxLevel();
        boolean canAfford = mPlayer.getCoins() >= upgrade.getCost(level + 1);

        Material material = maxed ? Material.GREEN_STAINED_GLASS_PANE : upgrade.getMaterial(level + 1);
        short durability = maxed ? (short) 13 : (canAfford ? (short) 5 : (short) 14);
        String name = (maxed ? "§a" : (canAfford ? "§a" : "§c")) + upgrade.getName() + " " + (level + 1);

        ItemStackCreator creator = new ItemStackCreator(material, name).setDurability(durability);
        if (maxed) {
            creator.addEnchantment(Enchantment.DURABILITY, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS);
        }

        List<String> lore = new ArrayList<>(upgrade.getDescription());
        lore.add(" ");
        if (maxed) {
            lore.add("§aMAXED OUT");
        } else {
            lore.add("§7Cost: §6" + upgrade.getCost(level + 1));
            if (canAfford) {
                lore.add("§aClick to purchase!");
            } else {
                lore.add("§cYou cannot afford this!");
            }
        }
        creator.setLore(lore);

        return creator.build();
    }

    private void setupStats() {
        KitStat stats = kit.getKitStat();
        inv.setItem(48, new ItemStackCreator(Material.DIAMOND_SWORD, "§cDamage: " + stats.getDamage() + "/5").build());
        inv.setItem(49, new ItemStackCreator(Material.DIAMOND_CHESTPLATE, "§aDefense: " + stats.getDefense() + "/5").build());
        inv.setItem(50, new ItemStackCreator(Material.FEATHER, "§bMobility: " + stats.getMobility() + "/5").build());
        inv.setItem(51, new ItemStackCreator(Material.NETHER_STAR, "§dEnergy: " + stats.getEnergy() + "/5").build());
        inv.setItem(52, new ItemStackCreator(Material.BOOK, "§eDifficulty: " + stats.getDifficulty() + "/5").build());
    }

    private void setupPrestige() {
        // TODO: Implement prestige system
        inv.setItem(53, new ItemStackCreator(Material.DIAMOND, "§dPrestige").addLoreLine("§cComing soon!").build());
    }

    private Material getCategoryMaterial(UpgradeCategory category) {
        switch (category) {
            case ABILITY: return Material.NETHER_STAR;
            case PASSIVE_1: return Material.FEATHER;
            case PASSIVE_2: return Material.TNT;
            case KIT: return Material.DIAMOND_SWORD;
            case GATHERING: return Material.GOLDEN_PICKAXE;
            default: return Material.STONE;
        }
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
