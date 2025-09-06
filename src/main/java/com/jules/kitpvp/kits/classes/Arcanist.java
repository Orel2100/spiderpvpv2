package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.Beam;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Arcanist extends MegaWallsClass {

    public Arcanist() {
        super(
                "Arcanist",
                new String[]{"A powerful mage."},
                10000,
                new ItemStackCreator(Material.BOOK, "§bArcanist").build()
        );
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Arcane Beam", Arrays.asList("Your primary attack now shoots", "a beam of energy."), 5,
                        Arrays.asList(100, 200, 300, 400, 500),
                        Arrays.asList(Material.DIAMOND, Material.EMERALD, Material.GOLD_INGOT, Material.IRON_INGOT, Material.COAL))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Arcane Explosion", Arrays.asList("Chance to create an explosion", "on hit."), 3,
                        Arrays.asList(1000, 2000, 3000),
                        Arrays.asList(Material.TNT, Material.TNT, Material.TNT))
        ));

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Wizard Hat", Collections.singletonList("A pointy hat."), 1, Collections.singletonList(0), Collections.singletonList(Material.LEATHER_HELMET)),
                new Upgrade("Wizard Robes", Collections.singletonList("Flowy robes."), 1, Collections.singletonList(0), Collections.singletonList(Material.LEATHER_CHESTPLATE)),
                new Upgrade("Wizard Pants", Collections.singletonList("Comfortable pants."), 1, Collections.singletonList(0), Collections.singletonList(Material.LEATHER_LEGGINGS)),
                new Upgrade("Wizard Boots", Collections.singletonList("Stylish boots."), 1, Collections.singletonList(0), Collections.singletonList(Material.LEATHER_BOOTS))
        ));

        return upgrades;
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 2, 3, 5, 4);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());

        for (Upgrade upgrade : getUpgrades().get(UpgradeCategory.KIT)) {
            if (mPlayer.getUpgradeLevel(upgrade) > 0) {
                items.add(new ItemStack(upgrade.getMaterial(1)));
            }
        }
        items.add(new ItemStack(Material.STONE_SWORD));

        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.ARCANIST;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;
        if (p.getLevel() < 100) return;

        p.setExp(0);
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int upgradeLevel = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        double damage = 5.25 + (0.75 * upgradeLevel);
        Beam.shoot(p, damage);
        p.setLevel(0);
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int upgradeLevel = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
        if (upgradeLevel > 0) {
            double chance = (6.5 + (upgradeLevel * 1.5)) / 100;
            double random = new Random().nextDouble();
            if (random <= chance) {
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                for (Entity ent : p.getNearbyEntities(2, 2, 2)) {
                    if (ent instanceof LivingEntity && ent != p) {
                        ((LivingEntity) ent).damage(2);
                    }
                }
            }
        }
    }

    @Override
    public void onKill(Player p, Player killed) {
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
    }
}
