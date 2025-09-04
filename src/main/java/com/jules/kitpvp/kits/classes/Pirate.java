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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Pirate extends KitClass {

    public static ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Pirate";
    }

    @EventHandler
    public void hit(ProjectileHitEvent e) {
        if(e.getEntity() instanceof WitherSkull) {
            if(e.getEntity().getShooter() instanceof Player) {
                WitherSkull ws = (WitherSkull) e.getEntity();
                Player shooter =  (Player) e.getEntity().getShooter();
                if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(shooter.getName()).getCurrentClass() != this) return;
                com.jules.kitpvp.kits.Upgrade upgrade = new com.jules.kitpvp.kits.Upgrade(shooter, this,com.jules.kitpvp.kits.UpgradeType.ABILITY);
                for(Entity ent : ws.getNearbyEntities(3, 3, 3)) {
                    if(ent instanceof LivingEntity) {
                        if(shooter == ent) continue;
                        //if(TeamManager.getTeamByPlayer(shooter) != null) {
                        //    if(TeamManager.getTeamByPlayer(shooter).getPlayers().contains(ent)) {
                        //        continue;
                        //    }
                        //}
                        //Utils.realDamage(ent, shooter, 10);
                    }
                }
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
        com.jules.kitpvp.abilities.CannonFire.use(p);
        p.setLevel(0);
        p.setExp(0);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("A swashbuckling class with a cannon.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.TNT);
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
        return "Cannon Fire";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Fire a cannonball that explodes","§7on impact.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
