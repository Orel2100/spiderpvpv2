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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Creeper extends KitClass {

    public static ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Creeper";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Creeper class uses", "explosion based powers to", "win. Energy is gained by", "hitting players in melee", "range.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.TNT);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        if (upgrade >= 5) sword.setType(Material.STONE_SWORD);
        if (upgrade >= 8) sword.setType(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        int steakAmount = 2;
        if (upgrade >= 4) steakAmount = 3;
        items.put(1, Utils.getSteaks(steakAmount, getName()));

        int healPotionLevel = 1;
        if (upgrade >= 9) healPotionLevel = 2;
        items.put(2, Utils.getPotionHeal(8, healPotionLevel));

        if (upgrade >= 5) {
            int speedPotionLevel = 1;
            if (upgrade >= 8) speedPotionLevel = 2;
            items.put(3, Utils.getPotionSpeed(speedPotionLevel));
        }

        ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        if (upgrade >= 3) leggings.setType(Material.IRON_LEGGINGS);
        if (upgrade >= 9) leggings.setType(Material.DIAMOND_LEGGINGS);

        HashMap<Enchantment, Integer> enchants = new HashMap<>();
        int blastProt = 1;
        if (upgrade >= 2) blastProt = 2;
        if (upgrade >= 6) blastProt = 3;
        if (upgrade >= 7) blastProt = 4;
        enchants.put(Enchantment.PROTECTION_EXPLOSIONS, blastProt);
        items.put(4, ItemStackCreator.createItemStack(leggings, 1, ChatColor.AQUA + getName() + " Leggings", null, enchants));

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
        return "Detonate";
    }

    @Override
    public int getXPPerHit() {
        return 20;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 1.25 + (0.75 * upgrade);
        return Arrays.asList("§7Detonate an explosion that", "§7deals up to §c" + Utils.round(damage, 2) + "§7 damage to", "§7nearby players. However, it", "§7takes 3 seconds to", "§7detonate.");
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
    public void onMine(BlockBreakEvent e) {
        MPlayer player = MPlayerManager.getMPlayer(e.getPlayer().getName());
        if (player.getCurrentClass() != this) return;
        if (e.getBlock().getType() == Material.COAL_ORE) {
            // Skill: Sulphur
            double chance = (7 + (2 * new Upgrade(e.getPlayer(), this, null).getCurrentUpgrade())) / 100.0;
            if (new Random().nextDouble() <= chance) {
                e.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.TNT));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        final Player p = (Player) e.getEntity();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        if (p.getHealth() - e.getDamage() <= 14) {
            if (cd.contains(p.getName())) return;
            // Skill: Last Stand
            double duration = 1.25 + (0.75 * new Upgrade(p, this, null).getCurrentUpgrade());
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (20 * duration), 1));
            cd.add(p.getName());
            Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> cd.remove(p.getName()), 20 * 15);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: 50% Unstable, 50% Volatile
        double chance = (10 + (10 * new Upgrade(p, this, null).getCurrentUpgrade())) / 100.0;
        if (new Random().nextDouble() <= chance) {
            org.bukkit.entity.Creeper creeper = (org.bukkit.entity.Creeper) p.getWorld().spawnEntity(p.getLocation(), EntityType.CREEPER);
            creeper.setCustomName(ChatColor.AQUA + p.getName() + "'s Creeper");
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
        com.jules.kitpvp.abilities.Detonate.use(p, upgrade.getCurrentUpgrade());
        p.setLevel(0);
        p.setExp(0);
    }
}
