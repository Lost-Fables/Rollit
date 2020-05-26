package net.lostfables.lughgk.rollit;

import co.lotc.core.bukkit.command.Commands;
import net.lostfables.lughgk.rollit.utilitycommands.*;
import net.lostfables.lughgk.rollit.inventoryitems.InventoryItemCommands;
import net.lostfables.lughgk.rollit.inventoryitems.InventoryItemHandler;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Rollit extends JavaPlugin {

    private List<String> sitBlocks;
    private int rollCap, showDistance, rollDistance;

    public final static String BASE_PERMISSION = "Rollit";
    public final static String INVENTORY_ITEM_TAG = "inv-item";
    public final static String INVENTORY_ITEM_SIZE_TAG = "inv-size";
    public final static String INVENTORY_ITEM_NAME_TAG = "inv-name";
    public final static String INVENTORY_ITEM_TYPE_TAG = "inv-type";

    @Override
    public void onEnable() {
        saveDefaultConfig();

        rollDistance = getConfig().getInt("roll_distance");
        rollCap = getConfig().getInt("roll_cap");
        showDistance = getConfig().getInt("show_distance");
        sitBlocks = getConfig().getStringList("sit_blocks");

        new RollCommand();
        new ShowCommand();
        new SitCommand();
        Commands.build(getCommand("invitem"), () -> new InventoryItemCommands(this));
        Commands.build(getCommand("rollit"), () -> new RollitCommands(this));

        new InventoryItemHandler();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public Material[] getSitBlocks() {
        Material[] mats = new Material[sitBlocks.size()];
        for(int index = 0; index < sitBlocks.size(); index++) {
            mats[index] = Material.getMaterial(sitBlocks.get(index));
        }
        return mats;

    }

    public int getShowDistance() {
        return showDistance;
    }

    public int getRollCap() {
        return rollCap;
    }

    public int getRollDistance() {
        return rollDistance;
    }

    public void setSitBlocks(List<String> sitBlocks) {
        this.sitBlocks = sitBlocks;
    }

    public void setRollCap(int rollCap) {
        this.rollCap = rollCap;
    }

    public void setShowDistance(int showDistance) {
        this.showDistance = showDistance;
    }

    public void setRollDistance(int rollDistance) {
        this.rollDistance = rollDistance;
    }

}
