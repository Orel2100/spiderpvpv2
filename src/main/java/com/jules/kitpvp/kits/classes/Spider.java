package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.kits.HitType;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayerManager;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Spider extends KitClass {

    public static ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Spider";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Spider class uses agile", "paths for combat.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.WEB);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        if(upgrade >= 3) sword.setType(Material.STONE_SWORD);
        if(upgrade >= 5) sword.setType(Material.IRON_SWORD);
        if(upgrade >= 9) sword.setType(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 2);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        int steakAmount = 1;
        if(upgrade >= 2) steakAmount = 2;
        if(upgrade >= 6) steakAmount = 3;
        items.put(1, Utils.getSteaks(steakAmount, getName()));

        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        if(upgrade >= 4) boots.setType(Material.IRON_BOOTS);
        if(upgrade >= 7) boots.setType(Material.DIAMOND_BOOTS);
        if(upgrade >= 7) {
            HashMap<Enchantment, Integer> enchant = new HashMap<>();
            enchant.put(Enchantment.DURABILITY, 3);
            items.put(4, ItemStackCreator.createItemStack(boots, 1, ChatColor.AQUA + getName() + " Boots", null, enchant));
        } else {
            items.put(4, ItemStackCreator.createItemStack(boots, 1, ChatColor.AQUA + getName() + " Boots", null));
        }

        if(upgrade >= 4){
            int speedLevel = 1;
            if(upgrade >= 6) speedLevel = 2;
            items.put(2, Utils.getPotionSpeed(speedLevel));
        }

        if(upgrade >= 5){
            int healLevel = 1;
            if(upgrade >= 8) healLevel = 2;
            items.put(3, Utils.getPotionHeal(8, healLevel));
        }

        return items;
    }

    @Override
    public int getPrice() {
        return 900;
    }

    @Override
    public int getUpgradePrice(int level) {
        switch (level) {
            case 1: return 0;
            case 2: return 50;
            case 3: return 125;
            case 4: return 300;
            case 5: return 600;
            case 6: return 3500;
            case 7: return 6500;
            case 8: return 8500;
            case 9: return 14000;
            default: return 14000;
        }
    }

    @Override
    public String getAbilityName() {
        return "Leap";
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double radius = 2.75 + (0.25 * upgrade);
        return Arrays.asList("§7Leap forward into the air,", "§7applying §cSlowness II", "§7for 4 seconds to all", "§7enemies within a §c" + Utils.round(radius, 2) + "", "§7blocks upon landing. Gives", "§7you Absorption I for §c5 §7seconds after casting.");
    }

    @Override
    public int getXPPerHit() {
        return 7;
    }

    @Override
    public HitType getHitType() {
        return HitType.TIMER;
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player) || e.getCause() != DamageCause.FALL) return;

        Player p = (Player) e.getEntity();
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;

        // Leap ability landing
        if (cd.contains(p.getName())) {
            cd.remove(p.getName());
            Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
            double radius = 2.75 + (0.25 * upgrade.getCurrentUpgrade());
            for (Entity ent : p.getNearbyEntities(radius, radius, radius)) {
                if (ent instanceof LivingEntity && ent != p) {
                    ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 4, 1));
                }
            }
        }

        // Skill: Drop Shock
        Upgrade upgrade = new Upgrade(p, this, null);
        double maxDamage = 4 + (1 * upgrade.getCurrentUpgrade());
        double damage = e.getDamage() * ((110 + (10 * upgrade.getCurrentUpgrade())) / 100.0);
        if (damage > maxDamage) damage = maxDamage;

        for (Entity ent : p.getNearbyEntities(4, 4, 4)) {
            if (ent instanceof LivingEntity && ent != p) {
                Utils.realDamage(ent, p, damage);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        com.jules.kitpvp.abilities.Leap.use(p);
        cd.add(p.getName());
    }
}
