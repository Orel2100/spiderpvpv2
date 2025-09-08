package com.jules.kitpvp.io;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {

    private final KitPVP plugin;
    private final File dataFolder;

    public PlayerDataManager(KitPVP plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public void saveData(MPlayer mPlayer) {
        File playerFile = new File(dataFolder, mPlayer.getUuid().toString() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        config.set("coins", mPlayer.getCoins());
        config.set("upgrades", mPlayer.getUpgradeLevels());

        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(MPlayer mPlayer) {
        File playerFile = new File(dataFolder, mPlayer.getUuid().toString() + ".yml");
        if (!playerFile.exists()) {
            return; // New player
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        mPlayer.setCoins(config.getInt("coins", 0));

        if (config.isConfigurationSection("upgrades")) {
            for (String upgradeName : config.getConfigurationSection("upgrades").getKeys(false)) {
                mPlayer.setUpgradeLevel(upgradeName, config.getInt("upgrades." + upgradeName));
            }
        }
    }
}
