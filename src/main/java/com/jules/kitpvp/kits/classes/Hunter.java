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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class Hunter extends KitClass {

    public ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Hunter";
    }

    @EventHandler
    public void eventArrowFired(EntityShootBowEvent e) {
        if(e.getForce() != 1.0) return;
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player)e.getEntity();
        if(p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(!cd.contains(p.getName())) return;
        if (((e.getEntity() instanceof LivingEntity)) && ((e.getEntity() instanceof Player)) && ((e.getProjectile() instanceof Arrow))){
            LivingEntity player = e.getEntity();
            double minAngle = 6.283185307179586D;
            Entity minEntity = null;
            for (Entity entity : player.getNearbyEntities(64.0D, 64.0D, 64.0D)) {
                if ((player.hasLineOfSight(entity)) && (!entity.isDead()) && (entity instanceof Player)){
                    //Vector toTarget = entity.getLocation().toVector().clone().subtract(player.getLocation().toVector());
                    //double angle = e.getProjectile().getVelocity().angle(toTarget);
                    //if (angle < minAngle){
                    //    minAngle = angle;
                    //    minEntity = entity;
                    //}
                }
            }
            //if (minEntity != null && minEntity instanceof LivingEntity && e.getProjectile() instanceof Arrow) {
            //    new HomingTask((Arrow)e.getProjectile(), (LivingEntity)minEntity, Main.getPlugin());
            //}
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        final Player p = e.getPlayer();
        if(e.getAction().name().contains("LEFT") == false) return;
        if(p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(p.getItemInHand().getType() != Material.BOW) return;
        if(p.getLevel() < 100) return;
        //Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        int damage = 6;
        //for(int i = 0; i < upgrade.getCurrentUpgrade(); i++) {
        //    damage += 1;
        //}
        p.setLevel(0);
        p.setExp(0);
        cd.add(p.getName());
        //Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
        //
        //    @Override
        //    public void run() {
        //        cd.remove(p.getName());
        //        p.sendMessage(ChatColor.GREEN + "Your Eagle's Eye wore off.");
        //
        //    }
        //}, 20*damage);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("A skilled archer with homing arrows.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.BOW);
    }

    @Override
    public int getPrice() {
        return 1000;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.BOW));
        items.put(1, new ItemStack(Material.ARROW, 64));
        return items;
    }

    @Override
    public int getXPPerHit() {
        return 20;
    }

    @Override
    public int getUpgradePrice(int level) {
        return 100 * level;
    }

    @Override
    public String getAbilityName() {
        return "Homing Arrow";
    }

    @Override
    public HitType getHitType() {
        return HitType.PROJECTILE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Fire an arrow that seeks","§7the nearest enemy.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
