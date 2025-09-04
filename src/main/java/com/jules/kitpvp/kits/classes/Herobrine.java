package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.kits.HitType;
import com.jules.kitpvp.kits.KitClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Herobrine extends KitClass {
    @Override
    public String getName() {
        return "Herobrine";
    }

    @EventHandler
    public void kill(PlayerDeathEvent e) {
        if(e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {
            Player p = (Player)e.getEntity().getKiller();
            com.jules.kitpvp.player.MPlayer player = com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName());
            if(player.getCurrentClass() != this) return;
            double damage = 1.5;
            for(int i = 0; i < 9; i++) {
                damage += 0.5;
            }
            int time = (int) (20 * damage);
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,time,0));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction().name().contains("RIGHT") == false) return;
        if(p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if(com.jules.kitpvp.player.MPlayerManager.getMPlayer(p.getName()).getCurrentClass() != this) return;
        if(!com.jules.kitpvp.util.Utils.isUsingSword(p.getItemInHand())) return;
        if(p.getLevel() < 100) return;
        com.jules.kitpvp.kits.Upgrade upgrade = new com.jules.kitpvp.kits.Upgrade(p, this, com.jules.kitpvp.kits.UpgradeType.ABILITY);
        com.jules.kitpvp.abilities.Wrath.use(p, upgrade.getCurrentUpgrade());
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("A powerful being that gains","strength from kills.");
    }

    @Override
    public ClassType getType() {
        return ClassType.HERO;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.PLAYER_HEAD);
    }

    @Override
    public int getPrice() {
        return 15000;
    }

    @Override
    public HashMap<Integer, ItemStack> getStartingItems(int upgrade) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        items.put(0, sword);
        return items;
    }

    @Override
    public int getXPPerHit() {
        return 25;
    }

    @Override
    public int getUpgradePrice(int level) {
        return 200 * level;
    }

    @Override
    public String getAbilityName() {
        return "Wrath";
    }

    @Override
    public HitType getHitType() {
        return HitType.MELEE;
    }

    @Override
    public List<String> getAbilityDescription(int upgrade) {
        return Arrays.asList("§7Gain Strength II for 5 seconds","§7upon killing an enemy.");
    }

    @Override
    public String getAbilityReadyName() {
        return getAbilityName();
    }
}
