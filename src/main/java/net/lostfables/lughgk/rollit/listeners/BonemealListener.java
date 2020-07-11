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

import java.util.EnumMap;
import java.util.EnumSet;

public final class BonemealListener implements Listener {

	// Things that don't have a tall or potted version.
	private static final EnumSet<Material> NORMIES;
	static {
		NORMIES = EnumSet.of(Material.LILY_PAD);
		NORMIES.add(Material.VINE);
		NORMIES.add(Material.SEA_PICKLE);
	}

	// Sunflower, Lillac, Rose bush, and Peony are all done by Vanilla. Do not do Grass/Fern/Seagrass (short), Wither_Rose, or Mushrooms.
	private static final EnumMap<Material, Material> POTTED_BOIS = new EnumMap<>(Material.class); // Potted, Normal
	static {
		POTTED_BOIS.put(Material.POTTED_DANDELION, Material.DANDELION);
		POTTED_BOIS.put(Material.POTTED_POPPY, Material.POPPY);
		POTTED_BOIS.put(Material.POTTED_ALLIUM, Material.ALLIUM);
		POTTED_BOIS.put(Material.POTTED_BLUE_ORCHID, Material.BLUE_ORCHID);
		POTTED_BOIS.put(Material.POTTED_ORANGE_TULIP, Material.ORANGE_TULIP);
		POTTED_BOIS.put(Material.POTTED_RED_TULIP, Material.RED_TULIP);
		POTTED_BOIS.put(Material.POTTED_PINK_TULIP, Material.PINK_TULIP);
		POTTED_BOIS.put(Material.POTTED_WHITE_TULIP, Material.WHITE_TULIP);
		POTTED_BOIS.put(Material.POTTED_AZURE_BLUET, Material.AZURE_BLUET);
		POTTED_BOIS.put(Material.POTTED_OXEYE_DAISY, Material.OXEYE_DAISY);
		POTTED_BOIS.put(Material.POTTED_DEAD_BUSH, Material.DEAD_BUSH);
		POTTED_BOIS.put(Material.POTTED_CACTUS, Material.CACTUS);
	};

	// Things that we drop a short version of instead of the tall version.
	private static final EnumMap<Material, Material> TALL_BOIS = new EnumMap<>(Material.class); // Tall, Short
	static {
		TALL_BOIS.put(Material.TALL_GRASS, Material.GRASS);
		TALL_BOIS.put(Material.TALL_SEAGRASS, Material.SEAGRASS);
		TALL_BOIS.put(Material.LARGE_FERN, Material.FERN);
	};

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void PlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			ItemStack item = null;
			if (event.getHand() == EquipmentSlot.HAND) {
				item = player.getInventory().getItemInMainHand();
			} else {
				item = player.getInventory().getItemInOffHand();
			}

			if (block != null && player.hasPermission("flowers.use") && isBoneMeal(item) && isFlower(block.getType())) {
				event.setCancelled(true);
				if (!player.getGameMode().equals(GameMode.CREATIVE)) {
					consumeItem(item);
				}
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
			return (NORMIES.contains(mat) || POTTED_BOIS.containsValue(mat) || TALL_BOIS.containsKey(mat));
		}
		return false;
	}

	private Material dePot(Material mat) {
		if (POTTED_BOIS.containsKey(mat)) {
			return POTTED_BOIS.get(mat);
		}
		return mat;
	}

	private Material shorten(Material mat) {
		if (TALL_BOIS.containsKey(mat)) {
			return TALL_BOIS.get(mat);
		}
		return mat;
	}

	private void consumeItem(ItemStack item){
		item.setAmount(item.getAmount() - 1);
	}

	public void createFlower(Block block){
		ItemStack flower = new ItemStack(shorten(dePot(block.getBlockData().getMaterial())));
		block.getWorld().dropItemNaturally(block.getLocation(), flower);
	}
}