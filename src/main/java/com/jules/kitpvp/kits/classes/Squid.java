package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.SquidSplash;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Squid extends MegaWallsClass {

    private final Set<UUID> rejuvenateList = new HashSet<>();

    public Squid() {
        super(
                "Squid",
                new String[]{
                        "You pull opponents inwards, dealing 3.5 damage to all enemies within a 5 block radius.",
                        "You are healed by &a70% &rof the total damage dealt.",
                        "You can heal for a maximum of &a7 HP&r."
                },
                1500,
                new ItemStackCreator(Material.INK_SAC, "§1Squid").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(1, 2, 4, 2, 2);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.STONE_SWORD));
        items.add(new ItemStack(Material.DIAMOND_BOOTS));
        return items;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!p.getItemInHand().hasItemMeta() || !p.getItemInHand().getItemMeta().hasLore() || !p.getItemInHand().getItemMeta().getLore().contains(KitUtils.KIT_ITEM_LORE)) return;
        if (p.getLevel() < KitClass.ABILITY_XP_COST) {
            return;
        }

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        SquidSplash.use(p, level);
        p.setLevel(0);
    }

    @Override
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.POTION) {
            Location loc = player.getLocation();

            player.getWorld().playSound(loc, Sound.ENTITY_GHAST_SHOOT, 0.75f, 2);
            player.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 0.5, 0.5, 0.5, 0.1);

            for (Player victim : inRange(player, 5)) {
                victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0));
                victim.getWorld().spawnParticle(Particle.SMOKE_LARGE, victim.getEyeLocation(), 50, 0.5, 0.5, 0.5, 0.1);
            }
        }
    }

    @Override
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        double finalHealth = player.getHealth() - event.getFinalDamage();
        rejuvenate(player, finalHealth);
    }

    private void rejuvenate(Player player, double health) {
        if (health >= 21) return;
        if (rejuvenateList.contains(player.getUniqueId())) return;

        Location loc = player.getEyeLocation();

        player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_SPLASH, 1, 1);

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30, 4)); // 1.5s * 20 ticks = 30 ticks. Regen V is amplifier 4
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 0)); // Resistance I is amplifier 0

        player.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, loc, 1, 0.5, 0.5, 0.5, 0.15);

        rejuvenateList.add(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> rejuvenateList.remove(player.getUniqueId()), 40 * 20);
    }

    @Override
    public void onKill(Player p, Player killed) {
        givePotions(p);
    }

    private void givePotions(Player player) {
        ItemStack potions = new ItemStack(Material.POTION, 2);
        PotionMeta meta = (PotionMeta) potions.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Squid's Absorption");
        meta.addCustomEffect(new PotionEffect(PotionEffectType.ABSORPTION, 60 * 20, 1), true); // Absorption II is amplifier 1
        potions.setItemMeta(meta);
        player.getInventory().addItem(potions);
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        List<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0));
        return effects;
    }

    @Override
    public Kit getKit() {
        return Kit.SQUID;
    }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.STONE_SWORD, "§1Squid Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Squid Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.squid.upgrades.kit.costs"),
                        Arrays.asList(Material.INK_SAC, Material.INK_SAC, Material.INK_SAC, Material.INK_SAC, Material.INK_SAC))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Squid Splash", "Pull enemies in and heal.", 5,
                        plugin.getConfig().getIntegerList("kits.squid.upgrades.ability.costs"),
                        Arrays.asList(Material.INK_SAC, Material.INK_SAC, Material.INK_SAC, Material.INK_SAC, Material.INK_SAC))
        ));

        return upgrades;
    }

    private List<Player> inRange(Player center, double radius) {
        List<Player> players = new ArrayList<>();
        for (Entity entity : center.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Player && entity != center) {
                players.add((Player) entity);
            }
        }
        return players;
    }
}
