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
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class Dreadlord extends KitClass {

    public static HashMap<Entity, Integer> hits = new HashMap<>();

    @Override
    public String getName() {
        return "Dreadlord";
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void hit(ProjectileHitEvent e) throws InterruptedException {
        if(e.getEntity() instanceof WitherSkull) {
            WitherSkull ws = (WitherSkull)e.getEntity();
            if(!(ws.getShooter() instanceof Player)) return;
            Player p = (Player)ws.getShooter();
            if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() == this) {
                com.jules.kitpvp.kits.Upgrade upgrade = new com.jules.kitpvp.kits.Upgrade(p, this, com.jules.kitpvp.kits.UpgradeType.ABILITY);
                double damage = 2.25;
            //    for(int i = 0; i < upgrade.getCurrentUpgrade(); i++) {
            //        damage += 0.75;
            //    }
            //    for(Player ps : Bukkit.getOnlinePlayers()) {
            //        ps.playSound(e.getEntity().getLocation(), Sound.EXPLODE, 5, 5);
            //    }
            //    for(Entity ent : ws.getNearbyEntities(2.5, 2.5, 2.5)) {
            //        if(!(ent instanceof Player)) continue;
            //        if(ent == p) continue;
            //        Player enp = (Player)ent;
            //        if(TeamManager.getTeamByPlayer(p) != null) {
            //            if(TeamManager.getTeamByPlayer(p).getPlayers().contains(enp)) {
            //                continue;
            //            }
            //        }
            //        Utils.realDamage(enp, p, 4);
            //    }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) throws InterruptedException{
        Player p = e.getPlayer();
        if(e.getAction().name().contains("RIGHT") == false) return;
        //if(Game.getGameState() != GameStage.STARTED) return;
        if(p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(!com.jules.kitpvp.util.Utils.isUsingSword(p.getItemInHand())) return;
        if(p.getLevel() < 100) return;
        com.jules.kitpvp.abilities.ShadowBurst.use(p);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("A shadowy class that heals on kill.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.WITHER_SKELETON_SKULL);
    }

    @Override
    public int getPrice() {
        return 500;
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
        return "Shadow Burst";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Burst forward, dealing damage to","§7enemies in your path.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
