package net.lostfables.lughgk.rollit.placeholders;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import net.lostfables.lughgk.rollit.Rollit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class StoredUserData {

	private static final int MAX_TIME_PER_UPDATE = 60000;
	protected static HashMap<UUID, StoredUserData> dataMap = new HashMap<>();

	static {
		new BukkitRunnable() {
			@Override
			public void run() {
				long currentTime = System.currentTimeMillis();
				ArrayList<UUID> keysForRemoval = new ArrayList<>();
				for (UUID key : dataMap.keySet()) {
					long timeSinceUpdate = currentTime - dataMap.get(key).lastUpdated;
					if (timeSinceUpdate > MAX_TIME_PER_UPDATE) {
						keysForRemoval.add(key);
					}
				}
				for(UUID key : keysForRemoval) {
					dataMap.remove(key);
				}
			}
		}.runTaskTimerAsynchronously(Rollit.get(), 0, 1200);
	}

	protected String weight;
	protected String prefix;
	protected long lastUpdated = System.currentTimeMillis();

	protected StoredUserData(UUID uuid, String weight, String prefix) {
		this.weight = weight;
		this.prefix = prefix;
		dataMap.put(uuid, this);
	}

}
