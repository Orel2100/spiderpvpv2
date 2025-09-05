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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shaman extends KitClass {

    public Shaman() {
        super(
                "Shaman",
                new String[]{"The Shaman class uses", "spiritual abilities to", "attack and defend."},
                10000,
                new ItemStackCreator(Material.ENCHANTING_TABLE, "§bShaman").build(),
                new Upgrade(UpgradeType.SWORD, 2),
                new Upgrade(UpgradeType.BOOTS, 4),
                new Upgrade(UpgradeType.POTION, 4),
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

        // Boots
        int bootsLevel = mPlayer.getUpgradeLevel(UpgradeType.BOOTS);
        ItemStackCreator boots = new ItemStackCreator(Material.DIAMOND_BOOTS);
        if (bootsLevel == 1) boots.addEnchantment(Enchantment.PROTECTION_FALL, 1);
        else if (bootsLevel == 2) boots.addEnchantment(Enchantment.PROTECTION_FALL, 2);
        else if (bootsLevel == 3) boots.addEnchantment(Enchantment.PROTECTION_FALL, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        else boots.addEnchantment(Enchantment.PROTECTION_FALL, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        items.add(boots.build());

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
        return Kit.SHAMAN;
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getDamager();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        if (mPlayer.getKit() != Kit.SHAMAN) return;

        // Spiritual Invigoration
        double chance = (1.5 + (0.5 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
        double duration = 1.5 + (0.5 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        if (new Random().nextDouble() <= chance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (20 * duration), 1));
            if (e.getEntity() instanceof LivingEntity && !e.getEntity().isDead()) {
                ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) (20 * duration), 0));
            }
        }
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        // Heroism
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        double chance = (4.37 + (0.625 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
        double duration = 4.37 + (0.625 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        if (new Random().nextDouble() <= chance) {
            final Wolf wolf = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
            wolf.setCustomName(p.getName() + "'s wolf");
            wolf.setCustomNameVisible(true);
            wolf.setCanPickupItems(false);
            wolf.setTamed(true);
            wolf.setAdult();
            wolf.setSitting(false);
            wolf.setOwner(p);

            Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> {
                if (wolf != null && !wolf.isDead()) {
                    wolf.remove();
                }
            }, (long) (20 * duration));
        }
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        com.jules.kitpvp.abilities.Tornado.use(p, mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        p.setLevel(0);
        p.setExp(0);
    }
}
