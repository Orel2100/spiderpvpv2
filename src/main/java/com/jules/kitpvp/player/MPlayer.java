package com.jules.kitpvp.player;

import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
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
    private int gatheringLevel = 0;
    private Map<String, Integer> upgradeLevels = new HashMap<>();

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

    public int getUpgradeLevel(Upgrade upgrade) {
        return upgradeLevels.getOrDefault(upgrade.getName(), 0);
    }

    public void setUpgradeLevel(Upgrade upgrade, int level) {
        upgradeLevels.put(upgrade.getName(), level);
    }

    public void setUpgradeLevel(String name, int level) {
        upgradeLevels.put(name, level);
    }

    public Map<String, Integer> getUpgradeLevels() {
        return upgradeLevels;
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

    public int getGatheringLevel() {
        return gatheringLevel;
    }

    public void setGatheringLevel(int gatheringLevel) {
        this.gatheringLevel = gatheringLevel;
    }
}
