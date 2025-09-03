package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.duel.DuelManager;
import com.jules.kitpvp.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final KitPVP plugin;
    private final PlayerManager playerManager;
    private final DuelManager duelManager;

    public PlayerDeathListener(KitPVP plugin, PlayerManager playerManager, DuelManager duelManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.duelManager = duelManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Handle FFA death, duel death, rewards, etc.
    }
}
