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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Pirate extends KitClass {

    public static ArrayList<String> cd = new ArrayList<>();

    @Override
    public String getName() {
        return "Pirate";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Use your cunning and wit to", "survive, or just blow", "people up with your", "parrots.");
    }

    @Override
    public ClassType getType() {
        return ClassType.HERO;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.STONE_AXE);
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        if(upgrade >= 4) sword.setType(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        items.put(0, ItemStackCreator.createItem(sword, ChatColor.AQUA + getName() + " Sword", 1, getSwordLore(upgrade)));

        int steakAmount = 2;
        if(upgrade >= 4) steakAmount = 3;
        items.put(1, Utils.getSteaks(steakAmount, getName()));

        if(upgrade >= 2){
            ItemStack helmet = new ItemStack(Material.IRON_HELMET);
            if(upgrade >= 8) helmet.setType(Material.DIAMOND_HELMET);
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.DURABILITY, 1);
            if(upgrade >= 6) enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            items.put(2, ItemStackCreator.createItemStack(helmet, 1, ChatColor.AQUA + getName() + " Helmet", null, enchants));
        }

        if(upgrade >= 3){
            ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
            if(upgrade >= 5) boots.setType(Material.IRON_BOOTS);
            if(upgrade >= 8) boots.setType(Material.DIAMOND_BOOTS);
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantment.DURABILITY, 1);
            items.put(3, ItemStackCreator.createItemStack(boots, 1, ChatColor.AQUA + getName() + " Boots", null, enchants));
        }

        if(upgrade >= 5){
            //items.put(4, Utils.getPotionRegeneration(1, getName()));
        }
        if(upgrade >= 7){
            //items.put(4, Utils.getPotionRegeneration(2, getName()));
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
        return "Cannon Fire";
    }

    @Override
    public int getXPPerHit() {
        return 13;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        double damage = 5.5 + (0.5 * upgrade);
        return Arrays.asList("§7Launch a cannonball that", "§7does §c" + damage + "§7 damage on impact.", "§7The cannonball also penetrates thin barricades!");
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
    public void onCannonballHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof WitherSkull) || !(e.getEntity().getShooter() instanceof Player)) return;

        WitherSkull ws = (WitherSkull) e.getEntity();
        Player shooter = (Player) e.getEntity().getShooter();
        if (MPlayerManager.getMPlayer(shooter.getName()).getCurrentClass() != this) return;

        Upgrade upgrade = new Upgrade(shooter, this, UpgradeType.ABILITY);
        double damage = 5.5 + (0.5 * upgrade.getCurrentUpgrade());

        for (Entity ent : ws.getNearbyEntities(3, 3, 3)) {
            if (ent instanceof LivingEntity && ent != shooter) {
                Utils.realDamage(ent, shooter, damage);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) return;

        final Player p = (Player) e.getEntity();
        if (MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;

        if (p.getHealth() - e.getDamage() <= 10) {
            if (cd.contains(p.getName())) return;

            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 1));
            cd.add(p.getName());

            Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> cd.remove(p.getName()), 20 * 30);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof WitherSkull) {
            e.setCancelled(true);
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

        com.jules.kitpvp.abilities.CannonFire.use(p);
        p.setLevel(0);
        p.setExp(0);
    }
}
