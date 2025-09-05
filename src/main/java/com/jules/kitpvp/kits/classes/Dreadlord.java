package com.jules.kitpvp.kits.classes;

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
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dreadlord extends KitClass {

    public Dreadlord() {
        super(
                "Dreadlord",
                new String[]{"The Dreadlord class uses", "explosive abilities to", "attack and steal health."},
                10000,
                new ItemStackCreator(Material.WITHER_SKELETON_SKULL, "§bDreadlord").build(),
                new Upgrade(UpgradeType.SWORD, 3),
                new Upgrade(UpgradeType.HELMET, 2),
                new Upgrade(UpgradeType.POTION, 3),
                new Upgrade(UpgradeType.ABILITY, 5)
        );
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        // Sword
        int swordLevel = mPlayer.getUpgradeLevel(UpgradeType.SWORD);
        ItemStackCreator sword = new ItemStackCreator(Material.IRON_SWORD);
        if (swordLevel >= 2) {
            sword = new ItemStackCreator(Material.DIAMOND_SWORD);
        }
        if (swordLevel >= 3) {
            sword.addEnchantment(Enchantment.DAMAGE_UNDEAD, 1);
        }
        items.add(sword.build());

        // Helmet
        int helmetLevel = mPlayer.getUpgradeLevel(UpgradeType.HELMET);
        if (helmetLevel >= 1) {
            ItemStackCreator helmet = new ItemStackCreator(Material.DIAMOND_HELMET);
            if (helmetLevel >= 2) {
                helmet.addEnchantment(Enchantment.PROTECTION_FIRE, 1).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2);
            }
            items.add(helmet.build());
        }

        // Consumables
        items.add(new ItemStack(Material.COOKED_BEEF, 3));
        int potionLevel = mPlayer.getUpgradeLevel(UpgradeType.POTION);
        if (potionLevel >= 1) items.add(Utils.getPotionHeal(8, 1));
        if (potionLevel >= 2) items.add(Utils.getPotionSpeed(1));
        if (potionLevel >= 3) items.add(Utils.getPotionSpeed(2));

        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.DREADLORD;
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof WitherSkull)) return;
        WitherSkull ws = (WitherSkull) e.getEntity();
        if (!(ws.getShooter() instanceof Player)) return;

        Player p = (Player) ws.getShooter();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        if (mPlayer.getKit() != Kit.DREADLORD) return;

        double damage = 2.25 + (0.75 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        ws.getWorld().playSound(ws.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 5);
        for (Entity ent : ws.getNearbyEntities(2.5, 2.5, 2.5)) {
            if (ent instanceof Player && ent != p) {
                Utils.realDamage(ent, p, damage);
            }
        }
    }

    @Override
    public void onKill(Player p, Player killed) {
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        double regen = 1.5 + (0.5 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        double strength = 0.5 + (0.5 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) (20 * regen), 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (20 * strength), 0));
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.IRON_ORE) {
            MPlayer mPlayer = MPlayer.getMPlayer(e.getPlayer().getUniqueId());
            double chance = (16.0 + (9.0 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
            if (new Random().nextDouble() <= chance) {
                e.setDropItems(false);
                e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT));
            }
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getDamager();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        if (mPlayer.getKit() != Kit.DREADLORD) return;

        double chance = (10.37 + (1.625 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
        if (new Random().nextDouble() <= chance) {
            p.setHealth(Math.min(p.getHealth() + 1, p.getMaxHealth()));
            p.setFoodLevel(Math.min(p.getFoodLevel() + 6, 20));
        }
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        com.jules.kitpvp.abilities.ShadowBurst.use(p);
        p.setLevel(0);
        p.setExp(0);
    }
}
