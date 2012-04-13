package me.DDoS.Quarantine.zone;

/**
 *
 * @author DDoS
 */
public class ZoneProperties {

    /*
     * Final fields
     */
    //strings
    private final String zoneName;
    //ints
    private final int maxNumberOfPlayers;
    private final int startingMoney;
    //booleans
    private final boolean clearDrops;
    private final boolean oneTimeUseKeys;
    //longs
    private final long mobCheckTaskInterval;

    /*
     * Non-final fields
     */
    //ints
    private int mobCheckTaskID;
    
    public ZoneProperties(String zoneName,
            int maxNumberOfPlayers, int startingMoney,
            boolean clearDrops, boolean oneTimeUseKeys,
            long mobCheckTaskInterval) {

        this.zoneName = zoneName;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.startingMoney = startingMoney;
        this.clearDrops = clearDrops;
        this.oneTimeUseKeys = oneTimeUseKeys;
        this.mobCheckTaskInterval = mobCheckTaskInterval;

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

    public void setMobCheckTaskID(int mobCheckTaskID) {
    
        this.mobCheckTaskID = mobCheckTaskID;
    
    }
}
