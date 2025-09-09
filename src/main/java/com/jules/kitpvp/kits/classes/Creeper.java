package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.abilities.Detonate;
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

public class Creeper extends MegaWallsClass {

    public Creeper() {
        super(
                "Creeper",
                new String[]{"An explosive kit that can", "detonate itself to deal damage."},
                3000,
                new ItemStackCreator(Material.TNT, "§2Creeper").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(1, 1, 3, 2, 2);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(getAbilityItem());
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
        Detonate.use(p, level);
        p.setLevel(0);
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.CREEPER;
    }

    @Override
    public ItemStack getAbilityItem() {
        return new ItemStackCreator(Material.STONE_SWORD, "§2Creeper Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build();
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        Map<UpgradeCategory, List<Upgrade>> upgrades = new LinkedHashMap<>();
        KitPVP plugin = KitPVP.getInstance();

        upgrades.put(UpgradeCategory.KIT, Arrays.asList(
                new Upgrade("Creeper Kit", "Upgrade your starting kit.", 5,
                        plugin.getConfig().getIntegerList("kits.creeper.upgrades.kit.costs"),
                        Arrays.asList(Material.TNT, Material.TNT, Material.TNT, Material.TNT, Material.TNT))
        ));

        upgrades.put(UpgradeCategory.ABILITY, Arrays.asList(
                new Upgrade("Detonate", "Explode, dealing damage to nearby enemies.", 5,
                        plugin.getConfig().getIntegerList("kits.creeper.upgrades.ability.costs"),
                        Arrays.asList(Material.LEGACY_SULPHUR, Material.LEGACY_SULPHUR, Material.LEGACY_SULPHUR, Material.LEGACY_SULPHUR, Material.LEGACY_SULPHUR))
        ));

        return upgrades;
    }
}
