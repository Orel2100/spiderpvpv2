package com.jules.kitpvp.kits;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class KitClass implements Listener {

    public abstract String getName();
    public abstract List<String> getDescription();
    public abstract ClassType getType();
    public abstract ItemStack getIcon();
    public abstract int getPrice();
    public abstract HashMap<Integer, ItemStack> getStartingItems(int upgrade);
    public abstract int getXPPerHit();
    public abstract int getUpgradePrice(int level);
    public abstract String getAbilityName();
    public abstract HitType getHitType();
    public abstract List<String> getAbilityDescription(int upgrade);
    public abstract String getAbilityReadyName();

    public List<String> getSwordLore(int upgrade) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.GRAY + "Ability: " + ChatColor.RED + getAbilityName());
        list.add("");
        for(String s : getAbilityDescription(upgrade)) {
            list.add(s);
        }
        return list;
    }

    public String getNameByType(UpgradeType type) {
        if(type.equals(UpgradeType.ABILITY)) {
            return getAbilityName();
        }
        if(type.equals(UpgradeType.KIT)) {
            return getName();
        }
        return null;
    }

    public void autoArmor(Player p) {
        for(ItemStack item : p.getInventory().getContents()) {
            if(item == null || item.getType() == Material.AIR) continue;
            if(item.getType().toString().contains("HELMET")) {
                p.getInventory().setHelmet(item);
                p.getInventory().remove(item);
            }
            if(item.getType().toString().contains("CHESTPLATE")) {
                p.getInventory().setChestplate(item);
                p.getInventory().remove(item);
            }
            if(item.getType().toString().contains("LEGGINGS")) {
                p.getInventory().setLeggings(item);
                p.getInventory().remove(item);
            }
            if(item.getType().toString().contains("BOOTS")) {
                p.getInventory().setBoots(item);
                p.getInventory().remove(item);
            }
        }
        if(p.getInventory().getHelmet() == null) {
            p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        }
        if(p.getInventory().getChestplate() == null) {
            p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        }
        if(p.getInventory().getLeggings() == null) {
            p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        }
        if(p.getInventory().getBoots() == null) {
            p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        }
        p.updateInventory();
    }

    public String getPotionEffectName(PotionEffectType pe) {
        if(pe.equals(PotionEffectType.INCREASE_DAMAGE)) {
            return "Strength";
        }
        if(pe.equals(PotionEffectType.SPEED)) {
            return "Speed";
        }
        if(pe.equals(PotionEffectType.FAST_DIGGING)) {
            return "Haste";
        }
        if(pe.equals(PotionEffectType.REGENERATION)) {
            return "Regeneration";
        }
        if(pe.equals(PotionEffectType.DAMAGE_RESISTANCE)) {
            return "Resistance";
        }
        return "PotionEffect";
    }

    public void apply(final Player p){
        KitPVP.getPlaying().add(p.getName());
        p.setGameMode(GameMode.SURVIVAL);
        p.setFoodLevel(20);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        for(PotionEffect potion : p.getActivePotionEffects()) p.removePotionEffect(potion.getType());

        Upgrade upgrade = new Upgrade(p, this, UpgradeType.KIT);
        for(Map.Entry<Integer, ItemStack> entry : getStartingItems(upgrade.getCurrentUpgrade()).entrySet()) {
            p.getInventory().setItem(entry.getKey(), entry.getValue());
        }

        if(upgrade.hasPrestige()) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST,100000,5));
        } else {
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST,100000,4));
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL,1,5));
        autoArmor(p);
    }
}
