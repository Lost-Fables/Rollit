package net.lostfables.lughgk.rollit.inventoryitems;

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
import java.util.UUID;

public class InventoryItemHandler implements Listener {

    private final Rollit plugin = Rollit.getPlugin(Rollit.class);

    public InventoryItemHandler() {
        moneyBagRecipe();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryItemOpenEvent(PlayerInteractEvent event) {
        if((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && event.getPlayer().getInventory().getItemInMainHand() != null) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

            if(item.hasItemMeta() && ItemUtil.hasCustomTag(item, Rollit.INVENTORY_ITEM_TAG) && !ItemUtil.getCustomTag(item, Rollit.INVENTORY_ITEM_UUID_TAG).equals("!")) {
                Player p = event.getPlayer();
                event.setCancelled(true);
                InventoryItem invItem = new InventoryItem(p.getInventory().getItemInMainHand());
                Inventory moneybag = plugin.getServer().createInventory(invItem, invItem.getSize(), invItem.getName());

                for(int x = 0; x < moneybag.getSize(); x++) {
                    if(!ItemUtil.getCustomTag(item,"inventoryslot-" + (x+1)).equals("!")) {
                        moneybag.setItem(x, ItemUtil.getItemFromYaml(ItemUtil.getCustomTag(item,"inventoryslot-" + (x+1))));
                    }
                }

                p.openInventory(moneybag);
                return;

            } else if(item.hasItemMeta() && ItemUtil.hasCustomTag(item, Rollit.INVENTORY_ITEM_TAG) && ItemUtil.getCustomTag(item, Rollit.INVENTORY_ITEM_UUID_TAG).equals("!")) {
                InventoryItem.initInventoryItem(item);
                ItemMeta im = item.getItemMeta();
                if(ItemUtil.getCustomTag(item, Rollit.INVENTORY_ITEM_TYPE_TAG).equals(InventoryItemType.MONEYBAG.tag)) {
                    event.setCancelled(true);
                    List<String> stringList = new ArrayList<>();
                    stringList.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click with this item in hand");
                    stringList.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "to view its contents.");
                    im.setLore(stringList);
                    item.setItemMeta(im);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryItemClose(InventoryCloseEvent event) {
        InventoryItem invitem;
        try {
            invitem = (InventoryItem) event.getInventory().getHolder();
            if(event.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && invitem.getSerialNumber()
                    .equals(UUID.fromString(ItemUtil.getCustomTag(event.getPlayer().getInventory().getItemInMainHand(), Rollit.INVENTORY_ITEM_UUID_TAG)))) {

                ItemStack[] items = event.getInventory().getContents();

                for(int x = 0; x < items.length; x++) {
                    try {
                        ItemUtil.setCustomTag(event.getPlayer().getInventory().getItemInMainHand(), "inventoryslot-" + (x + 1), ItemUtil.getItemYaml(items[x]));
                    } catch(NullPointerException e) {
                        ItemUtil.setCustomTag(event.getPlayer().getInventory().getItemInMainHand(), "inventoryslot-" + (x+1), "!");
                    }
                }
            }
        } catch(ClassCastException ignored) {
            return;
        }


    }

    @EventHandler
    public void onInventoryItemInteract(InventoryClickEvent event) {
        InventoryItem invitem;
        try {
            invitem = (InventoryItem) event.getInventory().getHolder();
            if (!invitem.getSerialNumber().equals(UUID.fromString(ItemUtil.getCustomTag(event.getWhoClicked().getInventory().getItemInMainHand(), Rollit.INVENTORY_ITEM_UUID_TAG)))) {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
            }
            if(ItemUtil.hasCustomTag(event.getCurrentItem(), Rollit.INVENTORY_ITEM_TAG)) {
                event.setCancelled(true);
            }
        } catch(ClassCastException ignored) {
            return;
        }
    }

    public void moneyBagRecipe() {
        ItemStack moneybag = ItemUtil.getSkullFromTexture(InventoryItemType.MONEYBAG.skullTexture);
        List<String> stringList = new ArrayList<>();
        stringList.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click with this item in hand");
        stringList.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "to claim this new money bag.");
        ItemMeta im = moneybag.getItemMeta();
        im.setDisplayName(InventoryItemType.MONEYBAG.title);
        im.setLore(stringList);
        moneybag.setItemMeta(im);

        NamespacedKey nsKey = new NamespacedKey(plugin, "moneybag");
        ShapedRecipe r = new ShapedRecipe(nsKey, InventoryItem.createInventoryItem(moneybag, InventoryItemType.MONEYBAG));

        r.shape(" L ", "LSL", " L ");
        r.setIngredient('L', Material.RABBIT_HIDE);
        r.setIngredient('S', Material.STRING);

        plugin.getServer().addRecipe(r);

    }




}

