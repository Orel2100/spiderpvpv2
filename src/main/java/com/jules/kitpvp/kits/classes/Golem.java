package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.IronPunch;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
import com.jules.kitpvp.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class Golem extends MegaWallsClass {

    private final Map<UUID, Long> ironHeartCooldowns = new HashMap<>();

    @Override
    public void onKill(Player p, Player killed) {
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel("Iron Heart");

        if (level > 0) {
            long currentTime = System.currentTimeMillis();
            long lastUsed = ironHeartCooldowns.getOrDefault(p.getUniqueId(), 0L);

            if (currentTime - lastUsed > 45 * 1000) {
                double duration = 2.0 + (level - 1) * 1.25;
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, (int) (duration * 20), 1));
                ironHeartCooldowns.put(p.getUniqueId(), currentTime);
            }
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof org.bukkit.entity.Arrow) {
            if (event.getEntity() instanceof Player) {
                Player p = (Player) event.getEntity();
                MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
                int level = mPlayer.getUpgradeLevel("Iron Constitution");

                if (level > 0) {
                    double duration = 2.0 + (level - 1) * 1.0;
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (duration * 20), 0));
                }
            }
        }
    }

    public Golem() {
        super(
                "Golem",
                new String[] {
                        "A powerful tank that can",
                        "absorb damage and deal",
                        "massive area damage."
                },
                KitPVP.getInstance().getConfig().getInt("kits.golem.cost", 3000),
                new ItemStackCreator(Material.IRON_BLOCK, "§bGolem").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(2, 5, 1, 2, 1);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        switch (level) {
            case 1:
                items.add(new ItemStackCreator(Material.IRON_SWORD, "§bGolem Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.COOKED_BEEF, "§bGolem Steak").setAmount(2).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                break;
            case 2:
                items.add(new ItemStackCreator(Material.IRON_SWORD, "§bGolem Sword").addEnchantment(Enchantment.DURABILITY, 1).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.COOKED_BEEF, "§bGolem Steak").setAmount(2).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                break;
            case 3:
                items.add(new ItemStackCreator(Material.IRON_SWORD, "§bGolem Sword").addEnchantment(Enchantment.DAMAGE_ALL, 1).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.COOKED_BEEF, "§bGolem Steak").setAmount(3).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                break;
            case 4:
                items.add(new ItemStackCreator(Material.IRON_SWORD, "§bGolem Sword").addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 1).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.COOKED_BEEF, "§bGolem Steak").setAmount(3).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                break;
            case 5:
                items.add(new ItemStackCreator(Material.IRON_SWORD, "§bGolem Sword").addEnchantment(Enchantment.DAMAGE_ALL, 1).addEnchantment(Enchantment.DURABILITY, 2).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.COOKED_BEEF, "§bGolem Steak").setAmount(4).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Utils.getPotionRegen(3, 2)).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                break;
        }
        return items;
    }

    @Override
    public void onInteract(Player p, PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT")) return;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) return;
        if (!p.getItemInHand().hasItemMeta() || !p.getItemInHand().getItemMeta().hasLore() || !p.getItemInHand().getItemMeta().getLore().contains(KitUtils.KIT_ITEM_LORE)) return;
        if (!Utils.isUsingSword(p.getItemInHand())) return;

        if (p.getLevel() < KitClass.ABILITY_XP_COST) {
            p.sendMessage(ChatColor.RED + "You don't have enough energy to use this ability!");
            return;
        }

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        IronPunch.use(p, level);
    }


    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.GOLEM;
    }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.IRON_SWORD, "§bGolem Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Golem Kit", "Upgrade your starting kit.", 9,
                        plugin.getConfig().getIntegerList("kits.golem.upgrades.kit.costs"),
                        Arrays.asList(Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.IRON_SWORD))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Iron Punch", "Casts a hexagon causing damage in a 4.5 block radius.", 9,
                        plugin.getConfig().getIntegerList("kits.golem.upgrades.ability.costs"),
                        Arrays.asList(Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Iron Heart", "After killing a player you get Absorption 2 for X seconds (cooldown of 45s).", 9,
                        plugin.getConfig().getIntegerList("kits.golem.upgrades.passive1.costs"),
                        Arrays.asList(Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT, Material.IRON_INGOT))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Iron Constitution", "When hit by an arrow you gain Resistance I for X seconds.", 9,
                        plugin.getConfig().getIntegerList("kits.golem.upgrades.passive2.costs"),
                        Arrays.asList(Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE, Material.IRON_CHESTPLATE))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        List<String> lore = new ArrayList<>();
        lore.add(upgrade.getDescription());
        lore.add("");
        switch (upgrade.getName()) {
            case "Iron Punch":
                double damage = 1.0 + (level - 1) * 0.5;
                lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + damage);
                break;
            case "Iron Heart":
                double duration = 2.0 + (level - 1) * 1.25;
                lore.add(ChatColor.GRAY + "Duration: " + ChatColor.AQUA + duration + "s");
                break;
            case "Iron Constitution":
                double resistanceDuration = 2.0 + (level - 1) * 1.0;
                lore.add(ChatColor.GRAY + "Duration: " + ChatColor.AQUA + resistanceDuration + "s");
                break;
        }
        return lore;
    }
}
