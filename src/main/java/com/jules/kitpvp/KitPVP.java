package com.jules.kitpvp;

import com.jules.kitpvp.arena.ArenaManager;
import com.jules.kitpvp.kits.ClassManager;
import com.jules.kitpvp.currency.CoinManager;
import com.jules.kitpvp.currency.BoosterManager;
import com.jules.kitpvp.kits.UpgradeManager;
import com.jules.kitpvp.config.Configuration;
import com.jules.kitpvp.duel.DuelManager;
import com.jules.kitpvp.player.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class KitPVP extends JavaPlugin {

    private static KitPVP instance;
    private static ArrayList<String> playing = new ArrayList<>();

    private Configuration configuration;
    private ArenaManager arenaManager;
    private ClassManager classManager;
    private CoinManager coinManager;
    private BoosterManager boosterManager;
    private UpgradeManager upgradeManager;
    private DuelManager duelManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        instance = this;
        this.configuration = new Configuration(this);
        this.configuration.load();

        this.playerManager = new PlayerManager();
        this.arenaManager = new ArenaManager();
        this.classManager = new ClassManager();
        new com.jules.kitpvp.player.MPlayerManager(playerManager);
        ClassManager.registerClasses();
        this.coinManager = new CoinManager(playerManager);
        this.boosterManager = new BoosterManager();
        this.upgradeManager = new UpgradeManager();
        this.duelManager = new DuelManager();

        getCommand("arena").setExecutor(new com.jules.kitpvp.commands.ArenaCommands(this, arenaManager));
        getCommand("class").setExecutor(new com.jules.kitpvp.commands.ClassCommand(classManager));
        getCommand("coins").setExecutor(new com.jules.kitpvp.commands.CoinCommand(coinManager));
        getCommand("booster").setExecutor(new com.jules.kitpvp.commands.BoosterCommand(boosterManager));
        getCommand("1v1").setExecutor(new com.jules.kitpvp.commands.CommandOneonOne(duelManager));

        getServer().getPluginManager().registerEvents(new com.jules.kitpvp.listeners.GUIListener(classManager), this);
        getServer().getPluginManager().registerEvents(new com.jules.kitpvp.listeners.OneOnOneListener(duelManager), this);
        getServer().getPluginManager().registerEvents(new com.jules.kitpvp.listeners.PlayerJoinListener(this, playerManager), this);
        getServer().getPluginManager().registerEvents(new com.jules.kitpvp.listeners.PlayerDeathListener(this, playerManager, duelManager), this);
        getServer().getPluginManager().registerEvents(new com.jules.kitpvp.listeners.PlayerDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new com.jules.kitpvp.listeners.PlayerInteractListener(this, classManager), this);
        getServer().getPluginManager().registerEvents(new com.jules.kitpvp.listeners.AssistListener(this), this);
        getServer().getPluginManager().registerEvents(new com.jules.kitpvp.listeners.TempBlocksListener(this), this);

        getLogger().info("KitPVP has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("KitPVP has been disabled!");
    }

    public static KitPVP getInstance() {
        return instance;
    }

    public static ArrayList<String> getPlaying() {
        return playing;
    }
}
