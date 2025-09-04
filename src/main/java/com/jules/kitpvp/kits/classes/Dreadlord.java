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
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Dreadlord extends KitClass {

    @Override
    public String getName() {
        return "Dreadlord";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Dreadlord class uses", "explosive abilities to", "attack and steal health.");
    }

    @Override
    public ClassType getType() {
        return ClassType.HERO;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        if (upgrade >= 4) sword.setType(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        if (upgrade >= 9) sword.addEnchantment(Enchantment.DAMAGE_UNDEAD, 1);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        int steakAmount = 2;
        if (upgrade >= 6) steakAmount = 3;
        items.put(1, Utils.getSteaks(steakAmount, getName()));

        int healPotionLevel = 1;
        if (upgrade >= 7) healPotionLevel = 2;
        items.put(2, Utils.getPotionHeal(8, healPotionLevel));

        if (upgrade >= 3) {
            int speedPotionLevel = 1;
            if (upgrade >= 5) speedPotionLevel = 2;
            items.put(3, Utils.getPotionSpeed(speedPotionLevel));
        }

        if (upgrade >= 8) {
            ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
            if (upgrade >= 9) {
                helmet.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1);
                helmet.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
            }
            items.put(4, ItemStackCreator.createItem(helmet, ChatColor.AQUA + getName() + " Helmet", 1));
        }

        return items;
    }

    @Override
    public int getPrice() {
        return 10000;
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
        return "Shadow Burst";
    }

    @Override
    public int getXPPerHit() {
        return 12;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 2.25 + (0.75 * upgrade);
        return Arrays.asList("§7Fire three wither heads at", "§7once dealing up to §c" + Utils.round(damage, 2), "§7damage per head.");
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
    public void onSkullHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof WitherSkull)) return;

        WitherSkull ws = (WitherSkull) e.getEntity();
        if (!(ws.getShooter() instanceof Player)) return;

        Player p = (Player) ws.getShooter();
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;

        Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        double damage = 2.25 + (0.75 * upgrade.getCurrentUpgrade());

        for (Player ps : Bukkit.getOnlinePlayers()) {
            ps.playSound(e.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 5);
        }

        for (Entity ent : ws.getNearbyEntities(2.5, 2.5, 2.5)) {
            if (ent instanceof Player && ent != p) {
                Utils.realDamage(ent, p, damage);
            }
        }
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;

        Player p = e.getEntity().getKiller();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: Soul Eater
        Upgrade upgrade = new Upgrade(p, this, null);
        double regen = 1.5 + (0.5 * upgrade.getCurrentUpgrade());
        double strength = 0.5 + (0.5 * upgrade.getCurrentUpgrade());

        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) (20 * regen), 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (20 * strength), 0));
    }

    @EventHandler
    public void onMine(BlockBreakEvent e) {
        MPlayer player = MPlayerManager.getMPlayer(e.getPlayer().getName());
        if (player.getCurrentClass() != this) return;

        if (e.getBlock().getType() == Material.IRON_ORE) {
            // Skill: Iron Heart
            Upgrade upgrade = new Upgrade(e.getPlayer(), this, null);
            double chance = (16.0 + (9.0 * upgrade.getCurrentUpgrade())) / 100.0;
            if (new Random().nextDouble() <= chance) {
                e.getBlock().setType(Material.AIR);
                e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT));
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getDamager();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: Dark Rush
        Upgrade upgrade = new Upgrade(p, this, null);
        double chance = (10.37 + (1.625 * upgrade.getCurrentUpgrade())) / 100.0;
        if (new Random().nextDouble() <= chance) {
            p.setHealth(Math.min(p.getHealth() + 1, p.getMaxHealth()));
            p.setFoodLevel(Math.min(p.getFoodLevel() + 6, 20));
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

        com.jules.kitpvp.abilities.ShadowBurst.use(p);
        p.setLevel(0);
        p.setExp(0);
    }
}
