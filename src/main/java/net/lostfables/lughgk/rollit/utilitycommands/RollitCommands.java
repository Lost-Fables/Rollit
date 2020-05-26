package net.lostfables.lughgk.rollit.utilitycommands;

import co.lotc.core.command.CommandTemplate;
import co.lotc.core.command.annotate.Cmd;
import net.lostfables.lughgk.rollit.Rollit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;


public class RollitCommands  extends CommandTemplate {

    private Rollit plugin;

    public RollitCommands(Rollit plugin) {
        this.plugin = plugin;

    }

    @Cmd(value = "Reloads the Rollit plugin configuration file.", permission = Rollit.BASE_PERMISSION + ".reload")
    public void reload(CommandSender sender) {
        plugin.getConfig().options().copyDefaults(true);
        plugin.setRollDistance(plugin.getConfig().getInt("roll_distance"));
        plugin.setRollCap(plugin.getConfig().getInt("roll_cap"));
        plugin.setShowDistance(plugin.getConfig().getInt("show_distance"));
        plugin.setSitBlocks(plugin.getConfig().getStringList("sit_blocks"));
        sender.sendMessage(ChatColor.DARK_AQUA + "[Rollit] Config has been reloaded.");
        sender.sendMessage(plugin.getRollCap() + "" + plugin.getRollDistance() + "" + plugin.getShowDistance() + "" + plugin.getSitBlocks());

    }

}
