package com.jules.kitpvp.kits;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.classes.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassManager {

    private static final Map<String, KitClass> classes = new HashMap<>();
    private final Map<Player, KitClass> playerClasses = new HashMap<>();

    private static void addClass(KitClass kitClass) {
        classes.put(kitClass.getName(), kitClass);
    }

    public static void registerClasses() {
        classes.clear();
        addClass(new Skeleton());
        addClass(new Zombie());
        addClass(new Creeper());
        addClass(new Enderman());
        addClass(new Herobrine());
        addClass(new Spider());
        addClass(new Squid());
        addClass(new Dreadlord());
        addClass(new Shaman());
        addClass(new Arcanist());
        addClass(new Golem());
        addClass(new Pigman());
        addClass(new Hunter());
        addClass(new Pirate());
        addClass(new Blaze());
        for (KitClass kitClass : classes.values()) {
            Bukkit.getPluginManager().registerEvents(kitClass, KitPVP.getInstance());
        }
    }

    public static KitClass getClass(String name) {
        return classes.get(name);
    }

    public static ArrayList<KitClass> getClasses() {
        return new ArrayList<>(classes.values());
    }

    public void setPlayerClass(Player player, KitClass kitClass) {
        playerClasses.put(player, kitClass);
        kitClass.apply(player);
    }

    public KitClass getPlayerClass(Player player) {
        return playerClasses.get(player);
    }
}
