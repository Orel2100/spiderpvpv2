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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Spider extends KitClass {

    private static ArrayList<String> cd = new ArrayList<>();

    public Spider() {
        super(
                "Spider",
                new String[]{"The Spider class uses agile", "paths for combat."},
                900,
                new ItemStackCreator(Material.COBWEB, "§bSpider").build(),
                new Upgrade(UpgradeType.SWORD, 4),
                new Upgrade(UpgradeType.BOOTS, 3),
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
        else if (swordLevel == 3) items.add(new ItemStack(Material.IRON_SWORD));
        else items.add(new ItemStack(Material.DIAMOND_SWORD));

        // Boots
        int bootsLevel = mPlayer.getUpgradeLevel(UpgradeType.BOOTS);
        if (bootsLevel == 1) items.add(new ItemStack(Material.CHAINMAIL_BOOTS));
        else if (bootsLevel == 2) items.add(new ItemStack(Material.IRON_BOOTS));
        else items.add(new ItemStackCreator(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.DURABILITY, 3).build());

        // Consumables
        items.add(new ItemStack(Material.COOKED_BEEF, 3));
        int potionLevel = mPlayer.getUpgradeLevel(UpgradeType.POTION);
        if (potionLevel >= 1) items.add(Utils.getPotionSpeed(1));
        if (potionLevel >= 2) items.add(Utils.getPotionHeal(8, 1));
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
        return Kit.SPIDER;
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (e.getCause() == DamageCause.FALL) {
            // Leap ability landing
            if (cd.contains(p.getName())) {
                cd.remove(p.getName());
                MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
                double radius = 2.75 + (0.25 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
                for (Entity ent : p.getNearbyEntities(radius, radius, radius)) {
                    if (ent instanceof LivingEntity && ent != p) {
                        ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 4, 1));
                    }
                }
            }

            // Skill: Drop Shock
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            double maxDamage = 4 + (1 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
            double newDamage = e.getDamage() * ((110 + (10 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0);
            if (newDamage > maxDamage) newDamage = maxDamage;

            for (Entity ent : p.getNearbyEntities(4, 4, 4)) {
                if (ent instanceof LivingEntity && ent != p) {
                    Utils.realDamage(ent, p, newDamage);
                }
            }
        }
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        com.jules.kitpvp.abilities.Leap.use(p);
        cd.add(p.getName());
    }
}
