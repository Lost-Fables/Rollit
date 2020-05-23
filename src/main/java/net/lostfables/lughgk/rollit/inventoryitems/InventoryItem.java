package net.lostfables.lughgk.rollit.inventoryitems;

import co.lotc.core.bukkit.util.ItemUtil;
import net.lostfables.lughgk.rollit.Rollit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryItem implements InventoryHolder {

    private final Rollit plugin = Rollit.getPlugin(Rollit.class);

    private int size;
    private String name;
    private UUID serialNumber;

    public InventoryItem(ItemStack item) {
        size = Integer.parseInt(ItemUtil.getCustomTag(item, Rollit.INVENTORY_ITEM_TAG));
        name = ItemUtil.getCustomTag(item, Rollit.INVENTORY_ITEM_NAME_TAG);
        serialNumber = UUID.fromString(ItemUtil.getCustomTag(item, Rollit.INVENTORY_ITEM_UUID_TAG));
    }

    @Override
    public Inventory getInventory() {

        return null;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public UUID getSerialNumber() {
        return serialNumber;
    }

    //initialize an Inventory inside of an item
    public static ItemStack initInventoryItem(ItemStack item, String name, int size) {
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_TAG, String.valueOf(size));
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_NAME_TAG, name);
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_UUID_TAG, String.valueOf(UUID.randomUUID()));
        for(int x = 0; x<size; x++) {
            ItemUtil.setCustomTag(item,"inventoryslot-" + (x+1),"!");
        }
        return item;
    }

    //initialize an Inventory inside of an item, giving it a UUID
    public static ItemStack initInventoryItem(ItemStack item) {
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_UUID_TAG, String.valueOf(UUID.randomUUID()));
        return item;
    }

    //load an item for an inventory
    public static ItemStack createInventoryItem(ItemStack item, InventoryItemType type) {
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_TAG, String.valueOf(type.size));
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_NAME_TAG, type.title);
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_TYPE_TAG, type.tag);
        ItemUtil.setCustomTag(item, Rollit.INVENTORY_ITEM_UUID_TAG, "!");
        for(int x = 0; x<type.size; x++) {
            ItemUtil.setCustomTag(item,"inventoryslot-" + (x+1),"!");
        }
        return item;
    }
}
