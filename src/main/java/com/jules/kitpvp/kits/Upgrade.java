package com.jules.kitpvp.kits;

import org.bukkit.Material;

import java.util.List;

public class Upgrade {

    private String name;
    private String description;
    private int maxLevel;
    private List<Integer> costs;
    private List<Material> materials;

    public Upgrade(String name, String description, int maxLevel, List<Integer> costs, List<Material> materials) {
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
        this.costs = costs;
        this.materials = materials;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getCost(int level) {
        if (level > 0 && level <= costs.size()) {
            return costs.get(level - 1);
        }
        return -1;
    }

    public Material getMaterial(int level) {
        if (level > 0 && level <= materials.size()) {
            return materials.get(level - 1);
        }
        return Material.BEDROCK; // Fallback material
    }
}
