package com.jules.kitpvp;

import com.jules.kitpvp.arena.ArenaManager;
import com.jules.kitpvp.kits.UpgradeManager;
import com.jules.kitpvp.config.Configuration;
import com.jules.kitpvp.commands.LobbyCommand;
import com.jules.kitpvp.commands.Set1v1Command;
import com.jules.kitpvp.commands.SetLobbyCommand;
import com.jules.kitpvp.commands.UpgradeCommand;
import com.jules.kitpvp.duel.DuelManager;
import com.jules.kitpvp.game.GameManager;
import com.jules.kitpvp.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class KitPVP extends JavaPlugin {

    private static KitPVP instance;
    private static ArrayList<String> playing = new ArrayList<>();

    private Configuration configuration;
    private ArenaManager arenaManager;
    private UpgradeManager upgradeManager;
    private DuelManager duelManager;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;
        this.configuration = new Configuration(this);
        this.configuration.load();

        this.arenaManager = new ArenaManager();
        this.upgradeManager = new UpgradeManager();
        this.duelManager = new DuelManager();
        this.gameManager = new GameManager(this);

        getCommand("arena").setExecutor(new com.jules.kitpvp.commands.ArenaCommands(this, arenaManager));
        getCommand("class").setExecutor(new com.jules.kitpvp.commands.ClassCommand());
        getCommand("1v1").setExecutor(new com.jules.kitpvp.commands.CommandOneonOne(duelManager));
        getCommand("coins").setExecutor(new com.jules.kitpvp.commands.CoinCommand());
        getCommand("setlobby").setExecutor(new SetLobbyCommand());
        getCommand("set1v1").setExecutor(new Set1v1Command());
        getCommand("setspawn").setExecutor(new com.jules.kitpvp.commands.SetSpawnCommand());
        getCommand("upgrade").setExecutor(new UpgradeCommand());
        getCommand("lobby").setExecutor(new LobbyCommand());

        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new OneOnOneListener(duelManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, duelManager), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new AssistListener(this), this);
        getServer().getPluginManager().registerEvents(new TempBlocksListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityShootBowListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new ProjectileHitListener(), this);
        getServer().getPluginManager().registerEvents(new HangingBreakListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerItemConsumeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);


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

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public UpgradeManager getUpgradeManager() {
        return upgradeManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
