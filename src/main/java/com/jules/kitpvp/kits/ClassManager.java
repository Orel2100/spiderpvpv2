package com.jules.kitpvp.kits;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ClassManager {

    private final Map<String, KitClass> classes = new HashMap<>();
    private final Map<Player, KitClass> playerClasses = new HashMap<>();

    public void registerClass(KitClass kitClass) {
        classes.put(kitClass.getName(), kitClass);
    }

    public KitClass getClass(String name) {
        return classes.get(name);
    }

    public void setPlayerClass(Player player, KitClass kitClass) {
        playerClasses.put(player, kitClass);
        kitClass.applyKit(player);
    }

    public KitClass getPlayerClass(Player player) {
        return playerClasses.get(player);
    }
}
