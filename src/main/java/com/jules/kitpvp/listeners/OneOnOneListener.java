package com.jules.kitpvp.listeners;

import com.jules.kitpvp.duel.DuelManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OneOnOneListener implements Listener {

    private final DuelManager duelManager;

    public OneOnOneListener(DuelManager duelManager) {
        this.duelManager = duelManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // End duel if player is in one
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // End duel if player is in one
    }
}
