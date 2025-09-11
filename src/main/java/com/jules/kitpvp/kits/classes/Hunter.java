package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.HomingTask;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Hunter extends MegaWallsClass {

    public Hunter() {
        super(
                "Hunter",
                new String[]{"A ranged kit that excels at", "keeping enemies at a distance."},
                5000,
                new ItemStackCreator(Material.BOW, "§aHunter").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 2, 4, 3, 2);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStackCreator(Material.STONE_SWORD, "§aHunter Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.BOW, "§aHunter Bow").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.ARROW, "§aHunter Arrow").setAmount(32).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.LEATHER_HELMET, "§aHunter Helmet").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.LEATHER_CHESTPLATE, "§aHunter Chestplate").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.LEATHER_LEGGINGS, "§aHunter Leggings").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.LEATHER_BOOTS, "§aHunter Boots").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        return items;
    }

    @Override
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        if (p.getLevel() < KitClass.ABILITY_XP_COST) {
            return;
        }

        LivingEntity target = null;
        double minAngle = 1.0;

        for (Entity entity : p.getNearbyEntities(20, 20, 20)) {
            if (entity instanceof LivingEntity && entity != p) {
                if (p.hasLineOfSight(entity)) {
                    double angle = p.getLocation().getDirection().angle(entity.getLocation().toVector().subtract(p.getLocation().toVector()));
                    if (angle < minAngle) {
                        minAngle = angle;
                        target = (LivingEntity) entity;
                    }
                }
            }
        }

        if (target != null) {
            new HomingTask((Arrow) event.getProjectile(), target, KitPVP.getInstance());
            p.setLevel(0);
        }
    }


    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.HUNTER;
    }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.BOW, "§aHunter Bow").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Hunter Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.hunter.upgrades.kit.costs"),
                        Arrays.asList(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.BOW))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Homing Arrow", "Shoots an arrow that homes in on the nearest target.", 5,
                        plugin.getConfig().getIntegerList("kits.hunter.upgrades.ability.costs"),
                        Arrays.asList(Material.ARROW, Material.ARROW, Material.ARROW, Material.ARROW, Material.ARROW))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        return new ArrayList<>();
    }

    @Override
    public String getDifficulty() {
        return "";
    }

    @Override
    public String getPlayStyle() {
        return "";
    }

    @Override
    public String getSkillName() {
        return "";
    }

    @Override
    public String getSkillDescription() {
        return "";
    }
}
