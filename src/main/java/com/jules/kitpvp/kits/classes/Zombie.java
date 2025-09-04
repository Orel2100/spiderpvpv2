package com.jules.kitpvp.kits.classes;

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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Zombie extends KitClass {

    @Override
    public String getName() {
        return "Zombie";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Zombie class focuses on", "defensive gameplay and", "boosts.");
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
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        if(upgrade >= 6) sword.setType(Material.STONE_SWORD);
        if(upgrade >= 8) sword.setType(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        items.put(1, Utils.getSteaks(2, getName()));

        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        if(upgrade >= 3) chestplate.setType(Material.IRON_CHESTPLATE);
        if(upgrade >= 9) chestplate.setType(Material.DIAMOND_CHESTPLATE);
        HashMap<Enchantment, Integer> enchants = new HashMap<>();
        if(upgrade >= 2) enchants.put(Enchantment.PROTECTION_PROJECTILE, 1);
        if(upgrade >= 6) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        if(upgrade >= 8) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        items.put(2, ItemStackCreator.createItemStack(chestplate, 1, ChatColor.AQUA + getName() + " Chestplate", null, enchants));

        if(upgrade >= 4){
            int healLevel = 1;
            if(upgrade >= 8) healLevel = 2;
            items.put(3, Utils.getPotionHeal(8, healLevel));
        }

        if(upgrade >= 5){
            int speedLevel = 1;
            if(upgrade >= 7) speedLevel = 2;
            items.put(4, Utils.getPotionSpeed(speedLevel));
        }

        return items;
    }

    @Override
    public int getPrice() {
        return 0;
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
        return "Circle of Healing";
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double healAmount = 1.5 + (0.5 * upgrade);
        return Arrays.asList("§7Heal yourself §c" + Utils.round(healAmount, 1) + "§7 and nearby", "§7friendly player for 1/2 of that.");
    }

    @Override
    public int getXPPerHit() {
        return 12;
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public String getAbilityReadyName() {
        return "Heal";
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;

        // Skill: Toughness
        Upgrade upgrade = new Upgrade(p, this, null);
        double chance = (6.87 + (3.125 * upgrade.getCurrentUpgrade())) / 100.0;

        if (new Random().nextDouble() <= chance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
        }
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || !(e.getDamager() instanceof Arrow) || !(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;

        // Skill: Well-Fed
        Upgrade upgrade = new Upgrade(p, this, null);
        double chance = (3.0 + (2.0 * upgrade.getCurrentUpgrade())) / 100.0;

        if (new Random().nextDouble() <= chance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 3, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 0));
        }
    }

    @EventHandler
    public void onMine(BlockBreakEvent e) {
        if (MPlayerManager.getMPlayer(e.getPlayer().getName()).getCurrentClass() != this) return;

        // Skill: Berzerker
        Player p = e.getPlayer();
        Upgrade upgrade = new Upgrade(p, this, null);
        double chance = (1.25 + (3.75 * upgrade.getCurrentUpgrade())) / 100.0;

        if (new Random().nextDouble() <= chance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 3 + 10, 0));
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

        Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        com.jules.kitpvp.abilities.Heal.use(p, upgrade.getCurrentUpgrade());
        p.setLevel(0);
        p.setExp(0);
    }
}
