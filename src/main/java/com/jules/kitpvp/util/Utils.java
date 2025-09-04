package com.jules.kitpvp.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static ItemStack getSteaks(int amount, String name) {
        ItemStack steak = new ItemStack(org.bukkit.Material.COOKED_BEEF, amount);
        org.bukkit.inventory.meta.ItemMeta meta = steak.getItemMeta();
        meta.setDisplayName(name + "'s Steak");
        steak.setItemMeta(meta);
        return steak;
    }

    public static ItemStack getPotionHeal(int level, int amount) {
        ItemStack potion = new ItemStack(org.bukkit.Material.POTION, amount);
        org.bukkit.inventory.meta.PotionMeta meta = (org.bukkit.inventory.meta.PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.HEAL, 1, level), true);
        meta.setDisplayName("Potion of Healing");
        potion.setItemMeta(meta);
        return potion;
    }

    public static ItemStack getPotionSpeed(int level) {
        ItemStack potion = new ItemStack(org.bukkit.Material.POTION, 1);
        org.bukkit.inventory.meta.PotionMeta meta = (org.bukkit.inventory.meta.PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 20 * 30, level), true);
        meta.setDisplayName("Potion of Speed");
        potion.setItemMeta(meta);
        return potion;
    }

    public static void realDamage(Entity target, Player damager, double damage) {
        if (target instanceof org.bukkit.entity.LivingEntity) {
            ((org.bukkit.entity.LivingEntity) target).damage(damage, damager);
        }
    }

    public static boolean isUsingSword(ItemStack item) {
        if (item == null) {
            return false;
        }
        return item.getType().name().endsWith("_SWORD");
    }

    public static void addLevel(Player player, int level) {
        player.setLevel(player.getLevel() + level);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
