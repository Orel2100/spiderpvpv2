package com.jules.kitpvp.kits;

import com.jules.kitpvp.abilities.Ability;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class KitClass {

    private String name;
    private String description;
    private ItemStack icon;
    private List<Ability> abilities;
    private List<Upgrade> upgrades;

    public KitClass(String name, String description, ItemStack icon, List<Ability> abilities, List<Upgrade> upgrades) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.abilities = abilities;
        this.upgrades = upgrades;
    }

    public abstract void applyKit(Player player);
    public abstract void triggerAbility(Player player);
    public abstract void onKill(Player killer, Player victim);
    public abstract void onDeath(Player player);
    public abstract List<ItemStack> getStartingItems(Player player);

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }
}
