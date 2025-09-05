package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pigman extends KitClass {

    private static ArrayList<String> cd = new ArrayList<>();

    public Pigman() {
        super(
                "Pigman",
                new String[]{"Half man, half pig, half..", "Oh wait! Feel the power of", "pork!"},
                15000,
                new ItemStackCreator(Material.COOKED_PORKCHOP, "§bPigman").build(),
                new Upgrade(UpgradeType.SWORD, 2),
                new Upgrade(UpgradeType.CHESTPLATE, 5),
                new Upgrade(UpgradeType.POTION, 3),
                new Upgrade(UpgradeType.ABILITY, 5)
        );
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        // Sword
        if (mPlayer.getUpgradeLevel(UpgradeType.SWORD) == 1) items.add(new ItemStack(Material.IRON_SWORD));
        else items.add(new ItemStack(Material.DIAMOND_SWORD));

        // Chestplate
        int chestLevel = mPlayer.getUpgradeLevel(UpgradeType.CHESTPLATE);
        ItemStackCreator chest = new ItemStackCreator(Material.GOLDEN_CHESTPLATE);
        if (chestLevel >= 2) chest = new ItemStackCreator(Material.IRON_CHESTPLATE);
        if (chestLevel >= 3) chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        if (chestLevel >= 4) chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        if (chestLevel >= 5) {
            chest = new ItemStackCreator(Material.DIAMOND_CHESTPLATE)
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchantment(Enchantment.DURABILITY, 3);
        }
        items.add(chest.build());

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
        return Kit.PIGMAN;
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        if (cd.contains(p.getName())) return;

        if (p.getHealth() - event.getDamage() <= 10) {
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            double duration = 1.5 + (0.5 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
            cd.add(p.getName());
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (20 * duration), 1));
            Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> cd.remove(p.getName()), 20 * 30);
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || !(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getDamager();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        if (mPlayer.getKit() != Kit.PIGMAN) return;

        double chance = (1.3 + (0.7 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
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

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        com.jules.kitpvp.abilities.BurningSoul.use(p, mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
    }
}
