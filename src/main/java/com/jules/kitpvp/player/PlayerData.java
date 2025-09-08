package com.jules.kitpvp.player;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.config.Configuration;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerData {

    public static void saveData(Player player) {
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
        Configuration config = new Configuration(KitPVP.getInstance(), "playerdata/" + player.getUniqueId().toString());

        config.set("coins", mPlayer.getCoins());
        config.set("upgrades", mPlayer.getUpgradeLevels());

        config.saveConfig();
    }

    public static void loadData(Player player) {
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
        Configuration config = new Configuration(KitPVP.getInstance(), "playerdata/" + player.getUniqueId().toString());

        mPlayer.setCoins(config.getInt("coins"));
        if (config.getConfiguration().isConfigurationSection("upgrades")) {
            for (String upgradeName : config.getConfiguration().getConfigurationSection("upgrades").getKeys(false)) {
                mPlayer.setUpgradeLevel(upgradeName, config.getInt("upgrades." + upgradeName));
            }
        }

        // Ensure every player has at least level 1 of Coin Gathering if they don't have it already.
        if (mPlayer.getUpgradeLevel("Coin Gathering") == 0) {
            mPlayer.setUpgradeLevel("Coin Gathering", 1);
        }
    }
}
