package com.jules.kitpvp.currency;

import com.jules.kitpvp.player.PlayerData;
import com.jules.kitpvp.player.PlayerManager;
import org.bukkit.entity.Player;

public class CoinManager {

    private final PlayerManager playerManager;

    public CoinManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public double getBalance(Player player) {
        PlayerData playerData = playerManager.getPlayerData(player);
        return playerData != null ? playerData.getCoins() : 0;
    }

    public void setBalance(Player player, double amount) {
        PlayerData playerData = playerManager.getPlayerData(player);
        if (playerData != null) {
            playerData.setCoins(amount);
        }
    }

    public void addBalance(Player player, double amount) {
        PlayerData playerData = playerManager.getPlayerData(player);
        if (playerData != null) {
            playerData.setCoins(playerData.getCoins() + amount);
        }
    }

    public void removeBalance(Player player, double amount) {
        PlayerData playerData = playerManager.getPlayerData(player);
        if (playerData != null) {
            playerData.setCoins(playerData.getCoins() - amount);
        }
    }
}
