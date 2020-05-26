package net.lostfables.lughgk.rollit.utilitycommands;
import co.lotc.core.bukkit.util.ItemUtil;
import net.lostfables.lughgk.rollit.Rollit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
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
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;


public class SitCommand implements CommandExecutor, Listener {

    private final Rollit plugin = Rollit.getPlugin(Rollit.class);

    public SitCommand() {
        plugin.getCommand("sit").setExecutor(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public boolean seat(Player player, Location loc) {

        if(player.isOnGround() && loc.getNearbyPlayers(3).contains(player) && player.getLocation().getY() > loc.getY()) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if((!itemStack.hasItemMeta() && itemStack.getType().isBlock() && !itemStack.toString().contains("ItemStack{AIR")) || (itemStack.hasItemMeta() && ItemUtil.hasCustomTag(itemStack.getItemMeta(), "item-unplaceable"))) {
                return false;
            }
            ArmorStand seat = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            seat.setCustomName(ChatColor.MAGIC + "seat");
            seat.setInvulnerable(true);
            seat.setCanMove(false);
            seat.setVisible(false);
            seat.addPassenger(player);
            return true;
        }

        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;
            Location blockAbove = player.getLocation();
            blockAbove.setY(blockAbove.getY() + 1);

            if(!blockAbove.getBlock().isEmpty()) {
                player.sendMessage("You don't have enough space to sit!");
                return false;
            }else if(!player.isInsideVehicle()) {
                Location playerLoc = player.getLocation();
                playerLoc.setY(playerLoc.getY()-1.7);
                return seat(player, playerLoc);
            } else {
                player.sendMessage(ChatColor.RED + "[Rollit] You're already sitting!");
                return false;
            }


        }


        sender.sendMessage(ChatColor.RED + "[Rollit] You must be a player to sit!");
        return false;
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        try {
            if (event.getEntity() instanceof Player && event.getDismounted() instanceof ArmorStand && event.getDismounted().getCustomName().equals(ChatColor.MAGIC + "seat")) {
                Player player = (Player) event.getEntity();
                event.getDismounted().remove();
                ArmorStand seat = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                seat.addPassenger(player);
                seat.remove();
            }
        } catch(NullPointerException ignored) {

        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && !event.getPlayer().isSneaking() && !event.getPlayer().isInsideVehicle()) {

            Block clickedBlock = event.getClickedBlock();
            Location blockAbove = event.getClickedBlock().getLocation();
            blockAbove.setY(blockAbove.getY() + 1);
            if (blockAbove.getBlock().isEmpty()) {
                Material[] mats = plugin.getSitBlocks();

                Location blockLoc = clickedBlock.getLocation();
                Location playerLoc = event.getPlayer().getLocation();

                for (Material mat : mats) {
                    if (clickedBlock.getType() == mat && !clickedBlock.getBlockData().toString().contains("half=top") && !clickedBlock.getBlockData().toString().contains("type=top")) {
                        try {
                            Directional clickedStair = (Directional) clickedBlock.getBlockData();

                            if (clickedStair.getFacing() == BlockFace.EAST) {
                                playerLoc.setX(blockLoc.getX() + 0.35);
                                playerLoc.setZ(blockLoc.getZ() + 0.5);
                            } else if (clickedStair.getFacing() == BlockFace.WEST) {
                                playerLoc.setX(blockLoc.getX() + 0.65);
                                playerLoc.setZ(blockLoc.getZ() + 0.5);
                            } else if (clickedStair.getFacing() == BlockFace.NORTH) {
                                playerLoc.setX(blockLoc.getX() + 0.5);
                                playerLoc.setZ(blockLoc.getZ() + 0.65);
                            } else if (clickedStair.getFacing() == BlockFace.SOUTH) {
                                playerLoc.setX(blockLoc.getX() + 0.5);
                                playerLoc.setZ(blockLoc.getZ() + 0.35);
                            }

                        } catch (Exception e) {
                            playerLoc.setX(blockLoc.getX() + 0.5);
                            playerLoc.setZ(blockLoc.getZ() + 0.5);
                        }
                        playerLoc.setY(blockLoc.getY() - 1.2);
                        if(seat(event.getPlayer(), playerLoc)) {
                            event.setCancelled(true);
                        }
                        break;
                    }
                }

            }
        }

    }
}
