package net.lostfables.lughgk.rollit.inventoryitems;

import co.lotc.core.bukkit.util.ItemUtil;
import co.lotc.core.command.CommandTemplate;
import co.lotc.core.command.annotate.Cmd;
import net.lostfables.lughgk.rollit.Rollit;
import net.lostfables.lughgk.rollit.inventoryitems.InventoryItem;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InventoryItemCommands extends CommandTemplate {

    public final static String BASE_INV_ITEM_PERMISSION = "InventoryItem";
    private Rollit plugin;

    public InventoryItemCommands(Rollit plugin) {
        this.plugin = plugin;
    }

    @Cmd(value = "Create a new inventory item.", permission = Rollit.BASE_PERMISSION + "." + BASE_INV_ITEM_PERMISSION + ".create")
    public void create(CommandSender sender, int size, String[] args) {
        if(sender instanceof Player && ((Player) sender).getInventory().getItemInMainHand() != null) {
            Player p = (Player) sender;
            if(ItemUtil.hasCustomTag(p.getInventory().getItemInMainHand(), Rollit.INVENTORY_ITEM_TAG)) {
                p.sendMessage(ChatColor.RED + "[Rollit] This is already an inventory item!");
                return;
            } else {
                InventoryItem.initInventoryItem(p.getInventory().getItemInMainHand(), StringUtils.join(args, " "), size);
                p.sendMessage(ChatColor.DARK_AQUA + "Successfully created new inventory item!");
            }
        }
    }

}
