package com.jules.kitpvp.kits;

import com.jules.kitpvp.KitPVP;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GatheringManager {

    private final KitPVP plugin;
    private final List<Upgrade> gatheringUpgrades;

    public GatheringManager(KitPVP plugin) {
        this.plugin = plugin;
        this.gatheringUpgrades = loadGatheringUpgrades();
    }

    private List<Upgrade> loadGatheringUpgrades() {
        List<Upgrade> upgrades = new java.util.ArrayList<>();
        org.bukkit.configuration.file.FileConfiguration config = plugin.getConfig();

        upgrades.add(new Upgrade("Coin Gathering", "Grants coins for mining iron ore.", 5,
                config.getIntegerList("gathering.coingathering.costs"),
                Arrays.asList(Material.IRON_ORE, Material.IRON_ORE, Material.IRON_ORE, Material.IRON_ORE, Material.IRON_ORE)));

        return upgrades;
    }

    public List<Upgrade> getGatheringUpgrades() {
        return gatheringUpgrades;
    }

    public void attemptPurchase(org.bukkit.entity.Player player, String itemName) {
        com.jules.kitpvp.player.MPlayer mPlayer = com.jules.kitpvp.player.MPlayer.getMPlayer(player.getUniqueId());
        String strippedItemName = org.bukkit.ChatColor.stripColor(itemName);
        String[] nameParts = strippedItemName.split(" ");
        if (nameParts.length < 2) {
            return;
        }

        int tier;
        try {
            tier = Integer.parseInt(nameParts[nameParts.length - 1]);
        } catch (NumberFormatException e) {
            return;
        }

        String upgradeName = strippedItemName.substring(0, strippedItemName.length() - 2);

        for (Upgrade upgrade : gatheringUpgrades) {
            if (upgrade.getName().equals(upgradeName)) {
                int currentLevel = mPlayer.getUpgradeLevel(upgrade);
                if (tier == currentLevel + 1) {
                    int cost = upgrade.getCost(tier);
                    if (mPlayer.getCoins() >= cost) {
                        mPlayer.setCoins(mPlayer.getCoins() - cost);
                        mPlayer.setUpgradeLevel(upgrade, tier);
                        player.sendMessage(org.bukkit.ChatColor.GREEN + "You have purchased " + upgrade.getName() + " " + tier + "!");
                        new com.jules.kitpvp.gui.GatheringGUI(mPlayer).open(player);
                    } else {
                        player.sendMessage(org.bukkit.ChatColor.RED + "You cannot afford this!");
                    }
                }
                return;
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {
        com.jules.kitpvp.player.MPlayer mPlayer = com.jules.kitpvp.player.MPlayer.getMPlayer(event.getPlayer().getUniqueId());

        int coinGatheringLevel = mPlayer.getUpgradeLevel(gatheringUpgrades.get(0));
        if (coinGatheringLevel > 0) {
            if (event.getBlock().getType() == Material.IRON_ORE) {
                event.setDropItems(false);
                int coins = (int) (10 * Math.pow(2, coinGatheringLevel - 1));
                mPlayer.setCoins(mPlayer.getCoins() + coins);
                event.getPlayer().sendMessage(org.bukkit.ChatColor.GOLD + "+ " + coins + " coins!");
            }
        }
    }
}
