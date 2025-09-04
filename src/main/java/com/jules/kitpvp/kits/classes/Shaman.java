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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Shaman extends KitClass {

    @Override
    public String getName() {
        return "Shaman";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("The Shaman class uses", "spiritual abilities to", "attack and defend.");
    }

    @Override
    public ClassType getType() {
        return ClassType.HERO;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ENCHANTMENT_TABLE);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        if (upgrade >= 7) sword.setType(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        int steakAmount = 2;
        if(upgrade >= 6) steakAmount = 3;
        items.put(1, Utils.getSteaks(steakAmount, getName()));

        if(upgrade >= 2){
            int healLevel = 1;
            if(upgrade >= 8) healLevel = 2;
            items.put(2, Utils.getPotionHeal(8, healLevel));
        }

        if(upgrade >= 3){
            int speedLevel = 1;
            if(upgrade >= 5) speedLevel = 2;
            items.put(3, Utils.getPotionSpeed(speedLevel));
        }

        if(upgrade >= 4){
            ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.PROTECTION_FALL, 1);
            if(upgrade >= 7) enchants.put(Enchantment.PROTECTION_FALL, 2);
            if(upgrade >= 8) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            if(upgrade >= 9) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            items.put(4, ItemStackCreator.createItemStack(boots, 1, ChatColor.AQUA + getName() + " Boots", null, enchants));
        }

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
        return "Tornado";
    }

    @Override
    public int getXPPerHit() {
        return 8;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 5.5 + (0.5 * upgrade);
        return Arrays.asList("§7Summons a destructive", "§7tornado that will send", "§7blocks flying and causing", "§c" + damage + "§7 damage to players in its", "§7path every second.");
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
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || !(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getDamager();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: Spiritual Invigoration
        Upgrade upgrade = new Upgrade(p, this, null);
        double chance = (1.5 + (0.5 * upgrade.getCurrentUpgrade())) / 100.0;
        double duration = 1.5 + (0.5 * upgrade.getCurrentUpgrade());

        if (new Random().nextDouble() <= chance) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (20 * duration), 1));
            if (e.getEntity() instanceof LivingEntity && !e.getEntity().isDead()) {
                ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) (20 * duration), 0));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        MPlayer player = MPlayerManager.getMPlayer(p.getName());
        if (player.getCurrentClass() != this) return;

        // Skill: Heroism
        Upgrade upgrade = new Upgrade(p, this, null);
        double chance = (4.37 + (0.625 * upgrade.getCurrentUpgrade())) / 100.0;
        double duration = 4.37 + (0.625 * upgrade.getCurrentUpgrade());

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

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        Upgrade upgrade = new Upgrade(p, this, UpgradeType.ABILITY);
        com.jules.kitpvp.abilities.Tornado.use(p, upgrade.getCurrentUpgrade());
        p.setLevel(0);
        p.setExp(0);
    }
}
