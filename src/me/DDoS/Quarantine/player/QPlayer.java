package me.DDoS.Quarantine.player;

import java.util.List;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author DDoS
 */
public abstract class QPlayer extends QPlayerData {

    protected QPlayer(Player player, QZone zone) {

        super(player, zone);

    }

    protected QPlayer(QPlayerData player) {

        super(player);

    }

    public abstract boolean isZonePlayer();

    public abstract boolean join();

    public abstract boolean enter();

    public abstract boolean leave();
    
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

        QUtil.tell(player, zone.getLB().getScoreAndRank(player.getName()));

    }

    public void tellScore() {

        QUtil.tell(player, "Score: " + score);

    }

    public void tellTopFive() {

        if (zone.getLB() == null) {

            QUtil.tell(player, "Leaderboards are not enabled.");
            return;

        }

        List<String> tops = zone.getLB().getTopFive();

        for (String top : tops) {

            QUtil.tell(player, top);

        }
    }

    public void tellMoney() {

        QUtil.tell(player, "You currently have " + money + " dollar(s).");

    }
}
