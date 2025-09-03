package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class ShadowBurst implements Ability {

    @Override
    public String getName() {
        return "Shadow Burst";
    }

    @Override
    public String getDescription() {
        return "Unleashes a burst of shadowy projectiles.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for shadow burst logic
        player.sendMessage("You unleash a burst of shadows!");
    }
}
