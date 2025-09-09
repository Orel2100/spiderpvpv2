package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Beam;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
import com.jules.kitpvp.util.Utils;
import org.bukkit.ChatColor;
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
                KitPVP.getInstance().getConfig().getInt("kits.arcanist.cost", 10000),
                new ItemStackCreator(Material.BOOK, "§bArcanist").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 2, 3, 5, 4);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        items.add(getAbilityItem());
        return items;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!p.getItemInHand().hasItemMeta() || !p.getItemInHand().getItemMeta().hasLore() || !p.getItemInHand().getItemMeta().getLore().contains(KitUtils.KIT_ITEM_LORE)) return;
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
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.ARCANIST;
    }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.STONE_SWORD, "§bArcanist Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Arcane Beam", "Your primary attack now shoots a beam of energy.", 5,
                        plugin.getConfig().getIntegerList("kits.arcanist.upgrades.ability.costs"),
                        Arrays.asList(Material.DIAMOND, Material.EMERALD, Material.GOLD_INGOT, Material.IRON_INGOT, Material.COAL))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Arcane Explosion", "Chance to create an explosion on hit.", 3,
                        plugin.getConfig().getIntegerList("kits.arcanist.upgrades.passive1.costs"),
                        Arrays.asList(Material.TNT, Material.TNT, Material.TNT))
        ));

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Wizard Hat", "A pointy hat.", 1, plugin.getConfig().getIntegerList("kits.arcanist.upgrades.kit.costs"), Collections.singletonList(Material.LEATHER_HELMET)),
                new Upgrade("Wizard Robes", "Flowy robes.", 1, plugin.getConfig().getIntegerList("kits.arcanist.upgrades.kit.costs"), Collections.singletonList(Material.LEATHER_CHESTPLATE)),
                new Upgrade("Wizard Pants", "Comfortable pants.", 1, plugin.getConfig().getIntegerList("kits.arcanist.upgrades.kit.costs"), Collections.singletonList(Material.LEATHER_LEGGINGS)),
                new Upgrade("Wizard Boots", "Stylish boots.", 1, plugin.getConfig().getIntegerList("kits.arcanist.upgrades.kit.costs"), Collections.singletonList(Material.LEATHER_BOOTS))
        ));

        return upgrades;
    }
}
