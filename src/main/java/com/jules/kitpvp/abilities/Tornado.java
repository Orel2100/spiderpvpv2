package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class Tornado implements Ability {

    @Override
    public String getName() {
        return "Tornado";
    }

    @Override
    public String getDescription() {
        return "Summons a tornado.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for tornado logic
        player.sendMessage("You summon a tornado!");
    }
}
