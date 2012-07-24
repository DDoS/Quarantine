package me.DDoS.Quarantine.zone;

/**
 *
 * @author DDoS
 */
public class ZoneProperties {

	//strings
	private final String zoneName;
	//ints
	private int maxNumberOfPlayers;
	private int startingMoney;
	private int mobCheckTaskID;
	//booleans
	private boolean clearMobDrops;
	private boolean oneTimeUseKeys;
	private boolean clearMobXP;
	private boolean keepXPOnRespawn;
	private boolean keepMoneyOnDeath;
	//longs
	private long mobCheckTaskInterval;

	public ZoneProperties(String zoneName) {

		this.zoneName = zoneName;

	}

	public void setClearMobDrops(boolean clearMobDrops) {

		this.clearMobDrops = clearMobDrops;

	}

	public void setClearMobXP(boolean clearMobXP) {

		this.clearMobXP = clearMobXP;

	}

	public void setMaxNumberOfPlayers(int maxNumberOfPlayers) {

		this.maxNumberOfPlayers = maxNumberOfPlayers;

	}

	public void setMobCheckTaskInterval(long mobCheckTaskInterval) {

		this.mobCheckTaskInterval = mobCheckTaskInterval;

	}

	public void setOneTimeUseKeys(boolean oneTimeUseKeys) {

		this.oneTimeUseKeys = oneTimeUseKeys;

	}

	public void setStartingMoney(int startingMoney) {

		this.startingMoney = startingMoney;

	}

	public void setMobCheckTaskID(int mobCheckTaskID) {

		this.mobCheckTaskID = mobCheckTaskID;

	}

	public void setKeepMoneyOnDeath(boolean keepMoneyOnDeath) {

		this.keepMoneyOnDeath = keepMoneyOnDeath;

	}

	public void setKeepXPOnRespawn(boolean keepXPOnRespawn) {

		this.keepXPOnRespawn = keepXPOnRespawn;

	}

	public String getZoneName() {

		return zoneName;

	}

	public boolean oneTimeUseKeys() {

		return oneTimeUseKeys;

	}

	public boolean clearMobDrops() {

		return clearMobDrops;

	}

	public boolean clearMobXP() {

		return clearMobXP;

	}

	public int getMaxNumberOfPlayers() {

		return maxNumberOfPlayers;

	}

	public int getStartingMoney() {

		return startingMoney;

	}

	public long getMobCheckTaskInterval() {

		return mobCheckTaskInterval;

	}

	public int getMobCheckTaskID() {

		return mobCheckTaskID;

	}

	public boolean keepMoneyOnDeath() {

		return keepMoneyOnDeath;

	}

	public boolean keepXPOnRespawn() {

		return keepXPOnRespawn;

	}
}
