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
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Blaze extends KitClass {
    @Override
    public String getName() {
        return "Blaze";
    }

    @EventHandler
    public void hit(ProjectileHitEvent e) {
        if(e.getEntity() instanceof Fireball && e.getEntity().getShooter() instanceof Player) {
            Player p = (Player)e.getEntity().getShooter();
            //if(MPlayerManager.getMPlayer(p.getName()).getCurrentClass() == this) {
            //    float damage = (float) 1.9;
            //    for(int i = 0; i < 9; i++) {
            //        damage += 0.3;
            //        damage = (float) Utils.round(damage, 2);
            //    }
            //    for(Player ps : Bukkit.getOnlinePlayers()) {
            //        ps.playSound(e.getEntity().getLocation(), Sound.EXPLODE, 2, 2);
            //    }
            //    for(Entity ent : e.getEntity().getNearbyEntities(3, 3, 3)) {
            //        if(ent == p) continue;
            //        if(ent instanceof LivingEntity && !ent.isDead()) {
            //            if(TeamManager.getTeamByPlayer(p) != null) {
            //                if(TeamManager.getTeamByPlayer(p).getPlayers().contains(ent)) {
            //                    continue;
            //                }
            //            }
            //            Utils.realDamage(ent, p, damage);
            //        }
            //    }
            //    e.getEntity().remove();
            //}
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
        try {
            com.jules.kitpvp.abilities.ImmolatingBurst.use(p, upgrade.getCurrentUpgrade());
        } catch (Exception e2) {
            // TODO: handle exception
        }
        p.setLevel(0);
        p.setExp(0);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("A fiery class with a fireball burst.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.BLAZE_ROD);
    }

    @Override
    public int getPrice() {
        return 1000;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.IRON_SWORD));
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
        return "Immolating Burst";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Unleash a burst of fireballs","§7at your enemies.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
