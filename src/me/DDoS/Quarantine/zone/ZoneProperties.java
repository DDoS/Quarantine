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
    private boolean clearDrops;
    private boolean oneTimeUseKeys;
    private boolean clearXP;
    //longs
    private long mobCheckTaskInterval;

    public ZoneProperties(String zoneName) {

        this.zoneName = zoneName;

    }

    public void clearDrops(boolean clearDrops) {

        this.clearDrops = clearDrops;

    }

    public void clearXP(boolean clearXP) {

        this.clearXP = clearXP;

    }

    public void setMaxNumberOfPlayers(int maxNumberOfPlayers) {

        this.maxNumberOfPlayers = maxNumberOfPlayers;

    }

    public void setMobCheckTaskInterval(long mobCheckTaskInterval) {

        this.mobCheckTaskInterval = mobCheckTaskInterval;

    }

    public void oneTimeUseKeys(boolean oneTimeUseKeys) {

        this.oneTimeUseKeys = oneTimeUseKeys;

    }

    public void setStartingMoney(int startingMoney) {

        this.startingMoney = startingMoney;

    }
    
    public void setMobCheckTaskID(int mobCheckTaskID) {

        this.mobCheckTaskID = mobCheckTaskID;

    }

    public String getZoneName() {

        return zoneName;

    }

    public boolean oneTimeUseKeys() {

        return oneTimeUseKeys;

    }

    public boolean clearDrops() {

        return clearDrops;

    }

    public boolean clearXP() {

        return clearXP;

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
}
