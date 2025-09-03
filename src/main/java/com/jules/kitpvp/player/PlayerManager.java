package com.jules.kitpvp.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, PlayerData> players = new HashMap<>();

    public void addPlayer(Player player) {
        players.put(player.getUniqueId(), new PlayerData(player.getUniqueId()));
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public PlayerData getPlayerData(Player player) {
        return players.get(player.getUniqueId());
    }
}
