package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.ExplosiveArrow;
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Skeleton extends KitClass {

    @Override
    public String getName() {
        return "Skeleton";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Skeleton class makes", "excellent use of ranged", "abilities and weapons.");
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
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack bow = new ItemStack(Material.BOW);
        int powerLevel = 1;
        if (upgrade >= 4) powerLevel = 2;
        if (upgrade >= 8) powerLevel = 3;
        if (upgrade >= 9) powerLevel = 4;
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, powerLevel);
        if (upgrade >= 8) bow.addEnchantment(Enchantment.DURABILITY, 1);
        items.put(1, ItemStackCreator.createItem(bow, ChatColor.AQUA + getName() + " Bow", 1, getSwordLore(upgrade)));

        int arrowAmount = 30;
        if (upgrade >= 2) arrowAmount = 35;
        if (upgrade >= 3) arrowAmount = 40;
        if (upgrade >= 4) arrowAmount = 45;
        if (upgrade >= 5) arrowAmount = 50;
        if (upgrade >= 6) arrowAmount = 55;
        if (upgrade >= 7) arrowAmount = 60;
        if (upgrade >= 8) arrowAmount = 64;
        items.put(5, new ItemStack(Material.ARROW, arrowAmount));

        if (upgrade >= 8) {
            ItemStack axe = new ItemStack(Material.IRON_AXE);
            axe.addEnchantment(Enchantment.DIG_SPEED, 1);
            axe.addEnchantment(Enchantment.DURABILITY, 1);
            items.put(0, ItemStackCreator.createItem(axe, ChatColor.AQUA + getName() + " Axe", 1));
        }

        int steakAmount = 2;
        if(upgrade >= 4) steakAmount = 3;
        items.put(4, Utils.getSteaks(steakAmount, getName()));

        if (upgrade >= 4) {
            int healLevel = 1;
            if (upgrade >= 8) healLevel = 2;
            items.put(2, Utils.getPotionHeal(8, healLevel));
        }

        if (upgrade >= 5) {
            int speedLevel = 1;
            if (upgrade >= 6) speedLevel = 2;
            items.put(3, Utils.getPotionSpeed(speedLevel));
        }

        if (upgrade >= 7) {
            ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
            if (upgrade >= 8) helmet.setType(Material.IRON_HELMET);
            if (upgrade >= 9) helmet.setType(Material.DIAMOND_HELMET);
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            int projProt = 2;
            if (upgrade >= 8) projProt = 3;
            if (upgrade >= 9) projProt = 4;
            enchants.put(Enchantment.PROTECTION_PROJECTILE, projProt);
            items.put(6, ItemStackCreator.createItemStack(helmet, 1, ChatColor.AQUA + getName() + " Helmet", null, enchants));
        }

        return items;
    }

    @Override
    public int getPrice() {
        return 800;
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
        return "Explosive Arrow";
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 1.5 + (0.5 * upgrade);
        return Arrays.asList("§7Fire an explosive arrow", "§7that deals up to §c" + Utils.round(damage, 2) + "§7 damage", "§7to nearby players.");
    }

    @Override
    public int getXPPerHit() {
        return 20;
    }

    @Override
    public HitType getHitType() {
        return HitType.PROJECTILE;
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }

    @EventHandler
    public void onArrowHitPlayer(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || !(e.getDamager() instanceof Arrow) || !(e.getEntity() instanceof Player)) return;

        Arrow a = (Arrow) e.getDamager();
        if (!(a.getShooter() instanceof Player)) return;

        Player p = (Player) a.getShooter();
        if (p == e.getEntity()) return;

        MPlayer shooterData = MPlayerManager.getMPlayer(p.getName());
        if (shooterData.getCurrentClass() != this) return;

        // Skill: Salvaging
        Upgrade upgrade = new Upgrade(p, this, null);
        double chance = (10.0 * upgrade.getCurrentUpgrade()) / 100.0;
        if (new Random().nextDouble() <= chance) {
            p.getInventory().addItem(new ItemStack(Material.ARROW));
            p.sendMessage(ChatColor.YELLOW + "Your Salvaging skill has given you an arrow!");
        }

        // Skill: Agile Hands
        double agileChance = (3.0 + (2.0 * upgrade.getCurrentUpgrade())) / 100.0;
        if (new Random().nextDouble() <= agileChance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*3, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*3, 1));
        }

        if (ExplosiveArrow.exArrow.containsKey(a)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplosiveArrowHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow)) return;

        Arrow a = (Arrow) e.getEntity();
        if (ExplosiveArrow.exArrow.containsKey(a)) {
            Player p = ExplosiveArrow.exArrow.get(a);
            Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
            double damage = 1.5 + (0.5 * upgrade.getCurrentUpgrade());

            for (Entity ent : a.getNearbyEntities(4, 4, 4)) {
                if (ent instanceof LivingEntity && ent != p) {
                    Utils.realDamage(ent, p, damage);
                }
            }
            ExplosiveArrow.exArrow.remove(a);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (p.getLevel() < 100) return;

        boolean isBow = p.getItemInHand().getType() == Material.BOW;
        boolean isSword = Utils.isUsingSword(p.getItemInHand());

        if ((e.getAction().name().contains("LEFT") && isBow) || (e.getAction().name().contains("RIGHT") && isSword)) {
            Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
            ExplosiveArrow.use(p, upgrade.getCurrentUpgrade());
            p.setLevel(0);
            p.setExp(0);
        }
    }
}
