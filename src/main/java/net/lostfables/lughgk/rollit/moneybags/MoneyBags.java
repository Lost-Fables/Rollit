package net.lostfables.lughgk.rollit.moneybags;

import co.lotc.core.bukkit.util.ItemUtil;
import net.lostfables.lughgk.rollit.Rollit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MoneyBags implements Listener {

    private final Rollit plugin = Rollit.getPlugin(Rollit.class);

    public MoneyBags() {
        moneyBagRecipe();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && event.getPlayer().getInventory().getItemInMainHand() != null) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

            if(item.hasItemMeta() && ItemUtil.hasCustomTag(item.getItemMeta(), Rollit.MONEYBAG_TYTHAN_TAG)) {
                Player p = event.getPlayer();
                event.setCancelled(true);
                Inventory moneybag = plugin.getServer().createInventory(null,9,Rollit.MONEYBAG_TITLE);

                for(int x = 0; x < moneybag.getSize(); x++) {
                    if(!ItemUtil.getCustomTag(item,"inventoryslot-" + (x+1)).equals("!")) {
                        moneybag.setItem(x, ItemUtil.getItemFromYaml(ItemUtil.getCustomTag(item,"inventoryslot-" + (x+1))));
                    }
                }

                p.openInventory(moneybag);
                return;
            }
        }
    }

    @EventHandler
    public void onMoneyBagClose(InventoryCloseEvent event) {
        if(event.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && ItemUtil.hasCustomTag(event.getPlayer().getInventory().getItemInMainHand().getItemMeta(), Rollit.MONEYBAG_TYTHAN_TAG)) {
            ItemStack[] items = event.getInventory().getContents();
            for(int x = 0; x < items.length; x++) {
                try {
                    ItemUtil.setCustomTag(event.getPlayer().getInventory().getItemInMainHand(), "inventoryslot-" + (x + 1), ItemUtil.getItemYaml(items[x]));
                } catch(NullPointerException e) {
                    ItemUtil.setCustomTag(event.getPlayer().getInventory().getItemInMainHand(), "inventoryslot-" + (x+1), "!");
                }
            }
        }

    }

    @EventHandler
    public void moneyBagInteract(InventoryClickEvent event) {
        if(ItemUtil.hasCustomTag(event.getCurrentItem(), Rollit.MONEYBAG_TYTHAN_TAG) && event.getView().getTitle().equals(Rollit.MONEYBAG_TITLE)) {
            event.setCancelled(true);
        }
    }

    public void moneyBagRecipe() {
        ItemStack moneybag = ItemUtil.getSkullFromTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNiM2FjZGMxMWNhNzQ3YmY3MTBlNTlmNGM4ZTliM2Q5NDlmZGQzNjRjNjg2OTgzMWNhODc4ZjA3NjNkMTc4NyJ9fX0=");
        ItemMeta im = moneybag.getItemMeta();
        List<String> stringList = new ArrayList<>();
        stringList.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click with this item in hand");
        stringList.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "to open and view the contents of it.");


        im.setDisplayName(Rollit.MONEYBAG_TITLE);
        im.setLore(stringList);

        moneybag.setItemMeta(im);
        ItemUtil.setCustomTag(moneybag, Rollit.MONEYBAG_TYTHAN_TAG, "!");
        ItemUtil.setCustomTag(moneybag,"inventoryslot-1","!");
        ItemUtil.setCustomTag(moneybag,"inventoryslot-2","!");
        ItemUtil.setCustomTag(moneybag,"inventoryslot-3","!");
        ItemUtil.setCustomTag(moneybag,"inventoryslot-4","!");
        ItemUtil.setCustomTag(moneybag,"inventoryslot-5","!");
        ItemUtil.setCustomTag(moneybag,"inventoryslot-6","!");
        ItemUtil.setCustomTag(moneybag,"inventoryslot-7","!");
        ItemUtil.setCustomTag(moneybag,"inventoryslot-8","!");
        ItemUtil.setCustomTag(moneybag,"inventoryslot-9","!");


        NamespacedKey nsKey = new NamespacedKey(plugin, "moneybag");
        ShapedRecipe r = new ShapedRecipe(nsKey, moneybag);

        r.shape(" L ", "LSL", " L ");
        r.setIngredient('L', Material.RABBIT_HIDE);
        r.setIngredient('S', Material.STRING);

        plugin.getServer().addRecipe(r);

    }



}
