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

        String strippedItemName = ChatColor.stripColor(itemName);
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

        for (List<Upgrade> categoryUpgrades : upgrades.values()) {
            for (Upgrade upgrade : categoryUpgrades) {
                if (upgrade.getName().equals(upgradeName)) {
                    int currentLevel = mPlayer.getUpgradeLevel(upgrade);
                    if (tier == currentLevel + 1) {
                        int cost = upgrade.getCost(tier);
                        if (mPlayer.getCoins() >= cost) {
                            mPlayer.setCoins(mPlayer.getCoins() - cost);
                            mPlayer.setUpgradeLevel(upgrade, tier);
                            player.sendMessage(ChatColor.GREEN + "You have purchased " + upgrade.getName() + " " + tier + "!");
                            new UpgradeGUI(mPlayer).open(player);
                        } else {
                            player.sendMessage(ChatColor.RED + "You cannot afford this!");
                        }
                    }
                    return;
                }
            }
        }
    }
}
