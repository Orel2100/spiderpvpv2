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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Squid extends KitClass {

    public ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Squid";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Splashing around, like a", "Squid, because that what", "Squids do. Splash.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.INK_SACK);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        if(upgrade >= 2) sword.setType(Material.STONE_SWORD);
        if(upgrade >= 7) sword.setType(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        int steakAmount = 3;
        if(upgrade >= 4) steakAmount = 4;
        items.put(1, Utils.getSteaks(steakAmount, getName()));

        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        if(upgrade >= 5) boots.setType(Material.IRON_BOOTS);
        if(upgrade >= 8) boots.setType(Material.DIAMOND_BOOTS);
        HashMap<Enchantment, Integer> enchants = new HashMap<>();
        enchants.put(Enchantment.DURABILITY, 1);
        if(upgrade >= 3) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        if(upgrade >= 9) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        items.put(2, ItemStackCreator.createItemStack(boots, 1, ChatColor.AQUA + getName() + " Boots", null, enchants));

        if(upgrade >= 4){
            int healLevel = 1;
            if(upgrade >= 6) healLevel = 2;
            if(upgrade >= 8) healLevel = 3;
            items.put(3, Utils.getPotionHeal(6, healLevel));
        }

        if(upgrade >= 7){
            items.put(4, Utils.getPotionSpeed(1));
        }

        return items;
    }

    @Override
    public int getPrice() {
        return 450;
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
        return "Squid Splash";
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double percent = 35 + (5 * (upgrade -1));
        return Arrays.asList("§7Deals 3 damages to all", "§7enemies in a 5 block", "§7radius. You are healed by", "§c" + percent + "%§7 of the total damage", "§7dealt. Max heal of §c8§7.");
    }

    @Override
    public int getXPPerHit() {
        return 16;
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (e.getItem().getType() != Material.POTION) return;

        // Skill: Ink Spiller
        Upgrade upgrade = new Upgrade(p, this, null);
        double duration = 0.75 + (0.25 * upgrade.getCurrentUpgrade());
        double radius = 2.75 + (0.25 * upgrade.getCurrentUpgrade());

        for (Entity ent : p.getNearbyEntities(radius, radius, radius)) {
            if (ent instanceof LivingEntity && ent != p) {
                ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) (20 * duration), 0));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        final Player p = (Player) e.getEntity();
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (cd.contains(p.getName())) return;

        // Skill: Rejuvenate
        Upgrade upgrade = new Upgrade(p, this, null);
        double healAmount = 4.0 + (0.5 * (upgrade.getCurrentUpgrade() - 1));
        double cooldown = 360.0 - (10.0 * (upgrade.getCurrentUpgrade() - 1));

        cd.add(p.getName());
        p.setHealth(Math.min(p.getHealth() + healAmount, p.getMaxHealth()));
        Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> cd.remove(p.getName()), (long) (20 * cooldown));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        double percent = 35 + (5 * (upgrade.getCurrentUpgrade() - 1));
        com.jules.kitpvp.abilities.SquidSplash.use(p, percent);
    }
}
