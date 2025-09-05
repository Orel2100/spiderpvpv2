package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.HomingTask;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Hunter extends KitClass {

    private ArrayList<String> cd = new ArrayList<>();

    public Hunter() {
        super(
                "Hunter",
                new String[]{"This archery class powers", "up with the thrill of the", "hunt."},
                20000,
                new ItemStackCreator(Material.BOW, "§bHunter").build(),
                new Upgrade(UpgradeType.SWORD, 4),
                new Upgrade(UpgradeType.BOW, 9),
                new Upgrade(UpgradeType.HELMET, 5),
                new Upgrade(UpgradeType.BOOTS, 5),
                new Upgrade(UpgradeType.POTION, 2), // for steaks
                new Upgrade(UpgradeType.GOLDEN_APPLE, 2),
                new Upgrade(UpgradeType.ABILITY, 5)
        );
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        // Sword
        int swordLevel = mPlayer.getUpgradeLevel(UpgradeType.SWORD);
        ItemStack sword;
        if (swordLevel == 1) {
            sword = new ItemStack(Material.WOODEN_SWORD);
        } else if (swordLevel == 2) {
            sword = new ItemStack(Material.STONE_SWORD);
        } else if (swordLevel == 3) {
            sword = new ItemStack(Material.IRON_SWORD);
        } else { // level 4+
            sword = new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).build();
        }
        items.add(sword);

        // Bow & Arrows
        int bowLevel = mPlayer.getUpgradeLevel(UpgradeType.BOW);
        ItemStack bow;
        int arrowCount = 0;
        if (bowLevel <= 4) {
            bow = new ItemStack(Material.BOW);
            if (bowLevel == 1) arrowCount = 12;
            else if (bowLevel == 2) arrowCount = 16;
            else if (bowLevel == 3) arrowCount = 20;
            else arrowCount = 24; // level 4
        } else { // level 5+
            bow = new ItemStackCreator(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build();
            if (bowLevel == 5) arrowCount = 32;
            else if (bowLevel == 6) arrowCount = 40;
            else if (bowLevel == 7) arrowCount = 48;
            else if (bowLevel == 8) arrowCount = 56;
            else arrowCount = 64; // level 9
        }
        items.add(bow);
        if (arrowCount > 0) items.add(new ItemStack(Material.ARROW, arrowCount));


        // Helmet
        int helmetLevel = mPlayer.getUpgradeLevel(UpgradeType.HELMET);
        if (helmetLevel >= 2) {
            ItemStack helmet;
            if (helmetLevel == 2) {
                helmet = new ItemStack(Material.IRON_HELMET);
            } else if (helmetLevel == 3) {
                helmet = new ItemStackCreator(Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build();
            } else if (helmetLevel == 4) {
                helmet = new ItemStackCreator(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build();
            } else { // level 5+
                helmet = new ItemStackCreator(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build();
            }
            items.add(helmet);
        }

        // Boots
        int bootsLevel = mPlayer.getUpgradeLevel(UpgradeType.BOOTS);
        if (bootsLevel >= 2) {
            ItemStack boots;
            if (bootsLevel == 2) {
                boots = new ItemStack(Material.CHAINMAIL_BOOTS);
            } else if (bootsLevel == 3) {
                boots = new ItemStack(Material.IRON_BOOTS);
            } else if (bootsLevel == 4) {
                boots = new ItemStackCreator(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build();
            } else { // level 5+
                boots = new ItemStackCreator(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build();
            }
            items.add(boots);
        }

        // Steak (using POTION)
        int steakLevel = mPlayer.getUpgradeLevel(UpgradeType.POTION);
        if (steakLevel == 1) {
            items.add(new ItemStack(Material.COOKED_BEEF, 2));
        } else if (steakLevel >= 2) {
            items.add(new ItemStack(Material.COOKED_BEEF, 3));
        }

        // Golden Apple
        int gappleLevel = mPlayer.getUpgradeLevel(UpgradeType.GOLDEN_APPLE);
        if (gappleLevel >= 2) {
            items.add(new ItemStack(Material.GOLDEN_APPLE, 2));
        }

        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.HUNTER;
    }

    @Override
    public void onBowShoot(EntityShootBowEvent e) {
        if (e.getForce() != 1.0) return;
        if (!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        if (!cd.contains(p.getName())) return;
        if (!(e.getProjectile() instanceof Arrow)) return;

        double minAngle = 6.283185307179586D;
        Entity minEntity = null;
        for (Entity entity : p.getNearbyEntities(64.0D, 64.0D, 64.0D)) {
            if (p.hasLineOfSight(entity) && !entity.isDead() && (entity instanceof Player)) {
                Vector toTarget = entity.getLocation().toVector().clone().subtract(p.getLocation().toVector());
                double angle = e.getProjectile().getVelocity().angle(toTarget);
                if (angle < minAngle) {
                    minAngle = angle;
                    minEntity = entity;
                }
            }
        }
        if (minEntity != null) {
            new HomingTask((Arrow) e.getProjectile(), (LivingEntity) minEntity, KitPVP.getInstance());
        }
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("LEFT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.BOW) return;
        if (p.getLevel() < 100) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int abilityLevel = mPlayer.getUpgradeLevel(UpgradeType.ABILITY);
        int duration = 6 + abilityLevel;

        p.setLevel(0);
        p.setExp(0);
        cd.add(p.getName());

        Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> {
            cd.remove(p.getName());
            p.sendMessage(ChatColor.GREEN + "Your Eagle's Eye wore off.");
        }, 20 * duration);
    }
}
