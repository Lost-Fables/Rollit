package net.lostfables.lughgk.rollit.inventoryitems;

import co.lotc.core.bukkit.util.ItemUtil;
import net.lostfables.lughgk.rollit.Rollit;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryItem implements InventoryHolder {

    private final Rollit plugin = Rollit.getPlugin(Rollit.class);

    private int size, inventorySlot;
    private String name;
    private Inventory inventoryItem;
    private ItemStack[] initInventory;
    private ItemStack baseItem;

    public InventoryItem(ItemStack item, int inventorySlot, ItemStack[] playerInventory) {
        try {
            this.baseItem = item;
            this.inventorySlot = inventorySlot;
            this.initInventory = playerInventory;
            this.size = Integer.parseInt(ItemUtil.getCustomTag(item, Rollit.INVENTORY_ITEM_TAG));
            this.name = ItemUtil.getCustomTag(item, Rollit.INVENTORY_ITEM_NAME_TAG);
            this.inventoryItem = plugin.getServer().createInventory(this, this.getSize(), this.getName());

            for (int x = 0; x < this.size; x++) {
                if (!ItemUtil.getCustomTag(item, "inventoryslot-" + (x + 1)).equals("!")) {
                    inventoryItem.setItem(x, ItemUtil.getItemFromYaml(ItemUtil.getCustomTag(item, "inventoryslot-" + (x + 1))));
                }
            }
        } catch(Exception e) {
            this.size = -1;
            this.inventorySlot = -1;
            this.name = null;
            inventoryItem = null;
            initInventory = null;

        }
    }

    public InventoryItem(ItemStack item, int inventorySlot) {
        try {
            this.baseItem = item;
            this.inventorySlot = inventorySlot;
            this.size = Integer.parseInt(ItemUtil.getCustomTag(item, Rollit.INVENTORY_ITEM_TAG));
            this.name = ItemUtil.getCustomTag(item, Rollit.INVENTORY_ITEM_NAME_TAG);
            this.inventoryItem = plugin.getServer().createInventory(this, this.getSize(), this.getName());

            for (int x = 0; x < this.size; x++) {
                if (!ItemUtil.getCustomTag(item, "inventoryslot-" + (x + 1)).equals("!")) {
                    inventoryItem.setItem(x, ItemUtil.getItemFromYaml(ItemUtil.getCustomTag(item, "inventoryslot-" + (x + 1))));
                }
            }
        } catch(Exception e) {
            this.size = -1;
            this.inventorySlot = -1;
            this.name = null;
            inventoryItem = null;

        }
    }

    @Override
    public Inventory getInventory() {
        return inventoryItem;
    }

    public ItemStack[] getInitInventory() {
        return initInventory;
    }

    public ItemStack getBaseItem() {
        return baseItem;
    }

    public void setBaseItem(ItemStack baseItem) {
        this.baseItem = baseItem;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    //initialize an Inventory inside of an item
    public static ItemStack initInventoryItem(ItemStack item, String name, int size) {
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_TAG, String.valueOf(size));
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_NAME_TAG, name);
        for(int x = 0; x<size; x++) {
            ItemUtil.setCustomTag(item,"inventoryslot-" + (x+1),"!");
        }
        return item;
    }


    //load an item for an inventory
    public static ItemStack createInventoryItem(ItemStack item, InventoryItemType type) {
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_TAG, String.valueOf(type.size));
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_NAME_TAG, type.title);
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_TYPE_TAG, type.tag);
        for(int x = 0; x<type.size; x++) {
            ItemUtil.setCustomTag(item,"inventoryslot-" + (x+1),"!");
        }
        return item;
    }
}
