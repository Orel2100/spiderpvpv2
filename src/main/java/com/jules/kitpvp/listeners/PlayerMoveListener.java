package com.jules.kitpvp.listeners;

import com.jules.kitpvp.abilities.Leap;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (Leap.leaping.contains(player) && player.isOnGround()) {
            Leap.leaping.remove(player);
            MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
            if (mPlayer.getKitClass() != null) {
                mPlayer.getKitClass().onLand(player);
            }
        }
    }
}
