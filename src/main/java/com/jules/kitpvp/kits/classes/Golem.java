package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.kits.HitType;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.player.MPlayerManager;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Golem extends KitClass {

    public ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Golem";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Golem class uses the", "all mighty powers of the ", "iron god.");
    }

    @Override
    public ClassType getType() {
        return ClassType.HERO;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.IRON_CHESTPLATE);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        if (upgrade >= 6) sword.setType(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        int steakAmount = 1;
        if (upgrade >= 2) steakAmount = 2;
        if (upgrade >= 6) steakAmount = 3;
        items.put(1, Utils.getSteaks(steakAmount, getName()));

        if (upgrade >= 3) {
            int regenPotionLevel = 1;
            if (upgrade >= 4) regenPotionLevel = 2;
            // Assuming Utils.getPotionRegeneration exists. If not, this needs to be created.
            // items.put(2, Utils.getPotionRegeneration(regenPotionLevel, getName()));
        }

        if (upgrade >= 9) {
            ItemStack slowpot = new ItemStack(Material.POTION, 1, (short) 16426);
            items.put(3, slowpot);
        }

        if (upgrade >= 2) {
            ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
            if (upgrade >= 7) chestplate.setType(Material.DIAMOND_CHESTPLATE);
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.DURABILITY, 3);
            if (upgrade >= 8) enchants.put(Enchantment.PROTECTION_EXPLOSIONS, 1);
            if (upgrade >= 9) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            items.put(4, ItemStackCreator.createItemStack(chestplate, 1, ChatColor.AQUA + getName() + " Chestplate", null, enchants));
        }

        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        if (upgrade >= 5) boots.setType(Material.DIAMOND_BOOTS);
        HashMap<Enchantment, Integer> enchants = new HashMap<>();
        enchants.put(Enchantment.DURABILITY, 3);
        if (upgrade >= 9) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        items.put(5, ItemStackCreator.createItemStack(boots, 1, ChatColor.AQUA + getName() + " Boots", null, enchants));

        return items;
    }

    @Override
    public int getPrice() {
        return 10000;
    }

    @Override
    public int getUpgradePrice(int level) {
        switch (level) {
            case 1: return 0;
            case 2: return 200;
            case 3: return 500;
            case 4: return 1200;
            case 5: return 2400;
            case 6: return 7000;
            case 7: return 13000;
            case 8: return 17000;
            case 9: return 28000;
            default: return 28000;
        }
    }

    @Override
    public String getAbilityName() {
        return "Iron Punch";
    }

    @Override
    public int getXPPerHit() {
        return 10;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 0.5 + (0.5 * upgrade);
        return Arrays.asList("§7Cast a hexagon causing §c" + damage, "§7damage in 5 blocks radius.");
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;

        final Player killer = e.getEntity().getKiller();
        MPlayer player = MPlayerManager.getMPlayer(killer.getName());
        if (player.getCurrentClass() != this) return;

        if (cd.contains(killer.getName())) return;

        // Skill: Iron Heart
        Upgrade upgrade = new Upgrade(killer, this, null);
        double duration = 1.75 + (1.25 * upgrade.getCurrentUpgrade());

        cd.add(killer.getName());
        killer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, (int) (20 * duration), 1));
        Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> cd.remove(killer.getName()), 20 * 45);
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Arrow)) return;

        Player p = (Player) e.getEntity();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: Momentum
        Upgrade upgrade = new Upgrade(p, this, null);
        double duration = 1.0 + (1.0 * upgrade.getCurrentUpgrade());

        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (20 * duration), 0));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        com.jules.kitpvp.abilities.IronPunch.use(p, upgrade.getCurrentUpgrade());
    }
}
