package net.lostfables.lughgk.rollit.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class FreeSilktouchListener implements Listener {

	private Material[] mats = {
			Material.ICE,
			Material.BLUE_ICE,
			Material.PACKED_ICE,
			Material.BOOKSHELF,
			Material.GLASS,
			Material.GLASS_PANE,
			Material.BLACK_STAINED_GLASS,
			Material.BLACK_STAINED_GLASS_PANE,
			Material.RED_STAINED_GLASS,
			Material.RED_STAINED_GLASS_PANE,
			Material.GREEN_STAINED_GLASS,
			Material.GREEN_STAINED_GLASS_PANE,
			Material.BROWN_STAINED_GLASS,
			Material.BROWN_STAINED_GLASS_PANE,
			Material.BLUE_STAINED_GLASS,
			Material.BLUE_STAINED_GLASS_PANE,
			Material.PURPLE_STAINED_GLASS,
			Material.PURPLE_STAINED_GLASS_PANE,
			Material.CYAN_STAINED_GLASS,
			Material.CYAN_STAINED_GLASS_PANE,
			Material.LIGHT_GRAY_STAINED_GLASS,
			Material.LIGHT_GRAY_STAINED_GLASS_PANE,
			Material.GRAY_STAINED_GLASS,
			Material.GRAY_STAINED_GLASS_PANE,
			Material.PINK_STAINED_GLASS,
			Material.PINK_STAINED_GLASS_PANE,
			Material.LIME_STAINED_GLASS,
			Material.LIME_STAINED_GLASS_PANE,
			Material.YELLOW_STAINED_GLASS,
			Material.YELLOW_STAINED_GLASS_PANE,
			Material.LIGHT_BLUE_STAINED_GLASS,
			Material.LIGHT_BLUE_STAINED_GLASS_PANE,
			Material.MAGENTA_STAINED_GLASS,
			Material.MAGENTA_STAINED_GLASS_PANE,
			Material.ORANGE_STAINED_GLASS,
			Material.ORANGE_STAINED_GLASS_PANE,
			Material.WHITE_STAINED_GLASS,
			Material.WHITE_STAINED_GLASS_PANE
	};

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		for (Material mat : mats) {
			if (e.getBlock().getType().equals(mat)) {
				e.setCancelled(true);
				ItemStack item = new ItemStack(mat);
				Location loc = e.getBlock().getLocation();
				loc.getWorld().dropItemNaturally(loc, item);
				break;
			}
		}
	}

}
