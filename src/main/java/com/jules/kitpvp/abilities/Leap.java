package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class Leap implements Ability {

    @Override
    public String getName() {
        return "Leap";
    }

    @Override
    public String getDescription() {
        return "Leap into the air.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for leap logic
        player.sendMessage("You leap into the air!");
    }
}
