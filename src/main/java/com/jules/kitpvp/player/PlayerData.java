package com.jules.kitpvp.player;

import com.jules.kitpvp.kits.KitClass;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private KitClass kit;
    private int kills;
    private int deaths;
    private double coins;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public KitClass getKit() {
        return kit;
    }

    public void setKit(KitClass kit) {
        this.kit = kit;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }
}
