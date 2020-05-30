package net.lostfables.lughgk.rollit;

import co.lotc.core.bukkit.command.Commands;
import net.lostfables.lughgk.rollit.utilitycommands.*;
import net.lostfables.lughgk.rollit.inventoryitems.InventoryItemCommands;
import net.lostfables.lughgk.rollit.inventoryitems.InventoryItemHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class Rollit extends JavaPlugin {

    private static Rollit instance;
    private List<String> sitBlocks;
    private Map<UUID, Location> rangePlayers;
    private int rollCap;
    private int showDistance;
    private int rollDistance;
    private int rangeDistance;

    public final static String BASE_PERMISSION = "rollit";
    public final static String INVENTORY_ITEM_TAG = "inv-item";
    public final static String INVENTORY_ITEM_SIZE_TAG = "inv-size";
    public final static String INVENTORY_ITEM_NAME_TAG = "inv-name";
    public final static String INVENTORY_ITEM_TYPE_TAG = "inv-type";

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        rollDistance = getConfig().getInt("roll_distance");
        rollCap = getConfig().getInt("roll_cap");
        showDistance = getConfig().getInt("show_distance");
        rangeDistance = getConfig().getInt("range_distance");
        sitBlocks = getConfig().getStringList("sit_blocks");
        rangePlayers = new HashMap<>();

        new RollCommand();
        new ShowCommand();
        new SitCommand();
        Commands.build(getCommand("invitem"), () -> new InventoryItemCommands(this));
        Commands.build(getCommand("rollit"), () -> new RollitCommands(this));
        Commands.build(getCommand("distance"), () -> new DistanceCommand(this));

        new InventoryItemHandler();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Rollit get() {
        return instance;
    }


    public Material[] getSitBlocks() {
        Material[] mats = new Material[sitBlocks.size()];
        for(int index = 0; index < sitBlocks.size(); index++) {
            mats[index] = Material.getMaterial(sitBlocks.get(index));
        }
        return mats;

    }

    public int getRangeDistance() {
        return rangeDistance;
    }

    public void setRangeDistance(int rangeDistance) {
        this.rangeDistance = rangeDistance;
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

    public Map<UUID, Location> getRangePlayers() {
        return rangePlayers;
    }

    public void setRangePlayers(Map<UUID, Location> rangePlayers) {
        this.rangePlayers = rangePlayers;
    }

}
