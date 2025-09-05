package com.jules.kitpvp.listeners;

import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
        if (mPlayer.getKitClass() != null) {
            mPlayer.getKitClass().onInteract(player, event);
        }
    }
}
