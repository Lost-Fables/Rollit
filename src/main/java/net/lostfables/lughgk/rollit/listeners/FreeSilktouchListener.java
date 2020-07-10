package net.lostfables.lughgk.rollit.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;

public class FreeSilktouchListener implements Listener {

	private static final EnumSet<Material> mats;
	private static final ItemStack silkPick = new ItemStack(Material.DIAMOND_PICKAXE);
	static {
		silkPick.addEnchantment(Enchantment.SILK_TOUCH, 1);

		mats = EnumSet.of(Material.ICE);
		mats.add(Material.BLUE_ICE);
		mats.add(Material.PACKED_ICE);
		mats.add(Material.BOOKSHELF);
		mats.add(Material.GLASS);
		mats.add(Material.GLASS_PANE);
		mats.add(Material.BLACK_STAINED_GLASS);
		mats.add(Material.BLACK_STAINED_GLASS_PANE);
		mats.add(Material.RED_STAINED_GLASS);
		mats.add(Material.RED_STAINED_GLASS_PANE);
		mats.add(Material.GREEN_STAINED_GLASS);
		mats.add(Material.GREEN_STAINED_GLASS_PANE);
		mats.add(Material.BROWN_STAINED_GLASS);
		mats.add(Material.BROWN_STAINED_GLASS_PANE);
		mats.add(Material.BLUE_STAINED_GLASS);
		mats.add(Material.BLUE_STAINED_GLASS_PANE);
		mats.add(Material.PURPLE_STAINED_GLASS);
		mats.add(Material.PURPLE_STAINED_GLASS_PANE);
		mats.add(Material.CYAN_STAINED_GLASS);
		mats.add(Material.CYAN_STAINED_GLASS_PANE);
		mats.add(Material.LIGHT_GRAY_STAINED_GLASS);
		mats.add(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		mats.add(Material.GRAY_STAINED_GLASS);
		mats.add(Material.GRAY_STAINED_GLASS_PANE);
		mats.add(Material.PINK_STAINED_GLASS);
		mats.add(Material.PINK_STAINED_GLASS_PANE);
		mats.add(Material.LIME_STAINED_GLASS);
		mats.add(Material.LIME_STAINED_GLASS_PANE);
		mats.add(Material.YELLOW_STAINED_GLASS);
		mats.add(Material.YELLOW_STAINED_GLASS_PANE);
		mats.add(Material.LIGHT_BLUE_STAINED_GLASS);
		mats.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		mats.add(Material.MAGENTA_STAINED_GLASS);
		mats.add(Material.MAGENTA_STAINED_GLASS_PANE);
		mats.add(Material.ORANGE_STAINED_GLASS);
		mats.add(Material.ORANGE_STAINED_GLASS_PANE);
		mats.add(Material.WHITE_STAINED_GLASS);
		mats.add(Material.WHITE_STAINED_GLASS_PANE);
	}

	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e) {
		if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			if (mats.contains(e.getBlock().getType())) {
				e.setCancelled(true);
				e.getBlock().breakNaturally(silkPick);
			}
		}
	}

}
