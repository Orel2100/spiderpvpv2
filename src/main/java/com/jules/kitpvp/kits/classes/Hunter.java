package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.HomingTask;
import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.kits.HitType;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeType;
import com.jules.kitpvp.player.MPlayerManager;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Hunter extends KitClass {

    public ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Hunter";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("This archery class powers", "up with the thrill of the", "hunt.");
    }

    @Override
    public ClassType getType() {
        return ClassType.HERO;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.BOW);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.WOOD_SWORD);
        if(upgrade >= 2) sword.setType(Material.STONE_SWORD);
        if(upgrade >= 4) sword.setType(Material.IRON_SWORD);
        if(upgrade >= 9) sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.DURABILITY, 2);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        ItemStack bow = new ItemStack(Material.BOW);
        if(upgrade >= 6) bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        bow.addEnchantment(Enchantment.DURABILITY, 2);
        items.put(1, ItemStackCreator.createItem(bow, ChatColor.AQUA + getName() + " Bow", 1, getSwordLore(upgrade)));

        int steakAmount = 2;
        if(upgrade >= 4) steakAmount = 3;
        items.put(2, Utils.getSteaks(steakAmount, getName()));

        int arrowAmount = 12;
        if(upgrade >= 2) arrowAmount = 16;
        if(upgrade >= 3) arrowAmount = 30;
        if(upgrade >= 4) arrowAmount = 24;
        if(upgrade >= 5) arrowAmount = 32;
        if(upgrade >= 6) arrowAmount = 40;
        if(upgrade >= 7) arrowAmount = 48;
        if(upgrade >= 8) arrowAmount = 56;
        if(upgrade >= 9) arrowAmount = 64;
        items.put(3, new ItemStack(Material.ARROW, arrowAmount));

        if(upgrade >= 3) {
            ItemStack helmet = new ItemStack(Material.IRON_HELMET);
            if(upgrade >= 7) helmet.setType(Material.DIAMOND_HELMET);
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.DURABILITY, 2);
            if(upgrade >= 6) {
                enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                enchants.put(Enchantment.PROTECTION_PROJECTILE, 1);
            }
            if(upgrade >= 8) {
                enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                enchants.put(Enchantment.PROTECTION_PROJECTILE, 2);
            }
            items.put(4, ItemStackCreator.createItemStack(helmet, 1, ChatColor.AQUA + getName() + " Helmet", null, enchants));
        }

        if(upgrade >= 4){
            ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
            if(upgrade >= 5) boots.setType(Material.IRON_BOOTS);
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.DURABILITY, 2);
            if(upgrade >= 7) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            if(upgrade >= 8) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            items.put(5, ItemStackCreator.createItemStack(boots, 1, ChatColor.AQUA + getName() + " Boots", null, enchants));
        }

        if(upgrade >= 9){
            items.put(6, new ItemStack(Material.GOLDEN_APPLE, 2));
        }

        return items;
    }

    @Override
    public int getPrice() {
        return 20000;
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
        return "Eagle's Eye";
    }

    @Override
    public int getXPPerHit() {
        return 2;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        int duration = 6 + upgrade;
        return Arrays.asList("§7Upon activation, you will", "§7have homing arrows for §c" + duration, "§7seconds.");
    }


    @Override
    public HitType getHitType() {
        return HitType.TIMER;
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {
        if (e.getForce() != 1.0) return;
        if (!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
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

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (!e.getAction().name().contains("LEFT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() != Material.BOW) return;
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (p.getLevel() < 100) return;

        Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        int duration = 6 + upgrade.getCurrentUpgrade();

        p.setLevel(0);
        p.setExp(0);
        cd.add(p.getName());

        Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> {
            cd.remove(p.getName());
            p.sendMessage(ChatColor.GREEN + "Your Eagle's Eye wore off.");
        }, 20 * duration);
    }
}
