package me.DDoS.Quarantine.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.DDoS.Quarantine.zone.Zone;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.player.error.DataError;
import me.DDoS.Quarantine.player.inventory.InventoryItem;
import me.DDoS.Quarantine.util.Messages;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author DDoS
 */
public class PlayerData {

	protected final Player player;
	protected final Zone zone;
	protected int money;
	protected int health;
	protected int foodLevel;
	protected final int preGameHealth;
	protected final int preGameFoodLevel;
	protected Location lastLoc;
	protected int score;
	protected Set<String> keys;

	protected PlayerData(Player player, Zone zone) {

		this.player = player;
		this.zone = zone;
		this.preGameHealth = player.getHealth();
		this.preGameFoodLevel = player.getFoodLevel();

	}

	protected PlayerData(PlayerData player) {

		this.player = player.getPlayer();
		this.zone = player.getZone();
		this.money = player.getMoney();
		this.health = player.getHealth();
		this.foodLevel = player.getFoodLevel();
		this.preGameHealth = player.getPreGameHealth();
		this.preGameFoodLevel = player.getPreGameFoodLevel();
		this.lastLoc = player.getLastLoc();
		this.score = player.getScore();
		this.keys = player.getKeys();

	}

	public Player getPlayer() {

		return player;

	}

	public Zone getZone() {

		return zone;

	}

	public int getHealth() {

		return health;

	}

	public int getFoodLevel() {

		return foodLevel;

	}

	public Set<String> getKeys() {

		return keys;

	}

	public Location getLastLoc() {

		return lastLoc;

	}

	public int getMoney() {

		return money;

	}

	public int getPreGameHealth() {

		return preGameHealth;

	}

	public int getPreGameFoodLevel() {

		return preGameFoodLevel;

	}

	public int getScore() {

		return score;

	}

	public boolean loadData() {

		FileConfiguration config = getLoadedConfig();

		if (config == null) {

			return false;

		}

		money = config.getInt("money", zone.getProperties().getStartingMoney());
		health = config.getInt("health", 20);
		foodLevel = config.getInt("foodLevel", 20);
		score = config.getInt("score", 0);

		if (zone.getEntrance() != null) {

			Location entrance = zone.getEntrance();
			World world = Bukkit.getWorld(config.getString("lastLoc.World", entrance.getWorld().getName()));
			double x = config.getDouble("lastLoc.X", entrance.getX());
			double y = config.getDouble("lastLoc.Y", entrance.getY());
			double z = config.getDouble("lastLoc.Z", entrance.getZ());
			float yaw = (float) config.getDouble("lastLoc.Yaw", entrance.getYaw());
			float pitch = (float) config.getDouble("lastLoc.Pitch", entrance.getPitch());

			lastLoc = new Location(world, x, y, z, yaw, pitch);

			if (!zone.isInZone(lastLoc)) {

				lastLoc = zone.getEntrance();
				QUtil.tell(player, Messages.get("LastLocationOutOfBounds"));

			}

		} else {

			lastLoc = null;

		}

		List<String> keyList = config.getStringList("keys");

		if (keyList != null) {

			keys = Sets.newHashSet(keyList);

		} else {

			keys = new HashSet<String>();

		}

		return true;
	}

	public boolean saveData(boolean lastLoc) {

		FileConfiguration config = getLoadedConfig();

		if (config == null) {

			return false;

		}

		health = player.getHealth();
		foodLevel = player.getFoodLevel();

		config.set("money", money);
		config.set("health", health);
		config.set("foodLevel", foodLevel);
		config.set("score", score);
		config.set("keys", Lists.newArrayList(keys));

		if (lastLoc) {

			Location loc = player.getLocation();
			config.set("lastLoc.World", loc.getWorld().getName());
			config.set("lastLoc.X", loc.getX());
			config.set("lastLoc.Y", loc.getY());
			config.set("lastLoc.Z", loc.getZ());
			config.set("lastLoc.Yaw", loc.getYaw());
			config.set("lastLoc.Pitch", loc.getPitch());

		}

		return saveConfig(config);

	}

	public boolean setData(int money, int health, int foodLevel, int score, List<String> keys, Location lastLoc) {

		FileConfiguration config = getLoadedConfig();

		if (config == null) {

			return false;

		}

		config.set("money", money);
		config.set("health", health);
		config.set("foodLevel", foodLevel);
		config.set("score", score);
		config.set("keys", keys);
		config.set("lastLoc.World", lastLoc.getWorld().getName());
		config.set("lastLoc.X", lastLoc.getX());
		config.set("lastLoc.Y", lastLoc.getY());
		config.set("lastLoc.Z", lastLoc.getZ());
		config.set("lastLoc.Yaw", lastLoc.getYaw());
		config.set("lastLoc.Pitch", lastLoc.getPitch());

		return saveConfig(config);

	}

	public boolean resetData() {

		return setData(0, 20, 20, 0, new ArrayList<String>(), zone.getEntrance());

	}

	private FileConfiguration getLoadedConfig() {

		YamlConfiguration config = new YamlConfiguration();

		File dir = new File(zone.getPlayerDataDir() + "/" + player.getName() + ".yml");

		if (!dir.exists()) {

			try {

				dir.createNewFile();

			} catch (IOException ex) {

				logError(DataError.DATA_SAVE, ex);
				return null;

			}
		}

		try {

			config.load(dir);

		} catch (Exception ex) {

			logError(DataError.DATA_SAVE, ex);
			return null;

		}

		return config;

	}

	private boolean saveConfig(FileConfiguration config) {

		File dir = new File(zone.getPlayerDataDir() + "/" + player.getName() + ".yml");

		if (!dir.exists()) {

			try {

				dir.createNewFile();

			} catch (IOException ex) {

				logError(DataError.DATA_SAVE, ex);
				return false;

			}
		}

		try {

			config.save(dir);

		} catch (IOException ex) {

			logError(DataError.DATA_SAVE, ex);
			return false;

		}

		return true;

	}

	public void deletePlayerDataFile() {

		new File(zone.getPlayerDataDir() + "/" + player.getName() + ".yml").delete();

	}

	protected boolean hasInventory() {

		return new File(zone.getPlayerInvDir() + "/" + player.getName() + ".inv").exists();

	}

	protected boolean loadInventory() {

		File invFile = new File(zone.getPlayerInvDir() + "/" + player.getName() + ".inv");

		try {

			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(invFile));
			InventoryItem[] fromFile = (InventoryItem[]) ois.readObject();
			ois.close();

			ItemStack[] armor = new ItemStack[4];
			ItemStack[] items = new ItemStack[fromFile.length - 4];

			for (int i = 0; i < 4; i++) {

				armor[i] = fromFile[i].getItem();

			}

			for (int i = 4; i < fromFile.length; i++) {

				items[i - 4] = fromFile[i].getItem();

			}

			PlayerInventory inv = player.getInventory();
			inv.setArmorContents(armor);

			for (ItemStack stack : items) {

				if (stack != null) {

					inv.addItem(stack);

				}
			}

		} catch (ClassCastException cce) {

			QUtil.tell(player, Messages.get("InventoryNotConvertedError"));
			return false;

		} catch (Exception ex) {

			logError(DataError.INV_LOAD, ex);
			return false;

		}

		return true;

	}

	protected boolean saveInventory() {

		File invFile = new File(zone.getPlayerInvDir() + "/" + player.getName() + ".inv");

		try {

			if (invFile.exists()) {

				invFile.delete();

			}

			invFile.createNewFile();

			ItemStack[] armor = player.getInventory().getArmorContents();
			ItemStack[] items = player.getInventory().getContents();
			InventoryItem[] inv = new InventoryItem[armor.length + items.length];

			for (int i = 0; i < armor.length; i++) {

				inv[i] = new InventoryItem(armor[i]);

			}

			for (int i = 0; i < items.length; i++) {

				inv[armor.length + i] = new InventoryItem(items[i]);

			}

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(invFile));
			oos.writeObject(inv);
			oos.flush();
			oos.close();

		} catch (Exception ex) {

			logError(DataError.INV_SAVE, ex);
			return false;

		}

		clearInventory();
		return true;

	}

	public void clearInventory() {

		PlayerInventory inv = player.getInventory();
		inv.clear();
		inv.setHelmet(null);
		inv.setChestplate(null);
		inv.setLeggings(null);
		inv.setBoots(null);

	}

	public void deleteInventory() {

		File dir = new File(zone.getPlayerInvDir() + "/" + player.getName() + ".inv");

		if (dir.exists()) {

			dir.delete();

		}
	}

	private void logError(DataError error, Exception ex) {

		switch (error) {

			case DATA_LOAD:
				Quarantine.log.info("[Quarantine] Couldn't load data for player: " + player.getName());
				break;

			case DATA_SAVE:
				Quarantine.log.info("[Quarantine] Couldn't save data for player: " + player.getName());
				break;

			case INV_LOAD:
				Quarantine.log.info("[Quarantine] Couldn't restore inventory for player: " + player.getName());
				break;

			case INV_SAVE:
				Quarantine.log.info("[Quarantine] Couldn't store inventory for player: " + player.getName());

		}

		Quarantine.log.info("[Quarantine] Error message: " + ex.getMessage());

	}

	@Override
	public String toString() {

		return player.getDisplayName();

	}

	@Override
	public int hashCode() {

		return player.hashCode() ^ zone.getProperties().getZoneName().hashCode();

	}

	@Override
	public boolean equals(Object o) {

		if (o == null) {

			return false;

		}

		if (o == this) {

			return true;

		}

		if (!(o instanceof PlayerData)) {

			return false;

		}

		PlayerData p = (PlayerData) o;
		return p.getPlayer().equals(player) && p.getZone().getProperties().
				getZoneName().equals(zone.getProperties().getZoneName());

	}
}
