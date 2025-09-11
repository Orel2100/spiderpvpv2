package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Wrath;
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

import java.util.*;

public class Herobrine extends MegaWallsClass {

    public Herobrine() {
        super(
                "Herobrine",
                new String[]{"A mysterious and powerful kit."},
                KitPVP.getInstance().getConfig().getInt("kits.herobrine.cost", 10000),
                new ItemStackCreator(Material.NETHER_STAR, "§dHerobrine").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(4, 3, 3, 4, 5);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        switch (level) {
            case 1:
                items.add(new ItemStackCreator(Material.STONE_SWORD, "§dHerobrine Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.COOKED_BEEF, "§dHerobrine Steak").setAmount(2).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                break;
            case 2:
                items.add(new ItemStackCreator(Material.STONE_SWORD, "§dHerobrine Sword").addEnchantment(Enchantment.DURABILITY, 1).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.COOKED_BEEF, "§dHerobrine Steak").setAmount(2).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                break;
            case 3:
                items.add(new ItemStackCreator(Material.IRON_SWORD, "§dHerobrine Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.COOKED_BEEF, "§dHerobrine Steak").setAmount(3).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Utils.getPotionHeal(1, 1)).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                break;
            case 4:
                items.add(new ItemStackCreator(Material.IRON_SWORD, "§dHerobrine Sword").addEnchantment(Enchantment.DURABILITY, 2).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.COOKED_BEEF, "§dHerobrine Steak").setAmount(3).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Utils.getPotionHeal(1, 1)).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Utils.getPotionSpeed(2)).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                break;
            case 5:
                items.add(new ItemStackCreator(Material.DIAMOND_SWORD, "§dHerobrine Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.COOKED_BEEF, "§dHerobrine Steak").setAmount(3).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Utils.getPotionHeal(1, 2)).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Utils.getPotionSpeed(2)).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
                items.add(new ItemStackCreator(Material.IRON_HELMET, "§dHerobrine Helmet").addEnchantment(Enchantment.WATER_WORKER, 1).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
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
            return;
        }

        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.ABILITY).get(0));
        Wrath.use(p, level);
        p.setLevel(0);
    }

    @Override
    public void onKill(Player p, Player killed) {
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_1).get(0));
        if (level > 0) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * (int) (2 + (level * 0.5)), 0));
        }
    }

    @Override
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getDamager();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.PASSIVE_2).get(0));

        if (level > 0) {
            double chance = (0.27 + (level - 1) * 0.06);
            if (new Random().nextDouble() < chance) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 2));
            }
        }
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.HEROBRINE;
    }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.STONE_SWORD, "§dHerobrine Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Herobrine Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.herobrine.upgrades.kit.costs"),
                        Arrays.asList(Material.STONE_SWORD, Material.IRON_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_HELMET))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Wrath", "Strikes nearby enemies with lightning.", 5,
                        plugin.getConfig().getIntegerList("kits.herobrine.upgrades.ability.costs"),
                        Arrays.asList(Material.NETHER_STAR, Material.NETHER_STAR, Material.NETHER_STAR, Material.NETHER_STAR, Material.NETHER_STAR))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_1, Arrays.asList(
                new Upgrade("Power", "Gain a Strength I effect upon killing an enemy.", 5,
                        plugin.getConfig().getIntegerList("kits.herobrine.upgrades.passive1.costs"),
                        Arrays.asList(Material.DIAMOND_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_SWORD, Material.DIAMOND_SWORD))
        ));

        upgrades.put(UpgradeCategory.PASSIVE_2, Arrays.asList(
                new Upgrade("Flurry", "Chance to gain Speed II on hit.", 5,
                        plugin.getConfig().getIntegerList("kits.herobrine.upgrades.passive2.costs"),
                        Arrays.asList(Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER))
        ));

        return upgrades;
    }

    @Override
    public List<String> getLoreForUpgrade(Upgrade upgrade, int level) {
        List<String> lore = new ArrayList<>();
        lore.add(upgrade.getDescription());
        lore.add("");
        switch (upgrade.getName()) {
            case "Wrath":
                lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + (1.0 + (level - 1) * 0.5));
                break;
            case "Power":
                lore.add(ChatColor.GRAY + "Duration: " + ChatColor.GREEN + (2.0 + (level - 1) * 0.5) + "s");
                break;
            case "Flurry":
                lore.add(ChatColor.GRAY + "Chance: " + ChatColor.GREEN + (27 + (level - 1) * 6) + "%");
                break;
        }
        return lore;
    }

    @Override
    public String getDifficulty() {
        return "§c●●●●";
    }

    @Override
    public String getPlayStyle() {
        return "§cAssassin";
    }

    @Override
    public String getSkillName() {
        return "§6Wrath";
    }

    @Override
    public String getSkillDescription() {
        return "Costs 100 Energy\\nUnleash the wrath of Herobrine striking all nearby enemies for 4.5 damage.";
    }
}
