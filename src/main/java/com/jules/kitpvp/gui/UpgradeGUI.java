package com.jules.kitpvp.gui;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class UpgradeGUI implements InventoryHolder {

    private Inventory inv;
    private MPlayer mPlayer;

    public UpgradeGUI(MPlayer mPlayer) {
        this.mPlayer = mPlayer;
        this.inv = Bukkit.createInventory(this, 27, "Upgrades for " + mPlayer.getKit().getName());
        setupGUI();
    }

    private void setupGUI() {
        if (mPlayer.getKitClass() == null) {
            mPlayer.getPlayer().sendMessage("You have not selected a kit!");
            return;
        }
        for(Upgrade upgrade : mPlayer.getKitClass().getUpgrades()) {
            ItemStack item;
            String name = "";
            String lore;
            int level = mPlayer.getUpgradeLevel(upgrade.getType());
            int maxLevel = upgrade.getMaxLevel();
            int cost = KitPVP.getInstance().getUpgradeManager().getCost(level + 1);

            switch (upgrade.getType()) {
                case SWORD:
                    name = "§bSword Upgrade";
                    item = new ItemStack(Material.DIAMOND_SWORD);
                    break;
                case AXE:
                    name = "§bAxe Upgrade";
                    item = new ItemStack(Material.DIAMOND_AXE);
                    break;
                case BOW:
                    name = "§bBow Upgrade";
                    item = new ItemStack(Material.BOW);
                    break;
                case HELMET:
                    name = "§bHelmet Upgrade";
                    item = new ItemStack(Material.DIAMOND_HELMET);
                    break;
                case CHESTPLATE:
                    name = "§bChestplate Upgrade";
                    item = new ItemStack(Material.DIAMOND_CHESTPLATE);
                    break;
                case LEGGINGS:
                    name = "§bLeggings Upgrade";
                    item = new ItemStack(Material.DIAMOND_LEGGINGS);
                    break;
                case BOOTS:
                    name = "§bBoots Upgrade";
                    item = new ItemStack(Material.DIAMOND_BOOTS);
                    break;
                case POTION:
                    name = "§bPotion Upgrade";
                    item = new ItemStack(Material.POTION);
                    break;
                case GOLDEN_APPLE:
                    name = "§bGolden Apple Upgrade";
                    item = new ItemStack(Material.GOLDEN_APPLE);
                    break;
                case ABILITY:
                    name = "§bAbility Upgrade";
                    item = new ItemStack(Material.NETHER_STAR);
                    break;
                default:
                    name = "§cUnknown Upgrade";
                    item = new ItemStack(Material.STONE);
                    break;
            }
            if(level >= maxLevel) {
                lore = "§aMax level reached!";
            } else {
                lore = "§7Cost: §6" + cost + " coins";
            }
            ItemStackCreator creator = new ItemStackCreator(item).setName(name).addLoreLine(lore).addLoreLine("§7Level: §a" + level + "/" + maxLevel);
            inv.addItem(creator.build());
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
