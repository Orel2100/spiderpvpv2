package com.jules.kitpvp.listeners;

import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getShooter() instanceof Player) {
            Player shooter = (Player) projectile.getShooter();
            MPlayer mPlayer = MPlayer.getMPlayer(shooter.getUniqueId());
            if (mPlayer.getKitClass() != null) {
                mPlayer.getKitClass().onProjectileHit(event);
            }
        }
    }
}
