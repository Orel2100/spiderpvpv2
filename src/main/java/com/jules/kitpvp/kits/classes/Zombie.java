package com.jules.kitpvp.kits.classes;

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
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Zombie extends KitClass {

    public Zombie() {
        super(
                "Zombie",
                new String[]{"The Zombie class focuses on", "defensive gameplay and", "boosts."},
                0,
                new ItemStackCreator(Material.ROTTEN_FLESH, "§bZombie").build(),
                new Upgrade(UpgradeType.SWORD, 3),
                new Upgrade(UpgradeType.CHESTPLATE, 6),
                new Upgrade(UpgradeType.POTION, 4),
                new Upgrade(UpgradeType.ABILITY, 5)
        );
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        // Sword
        int swordLevel = mPlayer.getUpgradeLevel(UpgradeType.SWORD);
        if (swordLevel == 1) items.add(new ItemStack(Material.WOODEN_SWORD));
        else if (swordLevel == 2) items.add(new ItemStack(Material.STONE_SWORD));
        else items.add(new ItemStack(Material.IRON_SWORD));

        // Chestplate
        int chestLevel = mPlayer.getUpgradeLevel(UpgradeType.CHESTPLATE);
        ItemStackCreator chest = new ItemStackCreator(Material.CHAINMAIL_CHESTPLATE);
        if (chestLevel >= 2) chest.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
        if (chestLevel >= 3) chest = new ItemStackCreator(Material.IRON_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
        if (chestLevel >= 4) chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        if (chestLevel >= 5) chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        if (chestLevel >= 6) chest = new ItemStackCreator(Material.DIAMOND_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        items.add(chest.build());

        // Consumables
        items.add(new ItemStack(Material.COOKED_BEEF, 2));
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
        return Kit.ZOMBIE;
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        double chance = (6.87 + (3.125 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
        if (new Random().nextDouble() <= chance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            if (mPlayer.getKit() != Kit.ZOMBIE) return;

            double chance = (3.0 + (2.0 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
            if (new Random().nextDouble() <= chance) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 3, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 0));
            }
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        double chance = (1.25 + (3.75 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
        if (new Random().nextDouble() <= chance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 3 + 10, 0));
        }
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        com.jules.kitpvp.abilities.Heal.use(p, mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        p.setLevel(0);
        p.setExp(0);
    }
}
