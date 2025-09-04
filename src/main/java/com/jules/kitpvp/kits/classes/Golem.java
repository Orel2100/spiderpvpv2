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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Golem extends KitClass {

    public ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Golem";
    }

    @EventHandler
    public void kill(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            final Player killer = (Player)e.getEntity().getKiller();
            com.jules.kitpvp.player.MPlayer player = com.jules.kitpvp.player.MPlayerManager.getMPlayer(killer.getName());
            if(player.getCurrentClass() != this) return;
            if(cd.contains(killer.getName())) return;
            double damage = 1.75;
            for(int i = 0; i < 9; i++) {
                damage += 1.25;
            }
            cd.add(killer.getName());
            int time = (int) (20 * damage);
            killer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,time,1));
            //Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            //
            //    @Override
            //    public void run() {
            //        if(cd.contains(killer.getName())) {
            //            cd.remove(killer.getName());
            //        }
            //    }
            //}, 20*45);
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
        com.jules.kitpvp.abilities.IronPunch.use(p, upgrade.getCurrentUpgrade());
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("A tanky class with a powerful punch.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.IRON_BLOCK);
    }

    @Override
    public int getPrice() {
        return 2000;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.IRON_SWORD));
        return items;
    }

    @Override
    public int getXPPerHit() {
        return 5;
    }

    @Override
    public int getUpgradePrice(int level) {
        return 100 * level;
    }

    @Override
    public String getAbilityName() {
        return "Iron Punch";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Punch the ground, sending enemies","§7flying.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
