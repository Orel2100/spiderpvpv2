package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.duel.DuelManager;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final KitPVP plugin;
    private final DuelManager duelManager;

    public PlayerDeathListener(KitPVP plugin, DuelManager duelManager) {
        this.plugin = plugin;
        this.duelManager = duelManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());

        if (mPlayer.getKitClass() != null) {
            mPlayer.getKitClass().onDeath(event);
        }

        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            MPlayer mKiller = MPlayer.getMPlayer(killer.getUniqueId());
            if (mKiller.getKitClass() != null) {
                mKiller.getKitClass().onKill(killer, player);
            }
        }
        // Handle FFA death, duel death, rewards, etc.
    }
}
