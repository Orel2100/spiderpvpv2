package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class SquidSplash implements Ability {

    @Override
    public String getName() {
        return "Squid Splash";
    }

    @Override
    public String getDescription() {
        return "Heal yourself in water.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for squid splash logic
        player.sendMessage("You use Squid Splash!");
    }
}
