package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Leap;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Spider extends MegaWallsClass {

    private static final int LANDING_RADIUS = 3;
    private static final Random random = new Random();

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

        items.add(getAbilityItem());

        if (level > 4) items.add(new ItemStackCreator(Material.LEATHER_HELMET, "§8Spider Helmet").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());

        return items;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!p.getItemInHand().hasItemMeta() || !p.getItemInHand().getItemMeta().hasLore() || !p.getItemInHand().getItemMeta().getLore().contains(KitUtils.KIT_ITEM_LORE)) return;
        if(p.getLevel() < KitClass.ABILITY_XP_COST) {
            return;
        }
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        Leap.use(p, 10 + (level - 1) * 5);
        p.setLevel(0);
    }

    @Override
    public void onLand(Player p, float fallDistance) {
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
        double multiplier = level < 4 ? 2.0 : 2.5;

        // Damage entities
        p.getNearbyEntities(LANDING_RADIUS, LANDING_RADIUS, LANDING_RADIUS).forEach(entity -> {
            if (entity instanceof Player && entity != p) {
                ((Player) entity).damage(fallDistance * multiplier, p);
            }
        });

        // Spawn visual-only "falling" cobwebs
        Location center = p.getLocation();
        for (int x = -1; x <= 1; x++) { // 3x3 area
            for (int z = -1; z <= 1; z++) {
                if (random.nextFloat() > 0.7) continue; // 70% chance to spawn a cobweb in a spot

                Location spawnLoc = center.clone().add(x, 2, z);

                FallingBlock fallingCobweb = spawnLoc.getWorld().spawnFallingBlock(spawnLoc, Material.COBWEB.createBlockData());
                fallingCobweb.setDropItem(false);
                fallingCobweb.setHurtEntities(false);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        fallingCobweb.remove();
                    }
                }.runTaskLater(KitPVP.getInstance(), 15L); // Remove after 0.75 seconds

                Location particleLoc = center.clone().add(x, 0, z);
                particleLoc.getWorld().spawnParticle(Particle.SQUID_INK, particleLoc, 10, 0.5, 0.5, 0.5, 0);
            }
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
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.STONE_SWORD, "§8Spider Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Spider Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.kit.costs"),
                        Arrays.asList(Material.STONE_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.LEATHER_HELMET))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Leap", "Leap up to " + ChatColor.GREEN + "10" + ChatColor.GRAY + " blocks forward.", 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.ability.costs"),
                        Arrays.asList(Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Drop Shock", "Deals " + ChatColor.RED + "200%" + ChatColor.GRAY + " of the fall damage you take in a 2-block radius upon landing.", 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.passive1.costs"),
                        Arrays.asList(Material.ANVIL, Material.ANVIL, Material.ANVIL, Material.ANVIL, Material.ANVIL))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Nest Egg", ChatColor.GREEN + "10%" + ChatColor.GRAY + " chance to spawn 1 spider upon death.", 5,
                        plugin.getConfig().getIntegerList("kits.spider.upgrades.passive2.costs"),
                        Arrays.asList(Material.SPIDER_EYE, Material.SPIDER_EYE, Material.SPIDER_EYE, Material.SPIDER_EYE, Material.SPIDER_EYE))
        ));

        return upgrades;
    }
}
