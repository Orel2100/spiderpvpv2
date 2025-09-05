package com.jules.kitpvp.listeners;

import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;

public class HangingBreakListener implements Listener {

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        if (event instanceof HangingBreakByEntityEvent) {
            HangingBreakByEntityEvent entityEvent = (HangingBreakByEntityEvent) event;
            if (entityEvent.getRemover() instanceof Player) {
                Player player = (Player) entityEvent.getRemover();
                MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
                if (mPlayer.getKitClass() != null) {
                    mPlayer.getKitClass().onHangingBreak(event);
                }
            }
        }
    }
}
