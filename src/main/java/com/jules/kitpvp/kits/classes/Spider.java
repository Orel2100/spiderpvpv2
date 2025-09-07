package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Leap;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Spider extends MegaWallsClass {

    public Spider() {
        super(
                "Spider",
                new String[]{"An agile class with a focus on", "mobility and sudden, high-impact attacks."},
                KitPVP.getInstance().getConfig().getInt("kits.spider.cost", 2000),
                new ItemStackCreator(Material.SPIDER_EYE, "§8Spider").build()
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

        items.add(new ItemStack(Material.STONE_SWORD));
        items.add(new ItemStack(Material.COOKED_BEEF, 2 + (level > 1 ? 1 : 0) + (level > 3 ? 1 : 0) + (level > 4 ? 1 : 0)));

        if (level > 2) items.set(0, new ItemStack(Material.IRON_SWORD));
        if (level > 1) items.get(0).addEnchantment(Enchantment.DURABILITY, 1);
        if (level > 3) items.get(0).addEnchantment(Enchantment.DURABILITY, 2);
        if (level > 4) items.get(0).addEnchantment(Enchantment.DAMAGE_ALL, 1);

        if (level > 4) items.add(new ItemStack(Material.LEATHER_HELMET));

        return items;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        Leap.use(p, 10 + (level - 1) * 5);
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        Player p = (Player) event.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
        if (level > 0 && event.getDamage() >= 4) {
            double multiplier = level < 4 ? 2.0 : 2.5;
            double radius = 2.0 + (level - 1) * 0.5;
            p.getNearbyEntities(radius, radius, radius).forEach(entity -> {
                if (entity instanceof Player && entity != p) {
                    ((Player) entity).damage(event.getDamage() * multiplier, p);
                }
            });
        }
    }

    @Override
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_2).get(0));
        if (level > 0) {
            double chance = 0.1 + (level - 1) * 0.05;
            if (new Random().nextDouble() < chance) {
                int spiderCount = level < 3 ? 1 : (level < 5 ? 2 : 3);
                for (int i = 0; i < spiderCount; i++) {
                    p.getWorld().spawnEntity(p.getLocation(), EntityType.SPIDER);
                }
            }
        }
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) { return new ArrayList<>(); }

    @Override
    public Kit getKit() { return Kit.SPIDER; }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Spider Kit", Arrays.asList(ChatColor.GRAY + "Upgrade your starting kit."), 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.kit.costs"),
                        Arrays.asList(Material.STONE_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.LEATHER_HELMET))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Leap", Arrays.asList(ChatColor.GRAY + "Leap up to " + ChatColor.GREEN + "10" + ChatColor.GRAY + " blocks forward."), 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.ability.costs"),
                        Arrays.asList(Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Drop Shock", Arrays.asList(ChatColor.GRAY + "Deals " + ChatColor.RED + "200%" + ChatColor.GRAY + " of the fall damage you take in a 2-block radius upon landing."), 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.passive1.costs"),
                        Arrays.asList(Material.ANVIL, Material.ANVIL, Material.ANVIL, Material.ANVIL, Material.ANVIL))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Nest Egg", Arrays.asList(ChatColor.GREEN + "10%" + ChatColor.GRAY + " chance to spawn 1 spider upon death."), 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.passive2.costs"),
                        Arrays.asList(Material.SPIDER_EYE, Material.SPIDER_EYE, Material.SPIDER_EYE, Material.SPIDER_EYE, Material.SPIDER_EYE))
        ));

        upgrades.put(UpgradeCategory.GATHERING, Arrays.asList(
                new Upgrade("Spiderman", Arrays.asList(ChatColor.GREEN + "10%" + ChatColor.GRAY + " chance to find an extra string when mining stone."), 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.gathering.costs"),
                        Arrays.asList(Material.STRING, Material.STRING, Material.STRING, Material.STRING, Material.STRING))
        ));

        return upgrades;
    }
}
