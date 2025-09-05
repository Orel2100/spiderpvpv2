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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Pirate extends KitClass {

    private static ArrayList<String> cd = new ArrayList<>();

    public Pirate() {
        super(
                "Pirate",
                new String[]{"Use your cunning and wit to", "survive, or just blow", "people up with your", "parrots."},
                20000,
                new ItemStackCreator(Material.STONE_AXE, "§bPirate").build(),
                new Upgrade(UpgradeType.SWORD, 2),
                new Upgrade(UpgradeType.HELMET, 3),
                new Upgrade(UpgradeType.BOOTS, 3),
                new Upgrade(UpgradeType.POTION, 2),
                new Upgrade(UpgradeType.ABILITY, 5)
        );
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        // Sword
        if (mPlayer.getUpgradeLevel(UpgradeType.SWORD) == 1) items.add(new ItemStack(Material.STONE_SWORD));
        else items.add(new ItemStack(Material.IRON_SWORD));

        // Helmet
        int helmetLevel = mPlayer.getUpgradeLevel(UpgradeType.HELMET);
        ItemStackCreator helmet = new ItemStackCreator(Material.IRON_HELMET);
        if (helmetLevel >= 2) helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        if (helmetLevel >= 3) helmet = new ItemStackCreator(Material.DIAMOND_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        items.add(helmet.build());

        // Boots
        int bootsLevel = mPlayer.getUpgradeLevel(UpgradeType.BOOTS);
        if (bootsLevel == 1) items.add(new ItemStack(Material.CHAINMAIL_BOOTS));
        else if (bootsLevel == 2) items.add(new ItemStack(Material.IRON_BOOTS));
        else items.add(new ItemStack(Material.DIAMOND_BOOTS));

        // Consumables
        items.add(new ItemStack(Material.COOKED_BEEF, 3));
        if (mPlayer.getUpgradeLevel(UpgradeType.POTION) >= 1) items.add(Utils.getPotionHeal(8, 1));
        if (mPlayer.getUpgradeLevel(UpgradeType.POTION) >= 2) items.add(Utils.getPotionHeal(8, 2));

        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.PIRATE;
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof WitherSkull) || !(e.getEntity().getShooter() instanceof Player)) return;

        WitherSkull ws = (WitherSkull) e.getEntity();
        Player shooter = (Player) e.getEntity().getShooter();
        MPlayer mShooter = MPlayer.getMPlayer(shooter.getUniqueId());
        if (mShooter.getKit() != Kit.PIRATE) return;

        double damage = 5.5 + (0.5 * mShooter.getUpgradeLevel(UpgradeType.ABILITY));
        for (Entity ent : ws.getNearbyEntities(3, 3, 3)) {
            if (ent instanceof LivingEntity && ent != shooter) {
                Utils.realDamage(ent, shooter, damage);
            }
        }
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        if (p.getHealth() - event.getDamage() <= 10) {
            if (cd.contains(p.getName())) return;
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1));
            cd.add(p.getName());
            Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> cd.remove(p.getName()), 20 * 30);
        }
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof WitherSkull) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        com.jules.kitpvp.abilities.CannonFire.use(p);
        p.setLevel(0);
        p.setExp(0);
    }
}
