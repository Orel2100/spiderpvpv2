package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class Wrath implements Ability {

    @Override
    public String getName() {
        return "Wrath";
    }

    @Override
    public String getDescription() {
        return "Deals damage in an area around you.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for wrath logic
        player.sendMessage("You unleash your wrath!");
    }
}
