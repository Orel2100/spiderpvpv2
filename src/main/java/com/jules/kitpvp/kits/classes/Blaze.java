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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Blaze extends KitClass {

    public Blaze() {
        super(
                "Blaze",
                new String[]{"The Blaze class uses the", "spirit of fire to enforce", "flames."},
                15000,
                new ItemStackCreator(Material.BLAZE_ROD, "§bBlaze").build(),
                new Upgrade(UpgradeType.BOW, 3),
                new Upgrade(UpgradeType.SWORD, 1),
                new Upgrade(UpgradeType.LEGGINGS, 3),
                new Upgrade(UpgradeType.POTION, 4),
                new Upgrade(UpgradeType.ABILITY, 5)
        );
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        // Bow and Arrows
        int bowLevel = mPlayer.getUpgradeLevel(UpgradeType.BOW);
        ItemStackCreator bow = new ItemStackCreator(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, bowLevel);
        items.add(bow.build());
        items.add(new ItemStack(Material.ARROW, 16 + (bowLevel * 6)));

        // Sword
        if (mPlayer.getUpgradeLevel(UpgradeType.SWORD) >= 1) {
            items.add(new ItemStack(Material.DIAMOND_SWORD));
        }

        // Leggings
        int leggingsLevel = mPlayer.getUpgradeLevel(UpgradeType.LEGGINGS);
        if (leggingsLevel == 1) {
            items.add(new ItemStackCreator(Material.CHAINMAIL_LEGGINGS).addEnchantment(Enchantment.PROTECTION_FIRE, 1).build());
        } else if (leggingsLevel == 2) {
            items.add(new ItemStackCreator(Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_FIRE, 2).build());
        } else {
            items.add(new ItemStackCreator(Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
        }

        // Potions
        int potionLevel = mPlayer.getUpgradeLevel(UpgradeType.POTION);
        if (potionLevel >= 1) items.add(Utils.getPotionHeal(8, 1));
        if (potionLevel >= 2) items.add(Utils.getPotionSpeed(1));
        if (potionLevel >= 3) items.add(Utils.getPotionHeal(8, 2));
        if (potionLevel >= 4) items.add(Utils.getPotionSpeed(2));

        items.add(new ItemStack(Material.COOKED_BEEF, 3));
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.BLAZE;
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow a = (Arrow) event.getDamager();
            if (a.getShooter() instanceof Player) {
                Player p = (Player) a.getShooter();
                if (p == event.getEntity()) return;
                MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
                if (mPlayer.getKit() == Kit.BLAZE) {
                    double chance = (7.5 + (2.5 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100;
                    if (new Random().nextDouble() <= chance) {
                        if (event.getEntity() instanceof LivingEntity) {
                            ((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1));
                            event.getEntity().setFireTicks(20 * 3);
                        }
                    }
                }
            }
        } else if (event.getDamager() instanceof Fireball) {
            Fireball fb = (Fireball) event.getDamager();
            if (fb.getShooter() instanceof org.bukkit.entity.Blaze) {
                if(event.getEntity() instanceof Player) {
                    MPlayer mPlayer = MPlayer.getMPlayer(event.getEntity().getUniqueId());
                    if (mPlayer.getKit() == Kit.BLAZE) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType().name().contains("ORE")) {
            MPlayer mPlayer = MPlayer.getMPlayer(e.getPlayer().getUniqueId());
            double chance = (43.75 + (6.25 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100;
            if (new Random().nextDouble() <= chance) {
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 1));
            }
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Fireball) || !(e.getEntity().getShooter() instanceof Player)) return;
        Player p = (Player) e.getEntity().getShooter();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        if (mPlayer.getKit() != Kit.BLAZE) return;

        float damage = (float) (1.9 + (0.3 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY)));
        e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 0F);
        for (Entity ent : e.getEntity().getNearbyEntities(3, 3, 3)) {
            if (ent != p && ent instanceof LivingEntity && !ent.isDead()) {
                Utils.realDamage(ent, p, damage);
            }
        }
        e.getEntity().remove();
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        com.jules.kitpvp.abilities.ImmolatingBurst.use(p, mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        p.setLevel(0);
        p.setExp(0);
    }

    @Override
    public void onHangingBreak(HangingBreakEvent e) {
        if (e.getCause() == HangingBreakEvent.RemoveCause.ENTITY || e.getCause() == HangingBreakEvent.RemoveCause.EXPLOSION) {
            e.setCancelled(true);
        }
    }
}
