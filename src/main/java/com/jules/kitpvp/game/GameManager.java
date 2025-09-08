package com.jules.kitpvp.game;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.config.Configuration;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {

    private final KitPVP plugin;
    private final List<Location> spawns = new ArrayList<>();
    private final Random random = new Random();
    private final Configuration locationsConfig;

    public GameManager(KitPVP plugin) {
        this.plugin = plugin;
        this.locationsConfig = Configuration.getConfig("locations");
        loadSpawns();
    }

    public void loadSpawns() {
        spawns.clear();
        FileConfiguration config = locationsConfig.getConfiguration();
        ConfigurationSection spawnsSection = config.getConfigurationSection("spawns");
        if (spawnsSection != null) {
            for (String key : spawnsSection.getKeys(false)) {
                Location loc = spawnsSection.getLocation(key);
                if (loc != null) {
                    spawns.add(loc);
                }
            }
        }
    }

    public void saveSpawns() {
        FileConfiguration config = locationsConfig.getConfiguration();
        config.set("spawns", null); // Clear existing spawns
        ConfigurationSection spawnsSection = config.createSection("spawns");
        for (int i = 0; i < spawns.size(); i++) {
            spawnsSection.set(String.valueOf(i + 1), spawns.get(i));
        }
        locationsConfig.saveConfig();
    }

    public void addSpawn(Location location) {
        spawns.add(location);
        saveSpawns();
    }

    public Location getRandomSpawn() {
        if (spawns.isEmpty()) {
            return null;
        }
        return spawns.get(random.nextInt(spawns.size()));
    }

    public List<Location> getSpawns() {
        return spawns;
    }
}
