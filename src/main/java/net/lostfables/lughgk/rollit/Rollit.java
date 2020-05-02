package net.lostfables.lughgk.rollit;

import net.lostfables.lughgk.rollit.commands.RollCommand;
import net.lostfables.lughgk.rollit.commands.ShowCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rollit extends JavaPlugin {


    private int rollDistance;

    private int rollCap;

    private int showDistance;

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
        new RollCommand();
        new ShowCommand();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
