package net.lostfables.lughgk.rollit;

import net.lostfables.lughgk.rollit.commands.RollCommand;
import net.lostfables.lughgk.rollit.commands.ShowCommand;
import net.lostfables.lughgk.rollit.commands.SitCommand;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Rollit extends JavaPlugin {

    private int rollCap, showDistance, rollDistance;
    List<String> sitBlocks;

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

    @Override
    public void onEnable() {
        saveDefaultConfig();
        rollDistance = getConfig().getInt("roll_distance");
        rollCap = getConfig().getInt("roll_cap");
        showDistance = getConfig().getInt("show_distance");
        sitBlocks = getConfig().getStringList("sit_blocks");
        System.out.println(getSitBlocks().toString());
        new RollCommand();
        new ShowCommand();
        new SitCommand();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
