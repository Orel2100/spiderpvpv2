package com.jules.kitpvp.config;

import com.jules.kitpvp.KitPVP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private static final Map<String, Configuration> configs = new HashMap<>();
    private final KitPVP plugin;
    private FileConfiguration config;
    private File configFile;

    public Configuration(KitPVP plugin) {
        this.plugin = plugin;
    }

    public Configuration(KitPVP plugin, String fileName) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), fileName + ".yml");
        if (!configFile.exists()) {
            plugin.saveResource(fileName + ".yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void load() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        // Load settings here
    }

    public static Configuration getConfig(String name) {
        if (!configs.containsKey(name)) {
            configs.put(name, new Configuration(KitPVP.getInstance(), name));
        }
        return configs.get(name);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public void saveConfig() {
        if (this.configFile == null) {
            plugin.saveConfig();
        } else {
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Object get(String path) {
        return config.get(path);
    }
}
