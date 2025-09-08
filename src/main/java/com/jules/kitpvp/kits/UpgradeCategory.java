package com.jules.kitpvp.kits;

public enum UpgradeCategory {
    ABILITY("Ability", 0),
    PASSIVE_1("Passive 1", 1),
    PASSIVE_2("Passive 2", 2),
    KIT("Kit", 3);

    private final String name;
    private final int row;

    UpgradeCategory(String name, int row) {
        this.name = name;
        this.row = row;
    }

    public String getName() {
        return name;
    }

    public int getRow() {
        return row;
    }
}
