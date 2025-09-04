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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Enderman extends KitClass {

    @Override
    public String getName() {
        return "Enderman";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Enderman class has", "special teleportation", "powers and endurance.");
    }

    @Override
    public ClassType getType() {
        return ClassType.NORMAL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ENDER_CHEST);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        if (upgrade >= 5) sword.setType(Material.STONE_SWORD);
        if (upgrade >= 8) sword.setType(Material.IRON_SWORD);
        if (upgrade >= 9) sword.setType(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        int steakAmount = 1;
        if (upgrade >= 2) steakAmount = 2;
        if (upgrade >= 4) steakAmount = 3;
        items.put(1, Utils.getSteaks(steakAmount, getName()));

        if (upgrade >= 4) {
            int potionHealLevel = 1;
            if (upgrade >= 9) potionHealLevel = 2;
            items.put(2, Utils.getPotionHeal(8, potionHealLevel));
        }

        if (upgrade >= 5) {
            int potionSpeedLevel = 1;
            if (upgrade >= 8) potionSpeedLevel = 2;
            items.put(3, Utils.getPotionSpeed(potionSpeedLevel));
        }

        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        if (upgrade >= 2) boots.setType(Material.IRON_BOOTS);
        if (upgrade >= 9) boots.setType(Material.DIAMOND_BOOTS);

        HashMap<Enchantment, Integer> enchants = new HashMap<>();
        int fallProt = 1;
        if (upgrade >= 3) fallProt = 2;
        if (upgrade >= 6) fallProt = 3;
        if (upgrade >= 7) fallProt = 4;
        enchants.put(Enchantment.PROTECTION_FALL, fallProt);
        items.put(4, ItemStackCreator.createItemStack(boots, 1, ChatColor.AQUA + getName() + " Boots", null, enchants));

        return items;
    }

    @Override
    public int getPrice() {
        return 900;
    }

    @Override
    public int getUpgradePrice(int level) {
        switch (level) {
            case 1: return 0;
            case 2: return 50;
            case 3: return 125;
            case 4: return 300;
            case 5: return 600;
            case 6: return 3500;
            case 7: return 6500;
            case 8: return 8500;
            case 9: return 14000;
            default: return 14000;
        }
    }

    @Override
    public String getAbilityName() {
        return "Enderman Teleport";
    }

    @Override
    public int getXPPerHit() {
        return 20;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double distance = 10.0 + (2.0 * (upgrade -1));
        if(upgrade > 1) distance--;
        int speed = 1;
        if(upgrade >= 4) speed = 2;
        if(upgrade >= 9) speed = 3;

        return Arrays.asList("§7Teleports you §c" + distance, "§7blocks towards the nearest", "§7player and gain Speed", "§c" + speed + "§7 for 5 seconds.");
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public String getAbilityReadyName() {
        return "Teleport";
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        final Player p = (Player) e.getEntity();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: Soul Destroyer
        double chance = (7 + (3 * new Upgrade(p, this, null).getCurrentUpgrade())) / 100.0;
        if (new Random().nextDouble() <= chance) {
            Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> p.setVelocity(new Vector(0, 0, 0)), 1L);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        com.jules.kitpvp.abilities.Teleport.use(p, upgrade.getCurrentUpgrade());
        p.setLevel(0);
        p.setExp(0);
    }
}
