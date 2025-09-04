package com.jules.kitpvp.team;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private final List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getPlayers() {
        return players;
    }
}
