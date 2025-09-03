package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class ExplosiveArrow implements Ability {

    @Override
    public String getName() {
        return "Explosive Arrow";
    }

    @Override
    public String getDescription() {
        return "Your next arrow explodes on impact.";
    }

    @Override
    public void execute(Player player) {
        // This will be implemented more thoroughly later.
        // For now, it's a placeholder.
        player.sendMessage("Your next arrow will be explosive!");
    }
}
