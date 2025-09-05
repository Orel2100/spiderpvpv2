package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.ExplosiveArrow;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Skeleton extends KitClass {

    public Skeleton() {
        super(
                "Skeleton",
                new String[]{"The Skeleton class makes", "excellent use of ranged", "abilities and weapons."},
                800,
                new ItemStackCreator(Material.BONE, "§bSkeleton").build(),
                new Upgrade(UpgradeType.BOW, 4),
                new Upgrade(UpgradeType.AXE, 1),
                new Upgrade(UpgradeType.HELMET, 3),
                new Upgrade(UpgradeType.POTION, 4)
        );
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        // Bow and Arrows
        int bowLevel = mPlayer.getUpgradeLevel(UpgradeType.BOW);
        ItemStackCreator bow = new ItemStackCreator(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, bowLevel);
        if (bowLevel >= 3) bow.addEnchantment(Enchantment.DURABILITY, 1);
        items.add(bow.build());
        items.add(new ItemStack(Material.ARROW, 30 + (bowLevel * 5)));

        // Axe
        if (mPlayer.getUpgradeLevel(UpgradeType.AXE) >= 1) {
            items.add(new ItemStackCreator(Material.IRON_AXE).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DURABILITY, 1).build());
        }

        // Helmet
        int helmetLevel = mPlayer.getUpgradeLevel(UpgradeType.HELMET);
        if (helmetLevel >= 1) {
            ItemStackCreator helmet = new ItemStackCreator(Material.CHAINMAIL_HELMET).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
            if (helmetLevel >= 2) helmet = new ItemStackCreator(Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 3);
            if (helmetLevel >= 3) helmet = new ItemStackCreator(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
            items.add(helmet.build());
        }

        // Consumables
        items.add(new ItemStack(Material.COOKED_BEEF, 3));
        int potionLevel = mPlayer.getUpgradeLevel(UpgradeType.POTION);
        if (potionLevel >= 1) items.add(Utils.getPotionHeal(8, 1));
        if (potionLevel >= 2) items.add(Utils.getPotionSpeed(1));
        if (potionLevel >= 3) items.add(Utils.getPotionSpeed(2));
        if (potionLevel >= 4) items.add(Utils.getPotionHeal(8, 2));

        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.SKELETON;
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow) || !(e.getEntity() instanceof Player)) return;
        Arrow a = (Arrow) e.getDamager();
        if (!(a.getShooter() instanceof Player)) return;
        Player p = (Player) a.getShooter();
        if (p == e.getEntity()) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        if (mPlayer.getKit() != Kit.SKELETON) return;

        // Salvaging
        double chance = (10.0 * mPlayer.getUpgradeLevel(UpgradeType.BOW)) / 100.0;
        if (new Random().nextDouble() <= chance) {
            p.getInventory().addItem(new ItemStack(Material.ARROW));
        }

        // Agile Hands
        double agileChance = (3.0 + (2.0 * mPlayer.getUpgradeLevel(UpgradeType.BOW))) / 100.0;
        if (new Random().nextDouble() <= agileChance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 3, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 1));
        }

        if (ExplosiveArrow.exArrow.containsKey(a)) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow)) return;
        Arrow a = (Arrow) e.getEntity();
        if (ExplosiveArrow.exArrow.containsKey(a)) {
            Player p = ExplosiveArrow.exArrow.get(a);
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            double damage = 1.5 + (0.5 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));

            for (Entity ent : a.getNearbyEntities(4, 4, 4)) {
                if (ent instanceof LivingEntity && ent != p) {
                    Utils.realDamage(ent, p, damage);
                }
            }
            ExplosiveArrow.exArrow.remove(a);
        }
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        if (mPlayer.getKit() != Kit.SKELETON) return;
        if (p.getLevel() < 100) return;

        boolean isBow = p.getItemInHand().getType() == Material.BOW;
        boolean isSword = Utils.isUsingSword(p.getItemInHand());

        if ((e.getAction().name().contains("LEFT") && isBow) || (e.getAction().name().contains("RIGHT") && isSword)) {
            ExplosiveArrow.use(p, mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
            p.setLevel(0);
            p.setExp(0);
        }
    }
}
