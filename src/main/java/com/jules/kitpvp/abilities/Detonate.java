package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class Detonate implements Ability {

    @Override
    public String getName() {
        return "Detonate";
    }

    @Override
    public String getDescription() {
        return "Explodes around you.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for detonation logic
        player.sendMessage("You detonate!");
    }
}
