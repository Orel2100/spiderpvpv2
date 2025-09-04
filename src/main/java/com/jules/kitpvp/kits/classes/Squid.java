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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Squid extends KitClass {

    public ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Squid";
    }

    @EventHandler
    public void firstSkill(PlayerItemConsumeEvent e) {
        Player p = (Player)e.getPlayer();
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(e.getItem().getType() == Material.POTION) {
            double damage = 0.75;
            double blocks = 2.75;
            for(int i = 0; i < 9; i++) {
                damage += 0.25;
                blocks += 0.25;
            }
            for(Entity ent : p.getNearbyEntities(blocks, blocks, blocks)) {
                if(ent instanceof LivingEntity) {
                    if(ent.equals(p)) continue;
                    //if(TeamManager.getTeamByPlayer(p) != null) {
                    //    if(TeamManager.getTeamByPlayer(p).getPlayers().contains(ent)) continue;
                    //}
                    int time = (int) (20 *  damage);
                    ((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,time,0));
                    //EffectUtils.createCircle(p.getLocation(), 20*3,true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction().name().contains("RIGHT") == false) return;
        //if(Game.getGameState() != GameStage.STARTED) return;
        if(p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(!com.jules.kitpvp.util.Utils.isUsingSword(p.getItemInHand())) return;
        if(p.getLevel() < 100) return;
        com.jules.kitpvp.kits.Upgrade upgrade = new com.jules.kitpvp.kits.Upgrade(p, this, com.jules.kitpvp.kits.UpgradeType.ABILITY);
        double damage = 35;
        for(int i = 0; i<upgrade.getCurrentUpgrade(); i++) {
            damage+=5;
        }
        com.jules.kitpvp.abilities.SquidSplash.use(p,damage);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("An aquatic class that can heal","in water.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.INK_SAC);
    }

    @Override
    public int getPrice() {
        return 0;
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
        return "Squid Splash";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Heal yourself and nearby allies.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
