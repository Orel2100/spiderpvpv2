package com.jules.kitpvp.kits;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class KitClass {

    private String name;
    private List<String> description;
    private int price;
    private ItemStack icon;
    private List<Upgrade> upgrades;

    public KitClass(String name, String[] description, int price, ItemStack icon, Upgrade... upgrades) {
        this.name = name;
        this.description = Arrays.asList(description);
        this.price = price;
        this.icon = icon;
        this.upgrades = Arrays.asList(upgrades);
    }

    public KitClass(String name, String[] description, int price, ItemStack icon) {
        this.name = name;
        this.description = Arrays.asList(description);
        this.price = price;
        this.icon = icon;
        this.upgrades = new ArrayList<>();
    }

    public abstract List<ItemStack> getStartingItems(Player p);
    public abstract List<PotionEffect> getPassiveEffects(Player p);
    public abstract Kit getKit();

    public void onDamage(EntityDamageEvent event) {}
    public void onDamageByEntity(EntityDamageByEntityEvent event) {}
    public void onKill(Player p, Player killed) {}
    public void onDeath(PlayerDeathEvent event) {}
    public void onInteract(Player p, PlayerInteractEvent event) {}
    public void onBowShoot(EntityShootBowEvent event) {}
    public void onBlockBreak(BlockBreakEvent event) {}
    public void onProjectileHit(ProjectileHitEvent event) {}
    public void onHangingBreak(HangingBreakEvent event) {}
    public void onEntityExplode(EntityExplodeEvent event) {}
    public void onItemConsume(PlayerItemConsumeEvent event) {}

    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new java.util.HashMap<>();
    }

    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        return Arrays.asList(upgrade.getDescription());
    }
}
