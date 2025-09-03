package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public class IronPunch implements Ability {

    @Override
    public String getName() {
        return "Iron Punch";
    }

    @Override
    public String getDescription() {
        return "A powerful punch that knocks enemies back.";
    }

    @Override
    public void execute(Player player) {
        // Placeholder for iron punch logic
        player.sendMessage("You use Iron Punch!");
    }
}
