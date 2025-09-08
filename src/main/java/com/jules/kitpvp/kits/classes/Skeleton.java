package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.ExplosiveArrow;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Skeleton extends MegaWallsClass {

    public Skeleton() {
        super(
                "Skeleton",
                new String[]{"A master of the bow."},
                KitPVP.getInstance().getConfig().getInt("kits.skeleton.cost", 1000),
                new ItemStackCreator(Material.BOW, "§fSkeleton").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 2, 4, 3, 2);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        items.add(new ItemStack(Material.WOODEN_SWORD));
        items.add(new ItemStack(Material.BOW));
        items.add(new ItemStack(Material.ARROW, 16));

        switch (level) {
            case 2:
                items.add(new ItemStackCreator(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                break;
            case 3:
                items.add(new ItemStackCreator(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                items.add(new ItemStack(Material.ARROW, 32));
                break;
            case 4:
                items.add(new ItemStackCreator(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                items.add(new ItemStack(Material.ARROW, 32));
                break;
            case 5:
                items.add(new ItemStackCreator(Material.BOW).addEnchantment(Enchantment.ARROW_DAMAGE, 2).addEnchantment(Enchantment.ARROW_KNOCKBACK, 1).build());
                items.add(new ItemStack(Material.ARROW, 64));
                break;
        }
        return items;
    }

    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        ExplosiveArrow.use(p, level);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
            if (level > 0) {
                if (p.getInventory().getItemInHand().getType() == Material.BOW) {
                    event.setDamage(event.getDamage() * (1 + (level * 0.05)));
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
        return Kit.SKELETON;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Skeleton Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.skeleton.upgrades.kit.costs"),
                        Arrays.asList(Material.BOW, Material.BOW, Material.ARROW, Material.ARROW, Material.BOW))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Explosive Arrow", "Your arrows explode on impact.", 5,
                        plugin.getConfig().getIntegerList("kits.skeleton.upgrades.ability.costs"),
                        Arrays.asList(Material.TNT, Material.TNT, Material.TNT, Material.TNT, Material.TNT))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Agile", "Deal more damage with your bow.", 5,
                        plugin.getConfig().getIntegerList("kits.skeleton.upgrades.passive1.costs"),
                        Arrays.asList(Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Salvaging", "Chance to get back an arrow on hit.", 5,
                        plugin.getConfig().getIntegerList("kits.skeleton.upgrades.passive2.costs"),
                        Arrays.asList(Material.ARROW, Material.ARROW, Material.ARROW, Material.ARROW, Material.ARROW))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        List<String> lore = new ArrayList<>();
        lore.add(upgrade.getDescription());
        lore.add("");
        switch (upgrade.getName()) {
            case "Explosive Arrow":
                lore.add(ChatColor.GRAY + "Radius: " + ChatColor.GREEN + (2 + (level - 1) * 0.5) + " blocks");
                break;
            case "Agile":
                lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + (5 + (level - 1) * 5) + "%");
                break;
            case "Salvaging":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (10 + (level - 1) * 10) + "%");
                break;
        }
        return lore;
    }
}
