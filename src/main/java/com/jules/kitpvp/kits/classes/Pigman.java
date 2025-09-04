package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.kits.HitType;
import com.jules.kitpvp.kits.KitClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class Pigman extends KitClass {

    public static ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Pigman";
    }

    @EventHandler
    public void secondSkill(EntityDamageEvent e) {
        if(e.isCancelled()) return;
        if(e.getEntity() instanceof Player) {
            final Player p = (Player)e.getEntity();
            if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
            if(cd.contains(p.getName())) return;
            if(p.getHealth() <= 10) {
                double damage = 1.5;
                for(int i = 0; i < 9; i++) {
                    damage += 0.5;
                }
                cd.add(p.getName());
                int time = (int) (20 * damage);
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,time,1));

                //Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                //
                //    @Override
                //    public void run() {
                //        cd.remove(p.getName());
                //
                //    }
                //}, 20 * 30);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction().name().contains("RIGHT") == false) return;
        if(p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(!com.jules.kitpvp.util.Utils.isUsingSword(p.getItemInHand())) return;
        if(p.getLevel() < 100) return;
        com.jules.kitpvp.kits.Upgrade upgrade = new com.jules.kitpvp.kits.Upgrade(p, this, com.jules.kitpvp.kits.UpgradeType.ABILITY);
        com.jules.kitpvp.abilities.BurningSoul.use(p, upgrade.getCurrentUpgrade());
        p.setLevel(0);
        p.setExp(0);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("A resilient fighter that gets","stronger in combat.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.PORKCHOP);
    }

    @Override
    public int getPrice() {
        return 1000;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.GOLDEN_SWORD));
        return items;
    }

    @Override
    public int getXPPerHit() {
        return 10;
    }

    @Override
    public int getUpgradePrice(int level) {
        return 100 * level;
    }

    @Override
    public String getAbilityName() {
        return "Burning Soul";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Gain strength and resistance","§7for a short duration.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
