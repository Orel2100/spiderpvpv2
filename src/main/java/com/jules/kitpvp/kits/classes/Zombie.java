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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Zombie extends KitClass {
    @Override
    public String getName() {
        return "Zombie";
    }

    @EventHandler
    public void firstSkill(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
            float damage = (float) 6.87;
            for(int i = 0; i < 9; i++) {
                if(i == 2 || i == 4 || i == 6 || i == 8) {
                    damage += 3.12;
                    //damage = (float) Utils.round(damage, 2);
                    continue;
                }
                damage += 3.13;
                //damage = (float) Utils.round(damage, 2);
            }
            double chance = damage/100;
            double random = new Random().nextDouble();
            if(random <= chance) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20,0));
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
        com.jules.kitpvp.abilities.Heal.use(p, upgrade.getCurrentUpgrade());
        p.setLevel(0);
        p.setExp(0);
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Zombie class focuses on","defensive gameplay and","boosts.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ROTTEN_FLESH);
    }

    @Override
    public int getPrice() {
        return 0;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, sword);
        items.put(1, new ItemStack(Material.COOKED_BEEF, 2));
        items.put(2, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        return items;
    }

    @Override
    public int getXPPerHit() {
        return 12;
    }

    @Override
    public int getUpgradePrice(int level) {
        return 100 * level;
    }

    @Override
    public String getAbilityName() {
        return "Circle of Healing";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 1.5 + (0.5 * upgrade);
        return Arrays.asList("§7Heal yourself §c"+damage+"§7 and nearby","§7friendly player for 1/2 of that.");
    }

    @Override
    public String getAbilityReadyName() {
        return "Heal";
    }
}
