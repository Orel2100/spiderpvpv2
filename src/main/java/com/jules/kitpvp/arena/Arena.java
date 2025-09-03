package com.jules.kitpvp.arena;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.ArrayList;

public class Arena {

    private final String id;
    private final List<Location> spawnPoints;
    private final List<Player> players;

    public Arena(String id) {
        this.id = id;
        this.spawnPoints = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<Location> getSpawnPoints() {
        return spawnPoints;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void addSpawnPoint(Location location) {
        spawnPoints.add(location);
    }

    public void broadcast(String message) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    public void reset() {
        players.clear();
    }
}
