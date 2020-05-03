package net.lostfables.lughgk.rollit.commands;
import net.lostfables.lughgk.rollit.Rollit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.spigotmc.event.entity.EntityDismountEvent;


public class SitCommand implements CommandExecutor, Listener {

    private Rollit plugin = Rollit.getPlugin(Rollit.class);

    public SitCommand() {
        plugin.getCommand("sit").setExecutor(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public boolean seat(Player player, Location loc) {


        if(player.isOnGround()) {
            ArmorStand seat = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            seat.setCustomName(ChatColor.MAGIC + "seat");
            seat.setInvulnerable(true);
            seat.setCanMove(false);
            seat.setVisible(false);
            seat.addPassenger(player);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;
            Location playerLoc = player.getLocation();
            playerLoc.setY(playerLoc.getY()-1.7);
            return seat(player, playerLoc);
        }


        sender.sendMessage(ChatColor.RED + "[Rollit] You must be a player to sit!");
        return false;
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if(event.getDismounted().getCustomName().equals(ChatColor.MAGIC + "seat")) {
            event.getDismounted().remove();
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Material[] mats = plugin.getSitBlocks();
            for(short index = 0; index < mats.length; index++) {
                if(event.getClickedBlock().getType() == mats[index]) {
                    Location blockLoc = event.getClickedBlock().getLocation();
                    blockLoc.setY(blockLoc.getY()-1.2);
                    blockLoc.setX(blockLoc.getX()+0.5);
                    blockLoc.setZ(blockLoc.getZ()+0.5);
                    seat(event.getPlayer(), blockLoc);
                }
            }

        }

    }
}
