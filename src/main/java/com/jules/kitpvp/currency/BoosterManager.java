package com.jules.kitpvp.currency;

import com.jules.kitpvp.KitPVP;
import org.bukkit.scheduler.BukkitRunnable;

public class BoosterManager {

    private double multiplier = 1.0;
    private boolean active = false;

    public void activateBooster(double multiplier, int duration) {
        this.multiplier = multiplier;
        this.active = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                active = false;
                BoosterManager.this.multiplier = 1.0;
            }
        }.runTaskLater(KitPVP.getPlugin(KitPVP.class), duration * 20L);
    }

    public double getMultiplier() {
        return active ? multiplier : 1.0;
    }

    public boolean isBoosterActive() {
        return active;
    }
}
