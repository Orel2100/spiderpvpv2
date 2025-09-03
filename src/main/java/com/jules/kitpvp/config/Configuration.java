package com.jules.kitpvp.config;

import com.jules.kitpvp.KitPVP;
import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {

    private final KitPVP plugin;
    private FileConfiguration config;

    public Configuration(KitPVP plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        // Load settings here
    }
}
