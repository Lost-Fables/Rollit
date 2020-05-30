package net.lostfables.lughgk.rollit.inventoryitems;

import co.lotc.core.bukkit.util.ItemUtil;
import net.lostfables.lughgk.rollit.Rollit;
import net.lostfables.lughgk.rollit.enums.InventoryItemType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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

            if(item.hasItemMeta() && ItemUtil.hasCustomTag(item, Rollit.INVENTORY_ITEM_TAG)) {
                event.setCancelled(true);
                Player p = event.getPlayer();
                InventoryItem invItem = new InventoryItem(p.getInventory().getItemInMainHand(), p.getInventory().getHeldItemSlot(), p.getInventory().getContents());
                p.openInventory(invItem.getInventory());
                return;
            }
        }
    }

    @EventHandler
    public void onInventoryItemClose(InventoryCloseEvent event) {
        try {
            if(event.getInventory().getHolder() instanceof InventoryItem) {
                InventoryItem invitem = (InventoryItem) event.getInventory().getHolder();
                Player p = (Player) event.getPlayer();
                if (invitem.getInventorySlot() == p.getInventory().getHeldItemSlot() && invitem.getBaseItem().equals(p.getInventory().getItemInMainHand())) {

                    ItemStack[] items = event.getInventory().getContents();

                    for (int x = 0; x < items.length; x++) {
                        try {
                            ItemUtil.setCustomTag(p.getInventory().getItemInMainHand(), "inventoryslot-" + (x + 1), ItemUtil.getItemYaml(items[x]));
                        } catch (NullPointerException e) {
                            ItemUtil.setCustomTag(p.getInventory().getItemInMainHand(), "inventoryslot-" + (x + 1), "!");
                        }
                    }
                }
            }
        } catch(ClassCastException ignored) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @EventHandler
    public void onInventoryItemClick(InventoryClickEvent event) {

        try {
            if(event.getInventory().getHolder() instanceof InventoryItem) {
                InventoryItem invitem = (InventoryItem) event.getInventory().getHolder();
                Player p = ((Player) event.getWhoClicked());
                if (invitem.getInventorySlot() != p.getInventory().getHeldItemSlot() || !invitem.getBaseItem().equals(p.getInventory().getItemInMainHand())) {
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                    ((Player) event.getWhoClicked()).getInventory().clear();
                    ((Player) event.getWhoClicked()).getInventory().setContents(invitem.getInitInventory());
                    ((Player) event.getWhoClicked()).updateInventory();
                    return;
                }
                if (ItemUtil.hasCustomTag(event.getCurrentItem(), Rollit.INVENTORY_ITEM_TAG) || event.getAction() == InventoryAction.HOTBAR_SWAP) {
                    event.setCancelled(true);
                    p.updateInventory();
                    return;
                }
                if(ItemUtil.hasCustomTag(invitem.getBaseItem(), Rollit.INVENTORY_ITEM_TYPE_TAG)) {
                    InventoryItemType type = InventoryItemType.valueOf(ItemUtil.getCustomTag(invitem.getBaseItem(), Rollit.INVENTORY_ITEM_TYPE_TAG));
                    if(!type.getMats().contains(event.getCurrentItem().getType())) {
                        event.setCancelled(true);
                        p.sendMessage(ChatColor.RED + "[Rollit] You can not put that sort of item here!");
                        p.updateInventory();
                        return;
                    }
                }
            }
        } catch(ClassCastException ignored) {
            return;
        }

    }


    public void moneyBagRecipe() {
        ItemStack moneybag = ItemUtil.getSkullFromTexture(InventoryItemType.MONEYBAG.getSkullTexture());
        List<String> stringList = new ArrayList<>();
        stringList.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click with this item in hand");
        stringList.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "to view its contents.");
        ItemMeta im = moneybag.getItemMeta();
        im.setDisplayName(InventoryItemType.MONEYBAG.getTitle());
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

