package com.jules.kitpvp.kits;

public enum UpgradeType {

    ABILITY("Ability"),
    KIT("KitUpgrade"),
    BLOCKS("Blocks"),
    TEAM("TeamOption");

    private String name;

    private UpgradeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
