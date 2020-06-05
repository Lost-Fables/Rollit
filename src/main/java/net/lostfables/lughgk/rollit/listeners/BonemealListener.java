package net.lostfables.lughgk.rollit.listeners;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.FlowerPot;
import org.bukkit.plugin.java.JavaPlugin;

public final class BonemealListener implements Listener {

	// Sunflower, Lillac, Rose bush, and Peony are all done by Vanilla. Do not do Grass/Fern/Seagrass (short), Wither_Rose, or Mushrooms.
	private static final Material[] GROWABLES = {
			Material.DANDELION,
			Material.POPPY,
			Material.ALLIUM,
			Material.BLUE_ORCHID,
			Material.ORANGE_TULIP,
			Material.RED_TULIP,
			Material.PINK_TULIP,
			Material.WHITE_TULIP,
			Material.AZURE_BLUET,
			Material.OXEYE_DAISY,
			Material.DEAD_BUSH,
			Material.LILY_PAD,
			Material.VINE,
			Material.SEA_PICKLE,
			Material.CACTUS
	};

	// Things that we drop a short version of instead of the tall version.
	private static final Material[] TALL_BOIS = {
			Material.TALL_GRASS,
			Material.TALL_SEAGRASS,
			Material.LARGE_FERN
	};

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void PlayerInteractEvent(PlayerInteractEvent event){
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			ItemStack item = null;
			if (event.getHand() == EquipmentSlot.HAND) {
				item = player.getInventory().getItemInMainHand();
			} else {
				item = player.getInventory().getItemInOffHand();
			}

			if (block != null && player.hasPermission("flowers.use") && isBoneMeal(item) && isFlower(dePot(block.getType(), event))) {
				event.setCancelled(true);
				consumeItem(item);
				createFlower(block);
				block.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 6);
			}
		}

	}

	private boolean isBoneMeal(ItemStack item){
		return (item.getType() == Material.BONE_MEAL);
	}

	private boolean isFlower(Material mat){
		if (mat != null) {
			for (Material thisMat : GROWABLES) {
				if (mat.equals(thisMat)) {
					return true;
				}
			}
			for (Material thisMat : TALL_BOIS) {
				if (mat.equals(thisMat)) {
					return true;
				}
			}
		}

		return false;
	}

	private Material dePot(Material mat, PlayerInteractEvent event) {
		if (mat != null && mat.toString().startsWith("POTTED_")) {
			if (event != null) {
				event.setCancelled(true);
			}
			String contents = mat.toString().replaceAll("POTTED_", "");
			for (Material thisMat : TALL_BOIS) {
				if (thisMat.toString().endsWith(contents)) {
					return thisMat;
				}
			}
			return Material.getMaterial(contents);
		}
		return mat;
	}

	private Material shorten(Material mat) {
		if (mat.toString().startsWith("TALL_")) {
			String contents = mat.toString().replaceAll("TALL_", "");
			return Material.getMaterial(contents);
		} else if (mat.toString().startsWith("LARGE_")) {
			String contents = mat.toString().replaceAll("LARGE_", "");
			return Material.getMaterial(contents);
		}
		return mat;
	}

	private void consumeItem(ItemStack item){
		item.setAmount(item.getAmount() - 1);
	}

	public void createFlower(Block block){
		ItemStack flower = new ItemStack(shorten(dePot(block.getBlockData().getMaterial(), null)));
		block.getWorld().dropItemNaturally(block.getLocation(), flower);
	}
}