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

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Random;

public class Skeleton extends KitClass {
    @Override
    public String getName() {
        return "Skeleton";
    }

    @EventHandler
    public void firstSkill(EntityDamageByEntityEvent e) {
        if(e.isCancelled()) return;
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            Arrow a = (Arrow)e.getDamager();
            if(a.getShooter() instanceof Player) {
                Player p = (Player)a.getShooter();
                if(p == e.getEntity()) return;
                if(com.jules.kitpvp.team.TeamManager.getTeamByPlayer(p) != null) {
                    if(com.jules.kitpvp.team.TeamManager.getTeamByPlayer(p).getPlayers().contains(e.getEntity())) {
                        return;
                    }
                }
                com.jules.kitpvp.player.MPlayer player = com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName());
                if(player.getCurrentClass() != this) return;
                double damage = 0;
                for(int i = 0; i < 9; i++) {
                    damage += 10;
                }
                double chance = damage/100;
                double random = new Random().nextDouble();
                if(random <= chance) {
                    p.getInventory().addItem(new ItemStack(Material.ARROW));
                    p.sendMessage(org.bukkit.ChatColor.YELLOW + "Skeleton: You Salvaging skill has given you an arrow.");
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction().name().contains("LEFT") == false) return;
        if(p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(p.getItemInHand().getType() != Material.BOW) return;
        if(p.getLevel() < 100) return;
        com.jules.kitpvp.kits.Upgrade upgrade = new com.jules.kitpvp.kits.Upgrade(p, this, com.jules.kitpvp.kits.UpgradeType.ABILITY);
        com.jules.kitpvp.abilities.ExplosiveArrow.use(p, upgrade.getCurrentUpgrade());
        p.setLevel(0);
        p.setExp(0);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Skeleton class makes","excellent use of ranged","abilities and weapons.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.BONE);
    }

    @Override
    public int getPrice() {
        return 800;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        items.put(0, bow);
        items.put(1, new ItemStack(Material.COOKED_BEEF, 2));
        items.put(2, new ItemStack(Material.ARROW, 30));
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
        return "Explosive Arrow";
    }

    @Override
    public HitType getHitType() {
        return HitType.PROJECTILE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 1.5 + (0.5 * upgrade);
        return Arrays.asList("§7Fire an explosive arrow","§7that deals up to §c"+damage+"§7 damage","§7to nearby players.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
