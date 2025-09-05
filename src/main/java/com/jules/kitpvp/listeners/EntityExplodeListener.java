package com.jules.kitpvp.listeners;

import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof WitherSkull) {
            WitherSkull ws = (WitherSkull) event.getEntity();
            if (ws.getShooter() instanceof Player) {
                Player shooter = (Player) ws.getShooter();
                MPlayer mPlayer = MPlayer.getMPlayer(shooter.getUniqueId());
                if (mPlayer.getKitClass() != null) {
                    mPlayer.getKitClass().onEntityExplode(event);
                }
            }
        }
    }
}
