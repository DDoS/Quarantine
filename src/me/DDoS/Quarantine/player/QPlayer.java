package me.DDoS.Quarantine.player;

import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author DDoS
 */
public abstract class QPlayer extends PlayerData {

    protected QPlayer(Player player, Zone zone) {

        super(player, zone);

    }

    protected QPlayer(PlayerData player) {

        super(player);

    }

    public abstract boolean isZonePlayer();

    public abstract boolean join();

    public abstract boolean enter();

    public abstract boolean commandLeave();
    
    public abstract void quitLeave();

    public abstract void forceLeave();

    public abstract boolean teleportLeave(PlayerTeleportEvent event);

    public abstract void dieLeave(EntityDeathEvent event);

    public void tellKeys() {

        QUtil.tell(player, "Your keys: " + keysToString());

    }

    private String keysToString() {

        String finalString = "";

        for (String key : keys) {

            finalString = finalString + key + ", ";

        }

        try {

            finalString = finalString.substring(0, finalString.length() - 2);

        } catch (StringIndexOutOfBoundsException sioobe) {

            return "";

        }

        return finalString;

    }

    public void tellRank() {

        if (zone.getLB() == null) {

            QUtil.tell(player, "Leaderboards are not enabled.");
            return;

        }

        zone.getLB().addRankQuery(player);

    }

    public void tellScore() {

        QUtil.tell(player, "Score: " + score);

    }

    public void tellTopFive(int page) {

        if (zone.getLB() == null) {

            QUtil.tell(player, "Leaderboards are not enabled.");
            return;

        }

        zone.getLB().addTopQuery(player, page);
        
    }

    public void tellMoney() {

        QUtil.tell(player, "You currently have " + money + " dollar(s).");

    }
}
