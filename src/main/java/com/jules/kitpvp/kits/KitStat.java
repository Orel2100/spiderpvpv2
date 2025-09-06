package com.jules.kitpvp.kits;

public class KitStat {

    private int damage;
    private int defense;
    private int mobility;
    private int energy;
    private int difficulty;

    public KitStat(int damage, int defense, int mobility, int energy, int difficulty) {
        this.damage = damage;
        this.defense = defense;
        this.mobility = mobility;
        this.energy = energy;
        this.difficulty = difficulty;
    }

    public int getDamage() {
        return damage;
    }

    public int getDefense() {
        return defense;
    }

    public int getMobility() {
        return mobility;
    }

    public int getEnergy() {
        return energy;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
