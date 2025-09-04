package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.kits.HitType;
import com.jules.kitpvp.kits.KitClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Enderman extends KitClass {
    @Override
    public String getName() {
        return "Enderman";
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        final Player p = e.getPlayer();
        if(e.getAction().name().contains("RIGHT") == false) return;
        if(p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(!com.jules.kitpvp.util.Utils.isUsingSword(p.getItemInHand())) return;
        if(p.getLevel() < 100) return;
        com.jules.kitpvp.kits.Upgrade upgrade = new com.jules.kitpvp.kits.Upgrade(p, this, com.jules.kitpvp.kits.UpgradeType.ABILITY);
        com.jules.kitpvp.abilities.Teleport.use(p, upgrade.getCurrentUpgrade());
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Enderman class is a master","of teleportation and surprise","attacks.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ENDER_PEARL);
    }

    @Override
    public int getPrice() {
        return 1000;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        items.put(0, sword);
        items.put(1, new ItemStack(Material.ENDER_PEARL, 16));
        return items;
    }

    @Override
    public int getXPPerHit() {
        return 15;
    }

    @Override
    public int getUpgradePrice(int level) {
        return 150 * level;
    }

    @Override
    public String getAbilityName() {
        return "Teleport";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Instantly teleport a short","§7distance.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
