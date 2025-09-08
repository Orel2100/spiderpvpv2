package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Leap;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Spider extends MegaWallsClass {

    public Spider() {
        super(
                "Spider",
                new String[]{"A nimble and venomous creature."},
                KitPVP.getInstance().getConfig().getInt("kits.spider.cost", 2000),
                new ItemStackCreator(Material.SPIDER_EYE, "§cSpider").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(2, 2, 5, 2, 3);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        items.add(new ItemStack(Material.IRON_SWORD));
        items.add(new ItemStack(Material.COOKED_BEEF, 2));

        switch (level) {
            case 2:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DURABILITY, 1).build());
                break;
            case 3:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                break;
            case 4:
                items.add(new ItemStackCreator(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 1).build());
                break;
            case 5:
                items.add(new ItemStackCreator(Material.DIAMOND_SWORD).build());
                break;
        }
        return items;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        Leap.use(p, level);
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
            int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
            if (level > 0) {
                double chance = 0.1 + (level - 1) * 0.05;
                if (new Random().nextDouble() < chance) {
                    if (event.getEntity() instanceof Player) {
                        ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 3, 0));
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
        return Kit.SPIDER;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Spider Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.kit.costs"),
                        Arrays.asList(Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Leap", "Leap forward a short distance.", 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.ability.costs"),
                        Arrays.asList(Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Venom Strike", "Chance to apply Poison on hit.", 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.passive1.costs"),
                        Arrays.asList(Material.SPIDER_EYE, Material.SPIDER_EYE, Material.SPIDER_EYE, Material.SPIDER_EYE, Material.SPIDER_EYE))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Spiderling", "Chance to spawn a friendly spider on hit.", 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.passive2.costs"),
                        Arrays.asList(Material.SPAWNER, Material.SPAWNER, Material.SPAWNER, Material.SPAWNER, Material.SPAWNER))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        List<String> lore = new ArrayList<>();
        lore.add(upgrade.getDescription());
        lore.add("");
        switch (upgrade.getName()) {
            case "Leap":
                lore.add(ChatColor.GRAY + "Distance: " + ChatColor.GREEN + (5 + level) + " blocks");
                break;
            case "Venom Strike":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (10 + (level - 1) * 5) + "%");
                break;
            case "Spiderling":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (5 + (level - 1) * 2.5) + "%");
                break;
        }
        return lore;
    }
}
