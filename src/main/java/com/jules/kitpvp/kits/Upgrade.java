package com.jules.kitpvp.kits;

public class Upgrade {

    private final UpgradeType type;
    private int level;
    private final double cost;

    public Upgrade(UpgradeType type, int level, double cost) {
        this.type = type;
        this.level = level;
        this.cost = cost;
    }

    public UpgradeType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getCost() {
        return cost;
    }
}
