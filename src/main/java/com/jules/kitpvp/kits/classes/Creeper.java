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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class Creeper extends KitClass {

    public static ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Creeper";
    }

    @EventHandler
    public void mining(BlockBreakEvent e) {
        com.jules.kitpvp.player.MPlayer player = com.jules.kitpvp.player.MPlayerManager.getMPlayer(e.getPlayer().getName());
        if(player.getCurrentClass() != this)return;
        if(e.getBlock().getType() == Material.COAL_ORE) {
            double damage = 7;
            for(int i = 0; i < 9; i++) {
                damage += 2;
            }
            double chance = damage/100;
            double random = new Random().nextDouble();
            if(random <= chance) {
                e.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.TNT));
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
        com.jules.kitpvp.abilities.Detonate.use(p, upgrade.getCurrentUpgrade());
        p.setLevel(0);
        p.setExp(0);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Creeper class uses","explosion based powers to","win. Energy is gained by","hitting players in melee","range.");
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
        return 800;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, sword);
        items.put(1, new ItemStack(Material.COOKED_BEEF, 2));
        items.put(2, new ItemStack(Material.CHAINMAIL_LEGGINGS));
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
        return "Detonate";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 1.25 + (0.75 * upgrade);
        return Arrays.asList("§7Detonate an explosion that","§7deals up to §c"+damage+"§7 damage to","§7nearby players. However, it","§7takes 3 seconds to","§7detonate.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
