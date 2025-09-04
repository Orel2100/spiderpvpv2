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
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Blaze extends KitClass {

    @Override
    public String getName() {
        return "Blaze";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Blaze class uses the", "spirit of fire to enforce", "flames.");
    }

    @Override
    public ClassType getType() {
        return ClassType.HERO;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.BLAZE_ROD);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack bow = new ItemStack(Material.BOW);
        int bowPower = 1;
        if (upgrade >= 6) bowPower = 2;
        if (upgrade >= 9) bowPower = 3;
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, bowPower);
        items.put(0, ItemStackCreator.createItem(bow, ChatColor.AQUA + getName() + " Bow", 1, getSwordLore(upgrade)));

        int steakAmount = 1;
        if (upgrade >= 2) steakAmount = 2;
        if (upgrade >= 6) steakAmount = 3;
        items.put(1, Utils.getSteaks(steakAmount, getName()));

        int arrowAmount = 16;
        if (upgrade >= 2) arrowAmount = 22;
        if (upgrade >= 3) arrowAmount = 28;
        if (upgrade >= 4) arrowAmount = 32;
        if (upgrade >= 5) arrowAmount = 40;
        if (upgrade >= 6) arrowAmount = 46;
        if (upgrade >= 7) arrowAmount = 52;
        if (upgrade >= 8) arrowAmount = 58;
        if (upgrade >= 9) arrowAmount = 64;
        items.put(2, new ItemStack(Material.ARROW, arrowAmount));

        if (upgrade >= 4) {
            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
            sword.addEnchantment(Enchantment.DURABILITY, 3);
            items.put(3, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, null)); // No lore on this sword
        }

        if (upgrade >= 4) {
            ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
            if (upgrade >= 7) {
                leggings.setType(Material.IRON_LEGGINGS);
            }
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            int fireProt = 1;
            if (upgrade >= 5) fireProt = 2;
            enchants.put(Enchantment.PROTECTION_FIRE, fireProt);
            enchants.put(Enchantment.DURABILITY, 1);
            if (upgrade >= 8) {
                enchants.put(Enchantment.PROTECTION_PROJECTILE, 1);
            }
            items.put(4, ItemStackCreator.createItemStack(leggings, 1, ChatColor.AQUA + getName() + " Leggings", null, enchants));
        }

        if (upgrade >= 5) {
            int speedLevel = 1;
            if (upgrade >= 7) speedLevel = 2;
            items.put(6, Utils.getPotionSpeed(speedLevel)); // Slot 6 for speed
        }

        if (upgrade >= 7) {
            int healLevel = 1;
            if (upgrade >= 8) healLevel = 2;
            items.put(5, Utils.getPotionHeal(8, healLevel)); // Slot 5 for heal
        }

        return items;
    }

    @Override
    public int getPrice() {
        return 15000;
    }

    @Override
    public int getXPPerHit() {
        return 5; // Timer based
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
        return "Immolating Burst";
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        float damage = (float) (1.9 + (0.3 * upgrade));
        return Arrays.asList("§7Immolate your enemies,", "§7charge up your skill and", "§7shoot 3 fireballs dealing", "§c" + Utils.round(damage, 2) + "§7 damage each.");
    }

    @Override
    public HitType getHitType() {
        return HitType.TIMER;
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow)) return;
        Arrow a = (Arrow) e.getDamager();
        if (!(a.getShooter() instanceof Player)) return;

        Player p = (Player) a.getShooter();
        if (p == e.getEntity()) return;

        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: Fossil Fuels
        double chance = (7.5 + (2.5 * new Upgrade(p, this, null).getCurrentUpgrade())) / 100; // Assuming null works for a generic upgrade check
        if (new Random().nextDouble() <= chance) {
            if (e.getEntity() instanceof LivingEntity) {
                ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1));
                e.getEntity().setFireTicks(20 * 3);
            }
        }
    }

    @EventHandler
    public void onFireballDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Fireball) {
            Fireball fb = (Fireball) e.getDamager();
            if (fb.getShooter() instanceof org.bukkit.entity.Blaze) {
                e.setCancelled(true);
                e.setDamage(2);
            }
        }
    }

    @EventHandler
    public void onMining(BlockBreakEvent e) {
        MPlayer player = MPlayerManager.getMPlayer(e.getPlayer().getName());
        if (player.getCurrentClass() != this) return;

        if (e.getBlock().getType().name().contains("ORE")) {
            // Skill: Melting Point
            double chance = (43.75 + (6.25 * new Upgrade(p, this, null).getCurrentUpgrade())) / 100;
            if (new Random().nextDouble() <= chance) {
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 1));
            }
        }
    }

    @EventHandler
    public void onFireballHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Fireball) || !(e.getEntity().getShooter() instanceof Player)) return;

        Player p = (Player) e.getEntity().getShooter();
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;

        Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        float damage = (float) (1.9 + (0.3 * upgrade.getCurrentUpgrade()));

        for (Player ps : Bukkit.getOnlinePlayers()) {
            ps.playSound(e.getEntity().getLocation(), Sound.EXPLODE, 2, 2);
        }

        for (Entity ent : e.getEntity().getNearbyEntities(3, 3, 3)) {
            if (ent != p && ent instanceof LivingEntity && !ent.isDead()) {
                Utils.realDamage(ent, p, damage);
            }
        }
        e.getEntity().remove();
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
        com.jules.kitpvp.abilities.ImmolatingBurst.use(p, upgrade.getCurrentUpgrade());
        p.setLevel(0);
        p.setExp(0);
    }

    @EventHandler
    public void onFrameBreak(HangingBreakEvent e) {
        if (e.getCause() == HangingBreakEvent.RemoveCause.ENTITY || e.getCause() == HangingBreakEvent.RemoveCause.EXPLOSION) {
            e.setCancelled(true);
        }
    }
}
