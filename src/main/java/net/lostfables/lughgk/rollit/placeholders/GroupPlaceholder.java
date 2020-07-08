package net.lostfables.lughgk.rollit.placeholders;

import co.lotc.core.bukkit.menu.Menu;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.lostfables.lughgk.rollit.Rollit;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
public class GroupPlaceholder extends PlaceholderExpansion {

	private Rollit plugin;

	/**
	 * Since we register the expansion inside our own plugin, we
	 * can simply use this method here to get an instance of our
	 * plugin.
	 *
	 * @param plugin
	 *        The instance of our plugin.
	 */
	public GroupPlaceholder(Rollit plugin) {
		this.plugin = plugin;
	}

	/**
	 * Because this is an internal class,
	 * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
	 * PlaceholderAPI is reloaded
	 *
	 * @return true to persist through reloads
	 */
	@Override
	public boolean persist() {
		return true;
	}

	/**
	 * Because this is a internal class, this check is not needed
	 * and we can simply return {@code true}
	 *
	 * @return Always true since it's an internal class.
	 */
	@Override
	public boolean canRegister() {
		return true;
	}

	/**
	 * The name of the person who created this expansion should go here.
	 * <br>For convienience do we return the author from the plugin.yml
	 *
	 * @return The name of the author as a String.
	 */
	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	/**
	 * The placeholder identifier should go here.
	 * <br>This is what tells PlaceholderAPI to call our onRequest
	 * method to obtain a value if a placeholder starts with our
	 * identifier.
	 * <br>This must be unique and can not contain % or _
	 *
	 * @return The identifier in {@code %<identifier>_<value>%} as String.
	 */
	@Override
	public String getIdentifier() {
		return "rollit";
	}

	/**
	 * This is the version of the expansion.
	 * <br>You don't have to use numbers, since it is set as a String.
	 *
	 * For convienience do we return the version from the plugin.yml
	 *
	 * @return The version as a String.
	 */
	@Override
	public String getVersion(){
		return plugin.getDescription().getVersion();
	}

	/**
	 * This is the method called when a placeholder with our identifier
	 * is found and needs a value.
	 * <br>We specify the value identifier in this method.
	 * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
	 *
	 * @param  player
	 *         A {@link Player Player}.
	 * @param  identifier
	 *         A String containing the identifier/value.
	 *
	 * @return possibly-null String of the requested identifier.
	 */
	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		if (player != null) {
			if (StoredUserData.needsToRefresh(player.getUniqueId())) {
				RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
				if (provider != null) {
					LuckPerms api = provider.getProvider();
					CompletableFuture<User> userFuture = api.getUserManager().loadUser(player.getUniqueId());
					userFuture.thenAcceptAsync(this::parseUser);
				}
			}

			if (StoredUserData.dataMap.containsKey(player.getUniqueId())) {
				StoredUserData data = StoredUserData.dataMap.get(player.getUniqueId());
				if (identifier.equalsIgnoreCase("prefix")) {
					return data.prefix;
				} else if (identifier.equalsIgnoreCase("group_weight")) {
					return data.weight;
				}
			}
		}
		return null;
	}

	private void parseUser(User user) {
		if (user != null) {
			// Get Weight
			int weight = 0;
			Map<String, Boolean> permissionsMap = user.getCachedData().getPermissionData(QueryOptions.defaultContextualOptions()).getPermissionMap();
			for (String key : permissionsMap.keySet()) {
				if (key.startsWith("rollit.prefixgroup") && permissionsMap.get(key)) {
					String[] split = key.replace(".", " ").split(" ");
					try {
						String data = split[split.length - 1];
						int value = Integer.parseInt(data);
						if (value > weight) {
							weight = value;
						}
					} catch (Exception e) {
						plugin.getLogger().warning("Node failed to parse for prefix group: " + key);
						e.printStackTrace();
					}
				}
			}
			if (weight >= 100) {
				weight -= 100;
			}

			// Get Prefix
			String prefix = "";
			String finalKey = user.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix();
			if (finalKey != null && finalKey.length() > 0) {
				prefix = org.bukkit.ChatColor.translateAlternateColorCodes('&', finalKey);
				prefix = org.bukkit.ChatColor.getLastColors(prefix);
			} else {
				prefix = "" + ChatColor.DARK_GRAY;
			}

			new StoredUserData(user.getUniqueId(), "" + weight, prefix);
		}
	}
}
