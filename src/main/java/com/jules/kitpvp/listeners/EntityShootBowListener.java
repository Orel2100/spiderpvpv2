package com.jules.kitpvp.listeners;

import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityShootBowListener implements Listener {

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());

        if (mPlayer.getKitClass() != null) {
            mPlayer.getKitClass().onBowShoot(event);
        }
    }
}
