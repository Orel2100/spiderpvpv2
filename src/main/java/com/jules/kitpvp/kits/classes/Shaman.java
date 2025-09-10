package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Tornado;
import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Shaman extends MegaWallsClass {

    public Shaman() {
        super(
                "Shaman",
                new String[]{"A mystical kit that uses the", "power of nature to its advantage."},
                6000,
                new ItemStackCreator(Material.VINE, "§2Shaman").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(2, 3, 3, 4, 4);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        MPlayer mPlayer = MPlayer.getMPlayer(p.getUniqueId());
        int level = mPlayer.getUpgradeLevel(getUpgrades().get(UpgradeCategory.KIT).get(0));

        items.add(new ItemStackCreator(Material.STONE_SWORD, "§2Shaman Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());

        if (level > 0) items.add(new ItemStackCreator(Material.LEATHER_HELMET, "§2Shaman Hood").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        if (level > 1) items.add(new ItemStackCreator(Material.LEATHER_CHESTPLATE, "§2Shaman Robes").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        if (level > 2) items.add(new ItemStackCreator(Material.LEATHER_LEGGINGS, "§2Shaman Leggings").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        if (level > 3) items.add(new ItemStackCreator(Material.LEATHER_BOOTS, "§2Shaman Boots").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        if (level > 4) {
            items.removeIf(item -> item.getType().toString().startsWith("LEATHER_"));
            items.add(new ItemStackCreator(Material.IRON_HELMET, "§2Shaman Hood").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
            items.add(new ItemStackCreator(Material.IRON_CHESTPLATE, "§2Shaman Robes").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
            items.add(new ItemStackCreator(Material.IRON_LEGGINGS, "§2Shaman Leggings").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
            items.add(new ItemStackCreator(Material.IRON_BOOTS, "§2Shaman Boots").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        }

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
        Tornado.use(p, level);
        p.setLevel(0);
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.SHAMAN;
    }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.STONE_SWORD, "§2Shaman Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Shaman Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.shaman.upgrades.kit.costs"),
                        Arrays.asList(Material.VINE, Material.VINE, Material.VINE, Material.VINE, Material.VINE))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Tornado", "Summons a tornado to damage nearby enemies.", 5,
                        plugin.getConfig().getIntegerList("kits.shaman.upgrades.ability.costs"),
                        Arrays.asList(Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER, Material.FEATHER))
        ));

        return upgrades;
    }
}
