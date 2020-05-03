package net.lostfables.lughgk.rollit.commands;

import co.lotc.core.bukkit.util.ChatBuilder;
import co.lotc.core.bukkit.util.PlayerUtil;
import net.lostfables.lughgk.rollit.Rollit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShowCommand implements CommandExecutor {

    private Rollit plugin = Rollit.getPlugin(Rollit.class);

    public ShowCommand() {
        plugin.getCommand("show").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("rollit.show")) {
            return false;
        }

        if(args.length > 1) {
            sender.sendMessage(ChatColor.RED + "[Rollit] Incorrect Arguments for this Command!");
            return false;
        }

        if(sender instanceof Player) {
            Player player = (Player) sender;
            Player reciever = null;
            ChatBuilder chatItem = showItem(player);
            List<Player> players = new ArrayList<>();

            if(args.length == 0) {
                players = (List<Player>) player.getLocation().getNearbyPlayers(plugin.getShowDistance());
            } else {
                if(plugin.getServer().getPlayer(PlayerUtil.getPlayerUUID(args[0])) != null) {
                    reciever = plugin.getServer().getPlayer(PlayerUtil.getPlayerUUID(args[0]));
                } else {
                    player.sendMessage(ChatColor.RED + "[Rollit] This player does not exist!");
                    return false;
                }
                if(reciever != player) {
                    players.add(reciever);
                }
                players.add(player);
            }

            for(Player p : players) {
                chatItem.send(p);
            }
            return true;

        }

        sender.sendMessage(ChatColor.RED + "[Rollit] You must be a Player to execute this Command!");
        return false;
    }

    public ChatBuilder showItem(Player player) {
        ItemStack itemInHand = null;


        String itemName;

        try {
            itemInHand = player.getInventory().getItemInMainHand();

            if (itemInHand.getItemMeta().getDisplayName().equals("")) {
                itemName = itemInHand.getType().toString() + " x" + itemInHand.getAmount();
            } else {
                itemName = itemInHand.getItemMeta().getDisplayName() + " x" + itemInHand.getAmount();
            }

        } catch(NullPointerException e) {
            itemInHand = new ItemStack(Material.STONE);
            ItemMeta itemInHandMeta = itemInHand.getItemMeta();
            List<String> lore = new ArrayList<>();
            itemInHandMeta.setDisplayName(ChatColor.DARK_GRAY + player.getDisplayName() + "'s hand");
            lore.add(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "Very Common " + ChatColor.GRAY + "| Natural | " + ChatColor.DARK_GRAY + "Mundane | Miscellanea" + ChatColor.GRAY + "]");
            lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "This is " + player.getDisplayName() + "'s empty hand.");
            itemInHandMeta.setLore(lore);
            itemInHand.setItemMeta(itemInHandMeta);

            itemName = itemInHand.getItemMeta().getDisplayName() + " x" + itemInHand.getAmount();
        }


        ChatBuilder chatItem = new ChatBuilder();
        chatItem = chatItem.hoverItem(itemInHand);
        chatItem.append(ChatColor.WHITE + player.getDisplayName() +  ChatColor.DARK_AQUA + " is holding [" + ChatColor.WHITE + itemName + ChatColor.DARK_AQUA + "]");
        return chatItem;
    }
}
