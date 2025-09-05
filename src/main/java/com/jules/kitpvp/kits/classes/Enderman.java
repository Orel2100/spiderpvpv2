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
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enderman extends KitClass {

    public Enderman() {
        super(
                "Enderman",
                new String[]{"The Enderman class has", "special teleportation", "powers and endurance."},
                900,
                new ItemStackCreator(Material.ENDER_CHEST, "§bEnderman").build(),
                new Upgrade(UpgradeType.SWORD, 4),
                new Upgrade(UpgradeType.BOOTS, 6),
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
        else if (swordLevel == 3) items.add(new ItemStack(Material.IRON_SWORD));
        else items.add(new ItemStack(Material.DIAMOND_SWORD));

        // Boots
        int bootsLevel = mPlayer.getUpgradeLevel(UpgradeType.BOOTS);
        ItemStackCreator boots = new ItemStackCreator(Material.CHAINMAIL_BOOTS);
        if (bootsLevel >= 2) boots = new ItemStackCreator(Material.IRON_BOOTS);
        if (bootsLevel >= 6) boots = new ItemStackCreator(Material.DIAMOND_BOOTS);

        int featherFalling = 0;
        if (bootsLevel == 1) featherFalling = 1;
        else if (bootsLevel == 3) featherFalling = 2;
        else if (bootsLevel == 4) featherFalling = 3;
        else if (bootsLevel >= 5) featherFalling = 4;

        if (featherFalling > 0) boots.addEnchantment(Enchantment.PROTECTION_FALL, featherFalling);
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
        return Kit.ENDERMAN;
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        double chance = (7 + (3 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY))) / 100.0;
        if (new Random().nextDouble() <= chance) {
            Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> p.setVelocity(new Vector(0, 0, 0)), 1L);
        }
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        com.jules.kitpvp.abilities.Teleport.use(p, mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        p.setLevel(0);
        p.setExp(0);
    }
}
