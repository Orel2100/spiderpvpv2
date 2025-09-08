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
        if (config.get("upgrades") != null) {
            for (String upgradeName : config.getConfiguration().getConfigurationSection("upgrades").getKeys(false)) {
                mPlayer.setUpgradeLevel(upgradeName, config.getInt("upgrades." + upgradeName));
            }
        }
    }
}
