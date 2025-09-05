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
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Herobrine extends KitClass {

    public Herobrine() {
        super(
                "Herobrine",
                new String[]{"The Herobrine class uses", "supernatural abilities to", "attack and destroy your", "enemies."},
                600,
                new ItemStackCreator(Material.ENDER_PEARL, "§bHerobrine").build(),
                new Upgrade(UpgradeType.SWORD, 3),
                new Upgrade(UpgradeType.HELMET, 1),
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
        if (swordLevel == 1) items.add(new ItemStack(Material.STONE_SWORD));
        else if (swordLevel == 2) items.add(new ItemStack(Material.IRON_SWORD));
        else items.add(new ItemStack(Material.DIAMOND_SWORD));

        // Helmet
        if (mPlayer.getUpgradeLevel(UpgradeType.HELMET) >= 1) {
            items.add(new ItemStackCreator(Material.IRON_HELMET)
                    .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addEnchantment(Enchantment.DURABILITY, 1)
                    .addEnchantment(Enchantment.WATER_WORKER, 1)
                    .build());
        }

        // Consumables
        items.add(new ItemStack(Material.COOKED_BEEF, 3));
        int potionLevel = mPlayer.getUpgradeLevel(UpgradeType.POTION);
        if (potionLevel >= 1) items.add(Utils.getPotionHeal(6, 1));
        if (potionLevel >= 2) items.add(Utils.getPotionSpeed(1));
        if (potionLevel >= 3) items.add(Utils.getPotionHeal(6, 2));
        if (potionLevel >= 4) items.add(Utils.getPotionSpeed(2));

        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.HEROBRINE;
    }

    @Override
    public void onKill(Player p, Player killed) {
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        double duration = 1.5 + (0.5 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (20 * duration), 0));
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getDamager();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        if (mPlayer.getKit() != Kit.HEROBRINE) return;

        double chance = (21 + (6 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
        if (new Random().nextDouble() <= chance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1));
        }
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        com.jules.kitpvp.abilities.Wrath.use(p, mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
    }
}
