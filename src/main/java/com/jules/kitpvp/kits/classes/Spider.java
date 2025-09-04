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

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Spider extends KitClass {

    public static ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Spider";
    }

    @EventHandler
    public void ability(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        if(e.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        Player p = (Player)e.getEntity();
        if(!com.jules.kitpvp.KitPVP.getPlaying().contains(p.getName())) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(!cd.contains(p.getName())) return;
        cd.remove(p.getName());
        //Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        double damage = 2.75;
        //for(int i = 0; i<upgrade.getCurrentUpgrade(); i++) {
        //    damage+=0.25;
        //}
        for(Entity ent : p.getNearbyEntities(damage, damage, damage)) {
            if(ent == p) continue;
            //if(TeamManager.getTeamByPlayer(p) != null) {
            //    if(TeamManager.getTeamByPlayer(p).getPlayers().contains(ent)) continue;
            //}
            if(ent instanceof LivingEntity && !ent.isDead()) {
                ((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,1));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction().name().contains("RIGHT") == false) return;
        if(p.getItemInHand() == null) return;
        if(p.getItemInHand().getItemMeta() == null) return;
        if(p.getItemInHand().getItemMeta().getDisplayName() == null) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(!com.jules.kitpvp.util.Utils.isUsingSword(p.getItemInHand())) return;
        if(p.getLevel() < 100) return;
        com.jules.kitpvp.abilities.Leap.use(p);
        cd.add(p.getName());
        p.setLevel(0);
        p.setExp(0);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("An agile class that can leap and","deal damage on landing.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.SPIDER_EYE);
    }

    @Override
    public int getPrice() {
        return 500;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        items.put(0, sword);
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
        return "Leap";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Leap forward, dealing damage","§7to enemies upon landing.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
