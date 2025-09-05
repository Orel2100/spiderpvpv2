package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arcanist extends KitClass {

    public Arcanist() {
        super(
                "Arcanist",
                new String[]{"The Arcanist class uses his", "power to gain energy faster", "than most"},
                10000,
                new ItemStackCreator(Material.BOOK, "§bArcanist").build(),
                new Upgrade(UpgradeType.SWORD, 2),
                new Upgrade(UpgradeType.LEGGINGS, 2),
                new Upgrade(UpgradeType.POTION, 2),
                new Upgrade(UpgradeType.ABILITY, 5)
        );
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        // Sword
        if (mPlayer.getUpgradeLevel(UpgradeType.SWORD) == 1) {
            items.add(new ItemStack(Material.IRON_SWORD));
        } else {
            items.add(new ItemStack(Material.DIAMOND_SWORD));
        }

        // Leggings
        if (mPlayer.getUpgradeLevel(UpgradeType.LEGGINGS) == 1) {
            items.add(new ItemStackCreator(Material.DIAMOND_LEGGINGS).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1).build());
        } else {
            items.add(new ItemStackCreator(Material.DIAMOND_LEGGINGS).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
        }

        // Potions
        if (mPlayer.getUpgradeLevel(UpgradeType.POTION) == 1) {
            items.add(Utils.getPotionHeal(8, 1));
        } else {
            items.add(Utils.getPotionHeal(8, 2));
        }

        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.ARCANIST;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        p.setExp(0);
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int upgrade = mPlayer.getUpgradeLevel(UpgradeType.ABILITY);
        double damage = 5.25 + (0.75 * upgrade);
        com.jules.kitpvp.abilities.Beam.shoot(p, damage);
        p.setLevel(0);
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        // secondSkill logic
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int upgrade = mPlayer.getUpgradeLevel(UpgradeType.ABILITY);
        double chance = (6.5 + (upgrade * 1.5)) / 100;
        double random = new Random().nextDouble();
        if (random <= chance) {
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            for (Entity ent : p.getNearbyEntities(2, 2, 2)) {
                if (ent instanceof LivingEntity && ent != p) {
                    ((LivingEntity) ent).damage(2);
                }
            }
        }
    }

    @Override
    public void onKill(Player p, Player killed) {
        // firstSkill logic
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int upgrade = mPlayer.getUpgradeLevel(UpgradeType.ABILITY);
        double damage = 0.5 + (upgrade * 0.5);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 6, 2));
        int time = (int) (20 * damage);
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, time, 1));
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType().name().contains("ORE")) {
            Utils.addLevel(e.getPlayer(), 10);
        }
    }
}
