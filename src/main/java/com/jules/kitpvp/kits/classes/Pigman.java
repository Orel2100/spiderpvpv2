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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Pigman extends KitClass {

    public static ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Pigman";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Half man, half pig, half..", "Oh wait! Feel the power of", "pork!");
    }

    @Override
    public ClassType getType() {
        return ClassType.HERO;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.GRILLED_PORK);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        if (upgrade >= 4) sword.setType(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        items.put(1, Utils.getPotionHeal(8, 1));

        if(upgrade >= 2){
            int steakAmount = 1;
            if(upgrade >= 5) steakAmount = 2;
            if(upgrade >= 7) steakAmount = 3;
            items.put(2, Utils.getSteaks(steakAmount, getName()));
        }

        if(upgrade >= 3){
            ItemStack chestplate = new ItemStack(Material.GOLD_CHESTPLATE);
            if(upgrade >= 5) chestplate.setType(Material.IRON_CHESTPLATE);
            if(upgrade >= 9) chestplate.setType(Material.DIAMOND_CHESTPLATE);

            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            if(upgrade >= 6) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            if(upgrade >= 7) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            if(upgrade >= 9) enchants.put(Enchantment.DURABILITY, 3);

            items.put(4, ItemStackCreator.createItemStack(chestplate, 1, ChatColor.AQUA + getName() + " Chestplate", null, enchants));
        }

        if(upgrade >= 6){
            int speedLevel = 1;
            if(upgrade >= 8) speedLevel = 2;
            items.put(3, Utils.getPotionSpeed(speedLevel));
        }

        return items;
    }

    @Override
    public int getPrice() {
        return 15000;
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
        return "Burning Soul";
    }

    @Override
    public int getXPPerHit() {
        return 10;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 0.875 + (0.125 * upgrade);
        double strengthSeconds = 0.875 + (0.125 * upgrade);
        int bubbleSeconds = (upgrade < 5) ? 3 : (upgrade < 9 ? 4 : 5);

        return Arrays.asList("§7Summon a fire bubble that", "§7deals §c" + damage + "§7 damage every", "§7second for §c" + bubbleSeconds + "§7 seconds", "§7and gives Strength I to the", "§7player for §c" + strengthSeconds + "§7 seconds.");
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
    public void onDamage(EntityDamageEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) return;

        final Player p = (Player) e.getEntity();
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (cd.contains(p.getName())) return;

        if (p.getHealth() - e.getDamage() <= 10) {
            // Skill: Endurance
            Upgrade upgrade = new Upgrade(p, this, null);
            double duration = 1.5 + (0.5 * upgrade.getCurrentUpgrade());

            cd.add(p.getName());
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (20 * duration), 1));

            Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> cd.remove(p.getName()), 20 * 30);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || !(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getDamager();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: Valor
        Upgrade upgrade = new Upgrade(p, this, null);
        double chance = (1.3 + (0.7 * upgrade.getCurrentUpgrade())) / 100.0;

        if (new Random().nextDouble() <= chance) {
            for (Entity ent : p.getNearbyEntities(8, 8, 8)) {
                if (ent instanceof Player) {
                    Player nearbyPlayer = (Player) ent;
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 8, 0));
                    if (nearbyPlayer.getHealth() < nearbyPlayer.getMaxHealth()) {
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 8, 0));
                    }
                }
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

        Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        com.jules.kitpvp.abilities.BurningSoul.use(p, upgrade.getCurrentUpgrade());
    }
}
