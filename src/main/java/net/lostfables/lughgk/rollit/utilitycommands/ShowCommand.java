package net.lostfables.lughgk.rollit.utilitycommands;

import co.lotc.core.bukkit.util.PlayerUtil;
import co.lotc.core.util.MessageUtil;
import net.lostfables.lughgk.rollit.Rollit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                p.sendMessage(showItem(player));
            }
            return true;

        }

        sender.sendMessage(ChatColor.RED + "[Rollit] You must be a Player to execute this Command!");
        return false;
    }

    public TextComponent showItem(Player player) {

        TextComponent chatItem = new TextComponent();
        ItemStack itemInHand = null;
        List<String> lines = new ArrayList<>();

        Map<Enchantment, Integer> enchants = new HashMap<>();
        String itemName;
        int amount = 1;

        try {
            itemInHand = player.getInventory().getItemInMainHand();

            if (itemInHand.getItemMeta().getDisplayName().equals("")) {
                itemName = itemInHand.getType().toString();
                enchants.putAll(itemInHand.getItemMeta().getEnchants());
                lines = itemInHand.getLore();
                amount = itemInHand.getAmount();

            } else {
                itemName = itemInHand.getItemMeta().getDisplayName();
                enchants.putAll(itemInHand.getItemMeta().getEnchants());
                lines.addAll(itemInHand.getLore());
                amount = itemInHand.getAmount();
            }

        } catch(NullPointerException e) {
            itemName =  ChatColor.DARK_GRAY + player.getDisplayName() + "'s Hand";
            lines.add(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "Very Common " + ChatColor.GRAY + "| Natural | " + ChatColor.DARK_GRAY + "Mundane | Miscellanea" + ChatColor.GRAY + "]");
            lines.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "This is " + player.getDisplayName() + "'s empty hand.");
            amount = 1;

        }

        chatItem.setText(ChatColor.DARK_AQUA + "["+ ChatColor.WHITE + player.getDisplayName() + ChatColor.DARK_AQUA + " is holding " + ChatColor.WHITE + itemName + ChatColor.WHITE + " x" + amount + ChatColor.DARK_AQUA + "]");

        StringBuilder hoverText = new StringBuilder(itemName);
        try {
            for (String str : lines) {
                hoverText.append("\n").append(ChatColor.RESET + str);
            }
        } catch(Exception e) {

        }

        chatItem.setHoverEvent(MessageUtil.hoverEvent(hoverText.toString()));
        return chatItem;
    }
}
