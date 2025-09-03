package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class HomingTask implements Ability {

    @Override
    public String getName() {
        return "Homing Task";
    }

    @Override
    public String getDescription() {
        return "Your arrows will home in on enemies.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for homing arrow logic
        player.sendMessage("Your arrows are now homing!");
    }
}
