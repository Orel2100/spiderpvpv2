package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class ImmolatingBurst implements Ability {

    @Override
    public String getName() {
        return "Immolating Burst";
    }

    @Override
    public String getDescription() {
        return "Creates a burst of fireballs.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for fireball burst logic
        player.sendMessage("You unleash a burst of fireballs!");
    }
}
