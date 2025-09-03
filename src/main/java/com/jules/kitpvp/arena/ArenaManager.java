package com.jules.kitpvp.arena;

import java.util.HashMap;
import java.util.Map;

public class ArenaManager {

    private final Map<String, Arena> arenas = new HashMap<>();

    public void createArena(String id) {
        arenas.put(id, new Arena(id));
    }

    public Arena getArena(String id) {
        return arenas.get(id);
    }

    public void removeArena(String id) {
        arenas.remove(id);
    }
}
