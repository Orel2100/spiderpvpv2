package com.jules.kitpvp.kits;

public class Upgrade {

    private UpgradeType type;
    private int maxLevel;

    public Upgrade(UpgradeType type, int maxLevel) {
        this.type = type;
        this.maxLevel = maxLevel;
    }

    public UpgradeType getType() {
        return type;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
