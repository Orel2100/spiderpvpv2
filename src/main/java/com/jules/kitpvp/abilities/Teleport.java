package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class Teleport implements Ability {

    @Override
    public String getName() {
        return "Teleport";
    }

    @Override
    public String getDescription() {
        return "Teleports to the nearest enemy.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for teleport logic
        player.sendMessage("You teleport!");
    }
}
