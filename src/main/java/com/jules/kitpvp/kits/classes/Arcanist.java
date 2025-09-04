package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.kits.HitType;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.player.MPlayerManager;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Arcanist extends KitClass {
    @Override
    public String getName() {
        return "Arcanist";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Arcanist class uses his","power to gain energy faster","than most");
    }

    @Override
    public ClassType getType() {
        return ClassType.HERO;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.BOOK);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        if (upgrade >= 7) {
            sword.setType(Material.DIAMOND_SWORD);
        }
        sword.addEnchantment(Enchantment.DURABILITY, 2);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        int steakAmount = 2;
        if (upgrade >= 6) {
            steakAmount = 3;
        }
        items.put(1, Utils.getSteaks(steakAmount, getName()));

        if (upgrade >= 2) {
            int potionHealLevel = 1;
            if (upgrade >= 8) {
                potionHealLevel = 2;
            }
            items.put(2, Utils.getPotionHeal(8, potionHealLevel));
        }

        if (upgrade >= 3) {
            int potionSpeedLevel = 1;
            if (upgrade >= 5) {
                potionSpeedLevel = 2;
            }
            items.put(3, Utils.getPotionSpeed(potionSpeedLevel));
        }

        if (upgrade >= 4) {
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.PROTECTION_EXPLOSIONS, 1);
            if (upgrade >= 9) {
                enchants.put(Enchantment.PROTECTION_EXPLOSIONS, 2);
                enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            }
            items.put(4, ItemStackCreator.createItemStack(new ItemStack(Material.DIAMOND_LEGGINGS), 1, ChatColor.AQUA + getName() + " Leggings", null, enchants));
        }

        return items;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!e.getAction().name().contains("RIGHT")) return;
        if (!KitPVP.getPlaying().contains(p.getName())) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        p.setExp(0);
        Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        double damage = 5.25 + (0.75 * upgrade.getCurrentUpgrade());
        // Assuming Beam.shoot takes player and damage. This might need adjustment.
        com.jules.kitpvp.abilities.Beam.shoot(p, damage);
        p.setLevel(0);
    }

    @EventHandler
    public void secondSkill(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        MPlayer player = MPlayerManager.getMPlayer(e.getEntity().getName());
        if (!KitPVP.getPlaying().contains(player.getPlayer().getPlayer().getName())) return;
        if (player.getCurrentClass() != this) return;

        // This skill seems to be based on a fixed loop, not upgrade level.
        // I will keep it as is, but this could be a point for future improvement.
        double damage = 6.5;
        for (int i = 0; i < 9; i++) {
            damage += 1.5;
        }
        double chance = damage / 100;
        double random = new Random().nextDouble();
        if (random <= chance) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(e.getEntity().getLocation(), Sound.EXPLODE, 1, 1);
            }
            for (Entity ent : e.getEntity().getNearbyEntities(2, 2, 2)) {
                if (ent instanceof LivingEntity) {
                    // Assuming no team logic for now, as TeamManager is not available.
                    ((LivingEntity) ent).damage(2);
                }
            }
        }
    }

    @EventHandler
    public void firstSkill(PlayerDeathEvent e) {
        if (!KitPVP.getPlaying().contains(e.getEntity().getName())) return;
        if (!(e.getEntity().getKiller() instanceof Player)) return;
        Player killer = e.getEntity().getKiller();
        MPlayer player = MPlayerManager.getMPlayer(killer.getName());
        if (player.getCurrentClass() != this) return;

        // This skill also seems to be based on a fixed loop.
        double damage = 0.5;
        for (int i = 0; i < 9; i++) {
            damage += 0.5;
        }
        killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 6, 2));
        int time = (int) (20 * damage);
        killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, time, 1));
    }

    @EventHandler
    public void mining(BlockBreakEvent e) {
        if (!KitPVP.getPlaying().contains(e.getPlayer().getName())) return;
        MPlayer player = MPlayerManager.getMPlayer(e.getPlayer().getName());
        if (player.getCurrentClass() != this) return;
        if (e.getBlock().getType().name().contains("ORE")) {
            Utils.addLevel(player.getPlayer().getPlayer(), 10);
        }
    }

    @Override
    public int getPrice() {
        return 10000;
    }

    @Override
    public int getXPPerHit() {
        if (getHitType() == HitType.MELEE)
            return 35;
        return 0;
    }

    @Override
    public int getUpgradePrice(int level) {
        switch (level) {
            case 1: return 0;
            case 2: return 200;
            case 3: return 500;
            case 4: return 1200;
            case 5: return 2400;
            case 6: return 7000;
            case 7: return 13000;
            case 8: return 17000;
            case 9: return 28000;
            default: return 28000;
        }
    }

    @Override
    public String getAbilityName() {
        return "Arcane Beam";
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 5.25 + (0.75 * upgrade);
        return Arrays.asList("§7Cast a beam that hits", "§7player for §c" + Utils.round(damage, 2) + "§7 damage.");
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
