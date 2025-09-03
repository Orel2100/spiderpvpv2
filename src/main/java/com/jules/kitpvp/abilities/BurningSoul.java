package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class BurningSoul implements Ability {

    @Override
    public String getName() {
        return "Burning Soul";
    }

    @Override
    public String getDescription() {
        return "Unleashes a wave of fire around you.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for burning soul logic
        player.sendMessage("You unleash your burning soul!");
    }
}
