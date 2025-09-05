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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Squid extends KitClass {

    private ArrayList<String> cd = new ArrayList<>();

    public Squid() {
        super(
                "Squid",
                new String[]{"Splashing around, like a", "Squid, because that what", "Squids do. Splash."},
                450,
                new ItemStackCreator(Material.INK_SAC, "§bSquid").build(),
                new Upgrade(UpgradeType.SWORD, 3),
                new Upgrade(UpgradeType.BOOTS, 5),
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

        // Boots
        int bootsLevel = mPlayer.getUpgradeLevel(UpgradeType.BOOTS);
        ItemStackCreator boots = new ItemStackCreator(Material.CHAINMAIL_BOOTS);
        if (bootsLevel >= 2) boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        if (bootsLevel >= 3) boots = new ItemStackCreator(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        if (bootsLevel >= 4) boots = new ItemStackCreator(Material.DIAMOND_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        if (bootsLevel >= 5) boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        items.add(boots.build());

        // Consumables
        items.add(new ItemStack(Material.COOKED_BEEF, 4));
        int potionLevel = mPlayer.getUpgradeLevel(UpgradeType.POTION);
        if (potionLevel >= 1) items.add(Utils.getPotionHeal(6, 1));
        if (potionLevel >= 2) items.add(Utils.getPotionHeal(6, 2));
        if (potionLevel >= 3) items.add(Utils.getPotionSpeed(1));
        if (potionLevel >= 4) items.add(Utils.getPotionHeal(6, 3));

        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.SQUID;
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.POTION) return;
        Player p = e.getPlayer();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        double duration = 0.75 + (0.25 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        double radius = 2.75 + (0.25 * mPlayer.getUpgradeLevel(UpgradeType.ABILITY));
        for (Entity ent : p.getNearbyEntities(radius, radius, radius)) {
            if (ent instanceof LivingEntity && ent != p) {
                ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) (20 * duration), 0));
            }
        }
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        if (cd.contains(p.getName())) return;
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        double healAmount = 4.0 + (0.5 * (mPlayer.getUpgradeLevel(UpgradeType.ABILITY) - 1));
        double cooldown = 360.0 - (10.0 * (mPlayer.getUpgradeLevel(UpgradeType.ABILITY) - 1));
        cd.add(p.getName());
        p.setHealth(Math.min(p.getHealth() + event.getDamage(), p.getMaxHealth()));
        Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> cd.remove(p.getName()), (long) (20 * cooldown));
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent e) {
        if (!e.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        double percent = 35 + (5 * (mPlayer.getUpgradeLevel(UpgradeType.ABILITY) - 1));
        com.jules.kitpvp.abilities.SquidSplash.use(p, percent);
    }
}
