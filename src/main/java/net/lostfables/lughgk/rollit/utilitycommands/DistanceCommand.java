package net.lostfables.lughgk.rollit.utilitycommands;

import co.lotc.core.command.CommandTemplate;
import co.lotc.core.command.annotate.Cmd;
import net.lostfables.lughgk.rollit.Rollit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class DistanceCommand extends CommandTemplate implements Listener {

    public final static String BASE_DISTANCE_PERMISSION = "distance";
    private Rollit plugin;


    public DistanceCommand(Rollit plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }
    
    public static double distance3D(Location loc1, Location loc2) {
        return Math.round(Math.sqrt(Math.pow((loc1.getX() - loc2.getX()), 2) + Math.pow((loc1.getY() - loc2.getY()), 2) + Math.pow((loc1.getZ() - loc2.getZ()), 2)));
    }

    @Cmd(value = "Gets the distance between you and a target.", permission = Rollit.BASE_PERMISSION + "." + BASE_DISTANCE_PERMISSION + ".player")
    public void player(CommandSender sender, Player player) {
        if(sender instanceof Player) {
            int d = (int) distance3D(((Player) sender).getLocation(), player.getLocation());
            if(d < plugin.getRangeDistance()) {
                sender.sendMessage(ChatColor.DARK_AQUA + "The Distance between you and " + ChatColor.WHITE + player.getName() + ChatColor.DARK_AQUA + " is " + ChatColor.WHITE + d);

                return;
            }
        }
    }

    @Cmd(value = "Gets the distance between two targets.", permission = Rollit.BASE_PERMISSION + "." + BASE_DISTANCE_PERMISSION + ".other")
    public void player(CommandSender sender, Player player, Player player2) {
        if(sender instanceof Player) {
            int d = (int) distance3D(player2.getLocation(), player.getLocation());
            if(d < plugin.getRangeDistance()) {
                sender.sendMessage(ChatColor.DARK_AQUA + "The Distance between " + ChatColor.WHITE + player.getName() + ChatColor.DARK_AQUA + " and " + ChatColor.WHITE + player2.getName() + ChatColor.DARK_AQUA + " is " + ChatColor.WHITE + d);

                return;
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(plugin.getRangePlayers().containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(ChatColor.DARK_AQUA + "Distance moved: " + ChatColor.WHITE + (int) distance3D(event.getPlayer().getLocation(), plugin.getRangePlayers().get(event.getPlayer().getUniqueId()))).create());
        }
    }



    @Cmd(value = "Toggle between tracking your distance", permission = Rollit.BASE_PERMISSION + "." + BASE_DISTANCE_PERMISSION + ".track")
    public void track(CommandSender sender) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!plugin.getRangePlayers().containsKey(player.getUniqueId())) {
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        plugin.getRangePlayers().put(player.getUniqueId(), player.getLocation());
                        player.sendMessage(ChatColor.DARK_AQUA + "You are now tracking your distance moved.");
                        //pause the task for 30000 millis half a minute
                        Thread.sleep(30000);
                        plugin.getRangePlayers().remove(player.getUniqueId());
                        player.sendMessage(ChatColor.DARK_AQUA + "You are no longer tracking your distance moved.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                return;
            } else if(plugin.getRangePlayers().containsKey(player.getUniqueId())) {
                plugin.getRangePlayers().remove(player.getUniqueId());
                player.sendMessage(ChatColor.DARK_AQUA + "You are no longer tracking your distance moved.");
                return;
            }
        }
    }

}
