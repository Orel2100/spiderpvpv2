package com.jules.kitpvp.player;

import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MPlayer {

    private static Map<UUID, MPlayer> mPlayers = new HashMap<>();

    private UUID uuid;
    private Kit kit;
    private KitClass kitClass;
    private int coins = 0;
    private Map<UpgradeType, Integer> upgradeLevels = new HashMap<>();

    public MPlayer(UUID uuid) {
        this.uuid = uuid;
        mPlayers.put(uuid, this);
    }

    public static MPlayer getMPlayer(UUID uuid) {
        if(!mPlayers.containsKey(uuid)) {
            new MPlayer(uuid);
        }
        return mPlayers.get(uuid);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public int getUpgradeLevel(UpgradeType type) {
        return upgradeLevels.getOrDefault(type, 1);
    }

    public void setUpgradeLevel(UpgradeType type, int level) {
        upgradeLevels.put(type, level);
    }

    public void setKit(Kit kit) {
        this.kit = kit;
        try {
            this.kitClass = kit.getKitClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Kit getKit() {
        return kit;
    }

    public KitClass getKitClass() {
        return kitClass;
    }

    public void setKitClass(KitClass kitClass) {
        this.kitClass = kitClass;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Map<UpgradeType, Integer> getUpgradeLevels() {
        return upgradeLevels;
    }

    public void setUpgradeLevels(Map<UpgradeType, Integer> upgradeLevels) {
        this.upgradeLevels = upgradeLevels;
    }
}
