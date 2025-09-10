package com.jules.kitpvp.listeners;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.jules.kitpvp.kits.Kit;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    private final KitPVP plugin;

    public PlayerDamageListener(KitPVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());
            if (mPlayer.getKitClass() != null) {
                mPlayer.getKitClass().onDamage(event);
            }
        }

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            Entity damager = e.getDamager();
            Player attacker = null;
            boolean isMelee = false;
            boolean isBow = false;

            if (damager instanceof Player) {
                attacker = (Player) damager;
                isMelee = true;
            } else if (damager instanceof Arrow) {
                Arrow arrow = (Arrow) damager;
                if (arrow.getShooter() instanceof Player) {
                    attacker = (Player) arrow.getShooter();
                    isBow = true;
                }
            }

            if (attacker != null) {
                MPlayer mAttacker = MPlayer.getMPlayer(attacker.getUniqueId());
                if (mAttacker.getKitClass() != null) {
                    mAttacker.getKitClass().onDamageByEntity(e);

                    int eph = 0;
                    Kit kit = mAttacker.getKit();
                    if (kit != null) {
                        switch (kit) {
                            case ARCANIST: eph = 36; break;
                            case CREEPER: eph = 20; break;
                            case HUNTER:
                                if (isMelee) eph = 4;
                                if (isBow) eph = 8;
                                break;
                            case DREADLORD: eph = 12; break;
                            case PIGMAN: eph = 10; break;
                            case SHAMAN: eph = 8; break;
                            case SKELETON:
                                if (isBow) eph = 20;
                                break;
                            case ENDERMAN: eph = 20; break;
                            case HEROBRINE: eph = 25; break;
                            case GOLEM: eph = 12; break;
                        }
                    }

                    if (eph > 0) {
                        com.jules.kitpvp.util.Utils.addLevel(attacker, eph);
                    }
                }
            }
        }
    }
}
