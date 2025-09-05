package com.jules.kitpvp.kits;

import com.jules.kitpvp.gui.UpgradeGUI;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class UpgradeManager {

    private List<Integer> costs = Arrays.asList(100, 200, 300, 500, 1000, 2000, 3000, 5000, 10000); // 9 levels

    public int getCost(int level) {
        if (level > costs.size()) {
            return -1; // max level reached or invalid level
        }
        return costs.get(level - 1);
    }

    public void upgrade(MPlayer mPlayer, UpgradeType type) {
        int currentLevel = mPlayer.getUpgradeLevel(type);
        int maxLevel = -1;
        for(Upgrade upgrade : mPlayer.getKitClass().getUpgrades()) {
            if(upgrade.getType() == type) {
                maxLevel = upgrade.getMaxLevel();
                break;
            }
        }

        if (maxLevel == -1) {
            mPlayer.getPlayer().sendMessage(ChatColor.RED + "This item is not upgradeable!");
            return;
        }

        if (currentLevel >= maxLevel) {
            mPlayer.getPlayer().sendMessage(ChatColor.RED + "You have reached the maximum level for this upgrade!");
            return;
        }

        int cost = getCost(currentLevel + 1);
        if (cost == -1) {
            mPlayer.getPlayer().sendMessage(ChatColor.RED + "This item cannot be upgraded further!");
            return;
        }

        if (mPlayer.getCoins() >= cost) {
            mPlayer.setCoins(mPlayer.getCoins() - cost);
            mPlayer.setUpgradeLevel(type, currentLevel + 1);
            mPlayer.getPlayer().sendMessage(ChatColor.GREEN + "You have successfully upgraded " + type.name() + " to level " + (currentLevel + 1) + "!");
            // Re-open GUI to show changes
            new UpgradeGUI(mPlayer).open(mPlayer.getPlayer());
        } else {
            mPlayer.getPlayer().sendMessage(ChatColor.RED + "You do not have enough coins!");
        }
    }
}
