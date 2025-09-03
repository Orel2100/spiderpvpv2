package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class Beam implements Ability {

    @Override
    public String getName() {
        return "Beam";
    }

    @Override
    public String getDescription() {
        return "Shoots a damaging beam.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for beam logic
        player.sendMessage("You shoot a beam!");
    }
}
