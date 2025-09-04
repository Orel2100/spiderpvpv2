package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.kits.HitType;
import com.jules.kitpvp.kits.KitClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Shaman extends KitClass {
    @Override
    public String getName() {
        return "Shaman";
    }

    @EventHandler
    public void firstSkill(EntityDamageByEntityEvent e) {
        if(e.isCancelled()) return;
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player)e.getDamager();
            com.jules.kitpvp.player.MPlayer player = com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName());
            if(player.getCurrentClass() != this) return;
            double damage = 1.5;
            for(int i = 0; i < 9; i++) {
                damage += 0.5;
            }
            double chance = damage/100;
            double random = new Random().nextDouble();
            int time = (int) (20*damage);
            if(random <= chance) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,time,1));
                if(e.getEntity() instanceof LivingEntity && !e.getEntity().isDead()) {
                    ((LivingEntity)e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,time,0));
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
        com.jules.kitpvp.kits.Upgrade upgrade = new com.jules.kitpvp.kits.Upgrade(p, this, com.jules.kitpvp.kits.UpgradeType.ABILITY);
        com.jules.kitpvp.abilities.Tornado.use(p, upgrade.getCurrentUpgrade());
        p.setLevel(0);
        p.setExp(0);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("A spiritual class that can summon","a tornado and wolves.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.TOTEM_OF_UNDYING);
    }

    @Override
    public int getPrice() {
        return 1000;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.VINE));
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
        return "Tornado";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Summon a tornado that damages","§7and knocks back enemies.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
