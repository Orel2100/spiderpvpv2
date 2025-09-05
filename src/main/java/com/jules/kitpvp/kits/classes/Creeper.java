package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Creeper extends KitClass {

    private static ArrayList<String> cd = new ArrayList<>();

    public Creeper() {
        super(
                "Creeper",
                new String[]{"The Creeper class uses", "explosion based powers to", "win."},
                800,
                new ItemStackCreator(Material.TNT, "§bCreeper").build(),
                new Upgrade(UpgradeType.SWORD, 3),
                new Upgrade(UpgradeType.LEGGINGS, 6),
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

        // Leggings
        int leggingsLevel = mPlayer.getUpgradeLevel(UpgradeType.LEGGINGS);
        if (leggingsLevel == 1) items.add(new ItemStackCreator(Material.CHAINMAIL_LEGGINGS).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1).build());
        else if (leggingsLevel == 2) items.add(new ItemStackCreator(Material.CHAINMAIL_LEGGINGS).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2).build());
        else if (leggingsLevel == 3) items.add(new ItemStackCreator(Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2).build());
        else if (leggingsLevel == 4) items.add(new ItemStackCreator(Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 3).build());
        else if (leggingsLevel == 5) items.add(new ItemStackCreator(Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4).build());
        else items.add(new ItemStackCreator(Material.DIAMOND_LEGGINGS).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4).build());

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
        return Kit.CREEPER;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.COAL_ORE) {
            MPlayer mPlayer = MPlayer.getMPlayer(e.getPlayer().getUniqueId());
            double chance = (7 + (2 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
            if (new Random().nextDouble() <= chance) {
                e.getPlayer().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.TNT));
            }
        }
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        if (p.getHealth() - event.getDamage() <= 14) {
            if (cd.contains(p.getName())) return;
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            double duration = 1.25 + (0.75 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (20 * duration), 1));
            cd.add(p.getName());
            Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> cd.remove(p.getName()), 20 * 15);
        }
    }

    @Override
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        double chance = (10 + (10 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
        if (new Random().nextDouble() <= chance) {
            org.bukkit.entity.Creeper creeper = (org.bukkit.entity.Creeper) p.getWorld().spawnEntity(p.getLocation(), EntityType.CREEPER);
            creeper.setCustomName(ChatColor.AQUA + p.getName() + "'s Creeper");
        }
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        com.jules.kitpvp.abilities.Detonate.use(p, mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        p.setLevel(0);
        p.setExp(0);
    }
}
