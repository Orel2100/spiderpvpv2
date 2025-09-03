package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class CannonFire implements Ability {

    @Override
    public String getName() {
        return "Cannon Fire";
    }

    @Override
    public String getDescription() {
        return "Fires a TNT cannon shot.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for cannon fire logic
        player.sendMessage("You fire a cannon!");
    }
}
