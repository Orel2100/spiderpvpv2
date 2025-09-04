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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Arcanist extends KitClass {
    @Override
    public String getName() {
        return "Arcanist";
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction().name().contains("RIGHT") == false) return;
        if(!com.jules.kitpvp.KitPVP.getPlaying().contains(p.getName())) return;
        if(p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(!com.jules.kitpvp.util.Utils.isUsingSword(p.getItemInHand())) return;
        if(p.getLevel() < 100) return;
        p.setExp(0);
        com.jules.kitpvp.kits.Upgrade upgrade = new com.jules.kitpvp.kits.Upgrade(p, this, com.jules.kitpvp.kits.UpgradeType.ABILITY);
        if(upgrade.getCurrentUpgrade() == 1) {
            com.jules.kitpvp.abilities.Beam.shoot(p, 6);
        }
        p.setLevel(0);
        p.setExp(0);
    }

    @EventHandler
    public void mining(BlockBreakEvent e) {
        //if(!Main.playing.contains(e.getPlayer().getName())) return;
        //MPlayer player = MPlayerManager.getMPlayer(e.getPlayer().getName());
        //if(player.getCurrentClass() != this)return;
        if(e.getBlock().getType().name().contains("ORE")) {
            //Utils.addLevel(player.getPlayer().getPlayer(), 10);
        }
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("A powerful mage with a damaging beam.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ENCHANTING_TABLE);
    }

    @Override
    public int getPrice() {
        return 2000;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.STICK));
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
        return "Beam";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Fire a beam of energy that","§7damages enemies.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
