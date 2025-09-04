package com.jules.kitpvp.kits.classes;

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
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

public class Herobrine extends KitClass {

    @Override
    public String getName() {
        return "Herobrine";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Herobrine class uses", "supernatural abilities to", "attack and destroy your", "enemies.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ENDER_PEARL);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        if (upgrade >= 5) sword.setType(Material.IRON_SWORD);
        if (upgrade >= 9) sword.setType(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        if (upgrade >= 2) {
            int steakAmount = 1;
            if (upgrade >= 3) steakAmount = 2;
            if (upgrade >= 4) steakAmount = 3;
            items.put(1, Utils.getSteaks(steakAmount, getName()));
        }

        if (upgrade >= 4) {
            int healPotionLevel = 1;
            if (upgrade >= 8) healPotionLevel = 2;
            items.put(2, Utils.getPotionHeal(6, healPotionLevel));
        }

        if (upgrade >= 6) {
            int speedPotionLevel = 1;
            if (upgrade >= 8) speedPotionLevel = 2;
            items.put(3, Utils.getPotionSpeed(speedPotionLevel));
        }

        if (upgrade >= 7) {
            ItemStack helmet = new ItemStack(Material.IRON_HELMET);
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            enchants.put(Enchantment.DURABILITY, 1);
            enchants.put(Enchantment.WATER_WORKER, 1);
            items.put(4, ItemStackCreator.createItemStack(helmet, 1, ChatColor.AQUA + getName() + " Helmet", null, enchants));
        }

        return items;
    }

    @Override
    public int getPrice() {
        return 600;
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
        return "Wrath";
    }

    @Override
    public int getXPPerHit() {
        return 20;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = -0.5 + (0.5 * upgrade);
        return Arrays.asList("§7Unleash the wrath of", "§7Herobrine striking all", "§7nearby enemies for §c" + damage, "§7damage.");
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
    public void onKill(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;

        Player p = e.getEntity().getKiller();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: Power
        Upgrade upgrade = new Upgrade(p, this, null);
        double duration = 1.5 + (0.5 * upgrade.getCurrentUpgrade());

        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (20 * duration), 0));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getDamager();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: Flurry
        Upgrade upgrade = new Upgrade(p, this, null);
        double chance = (21 + (6 * upgrade.getCurrentUpgrade())) / 100.0;
        if (new Random().nextDouble() <= chance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1));
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
        com.jules.kitpvp.abilities.Wrath.use(p, upgrade.getCurrentUpgrade());
    }
}
