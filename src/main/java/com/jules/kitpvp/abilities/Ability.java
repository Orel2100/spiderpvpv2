package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;

public interface Ability {

    String getName();

    String getDescription();

    void execute(Player player);
}
