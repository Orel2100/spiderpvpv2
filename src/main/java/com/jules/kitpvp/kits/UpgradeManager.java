package com.jules.kitpvp.kits;

import com.jules.kitpvp.gui.UpgradeGUI;
import com.jules.kitpvp.kits.classes.MegaWallsClass;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class UpgradeManager {

    public void attemptPurchase(Player player, String itemName) {
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
        if (!(mPlayer.getKitClass() instanceof MegaWallsClass)) {
            return;
        }

        MegaWallsClass kit = (MegaWallsClass) mPlayer.getKitClass();
        Map<UpgradeCategory, List<Upgrade>> upgrades = kit.getUpgrades();

        for (List<Upgrade> categoryUpgrades : upgrades.values()) {
            for (Upgrade upgrade : categoryUpgrades) {
                int currentLevel = mPlayer.getUpgradeLevel(upgrade);
                if (currentLevel < upgrade.getMaxLevel()) {
                    String upgradeName = ChatColor.stripColor(itemName).replaceAll(" [0-9]+$", "");
                    if (upgrade.getName().equals(upgradeName)) {
                        int cost = upgrade.getCost(currentLevel + 1);
                        if (mPlayer.getCoins() >= cost) {
                            mPlayer.setCoins(mPlayer.getCoins() - cost);
                            mPlayer.setUpgradeLevel(upgrade, currentLevel + 1);
                            player.sendMessage(ChatColor.GREEN + "You have purchased " + upgrade.getName() + " " + (currentLevel + 1) + "!");
                            new UpgradeGUI(mPlayer).open(player);
                        } else {
                            player.sendMessage(ChatColor.RED + "You cannot afford this!");
                        }
                        return;
                    }
                }
            }
        }
    }
}
