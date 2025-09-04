package com.jules.kitpvp.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MPlayerManager {

    private static PlayerManager playerManager;

    public MPlayerManager(PlayerManager playerManager) {
        MPlayerManager.playerManager = playerManager;
    }

    public static MPlayer getMPlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return null;
        }
        PlayerData playerData = playerManager.getPlayerData(player);
        if (playerData == null) {
            return null;
        }
        return new MPlayer(playerData);
    }
}
