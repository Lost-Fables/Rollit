package net.lostfables.lughgk.rollit.commands;

import co.lotc.core.bukkit.util.ChatBuilder;
import co.lotc.core.command.CommandTemplate;
import co.lotc.core.util.MessageUtil;
import net.lostfables.lughgk.rollit.Rollit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RollCommand implements CommandExecutor {

    private Rollit plugin = Rollit.getPlugin(Rollit.class);

    public RollCommand() {
        plugin.getCommand("roll").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length != 1) {
            sender.sendMessage(ChatColor.RED + "[Rollit] Incorrect Arguments for this Command!");
            return false;
        }

        if(sender instanceof Player && sender.hasPermission("rollit.roll")) {
            roll(args[0], (Player) sender);
            return true;
        }

        sender.sendMessage(ChatColor.RED + "[Rollit] You must be a Player to execute this Command!");
        return false;
    }


    public static int numOfDice(String rollString) {
        if(rollString.indexOf('d') == -1 || rollString.indexOf('d') <= 0) {
            return -1;
        }
        try {
            return Integer.parseInt(rollString.substring(0, rollString.indexOf('d')));
        } catch(Exception e) {
            return -1;
        }
    }

    public static int dieFace(String rollString) {
        if(rollString.indexOf('d') == -1 || rollString.indexOf('d') <= 0 || rollString.substring(rollString.indexOf('d')).contains("d+")) {
            return -1;
        }

        try {
            return Integer.parseInt(rollString.substring(rollString.indexOf('d')+1));
        } catch(Exception e) {
            try {
                if (rollString.indexOf('+') != -1) {
                    return Integer.parseInt(rollString.substring(rollString.indexOf('d') + 1, rollString.indexOf('+')));
                } else if (rollString.indexOf('-') != -1) {
                    return Integer.parseInt(rollString.substring(rollString.indexOf('d') + 1, rollString.indexOf('-')));
                } else if (rollString.indexOf('*') != -1) {
                    return Integer.parseInt(rollString.substring(rollString.indexOf('d') + 1, rollString.indexOf('*')));
                } else if (rollString.indexOf('/') != -1) {
                    return Integer.parseInt(rollString.substring(rollString.indexOf('d') + 1, rollString.indexOf('/')));
                }
            } catch(Exception errorOnRemovingOpSymbol) {
                return -1;
            }

        }
        return -1;
    }

    public Integer solver(int firstTotal, String rollString) {

        try {
            if (rollString.indexOf('+') != -1) {
                return firstTotal + Integer.parseInt(rollString.substring(rollString.indexOf('+') + 1));
            } else if (rollString.indexOf('-') != -1) {
                return firstTotal - Integer.parseInt(rollString.substring(rollString.indexOf('-') + 1));
            } else if (rollString.indexOf('*') != -1) {
                return firstTotal * Integer.parseInt(rollString.substring(rollString.indexOf('*') + 1));
            } else if (rollString.indexOf('/') != -1) {
                return firstTotal / Integer.parseInt(rollString.substring(rollString.indexOf('/') + 1));
            } else return firstTotal;
        } catch(Exception e) {
            return null;
        }



    }

    public void roll(String rollString, Player player) {
        if (numOfDice(rollString) == -1 || dieFace(rollString) < 0 || plugin.getRollCap() < numOfDice(rollString)) {
            player.sendMessage(ChatColor.RED + "[Rollit] That is the incorrect syntax for a roll!");
            return;
        }

        List<Player> players = (List<Player>) player.getLocation().getNearbyPlayers(plugin.getRollDistance());
        String firstLine = ChatColor.WHITE + player.getName() + ChatColor.DARK_AQUA + " rolled " + ChatColor.WHITE + rollString;
        List<String> extraLines = new ArrayList<>();

        TextComponent output = new TextComponent();

        // Build rolls array into string [20] [17] [5]...
        // and formats the total for two extra lines.
        StringBuilder rolls = new StringBuilder();
        int total = 0;
        for (int index = 0; index < numOfDice(rollString); index++) {
            int roll = (int) Math.floor(Math.random() * (dieFace(rollString) - 1 + 1) + 1);
            rolls.append(ChatColor.DARK_AQUA).append("[").append(ChatColor.WHITE).append(roll).append(ChatColor.DARK_AQUA).append("] ");
            total += roll;
        }
        if (solver(total, rollString) == null) {
            player.sendMessage(ChatColor.RED + "[Rollit] That is the incorrect syntax for a roll!");
            return;
        }
        extraLines.add(rolls.toString());
        extraLines.add(ChatColor.DARK_AQUA + "Total: " + ChatColor.WHITE + solver(total, rollString));

        // Format our lines into a singular string with \n linebreaks.
        output.setText(firstLine);
        StringBuilder hoverText = new StringBuilder(firstLine);
        for (String str : extraLines) {
            hoverText.append("\n").append(str);
        }

        // Use singular string in a hover event with line breaks to create a box without using an item.
        output.setHoverEvent(MessageUtil.hoverEvent(hoverText.toString()));

        // Display text to all players involved.
        for (Player p : players) {
            p.sendMessage(output);
        }
    }
}
