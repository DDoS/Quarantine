package me.DDoS.Quarantine.player;

import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
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

    public abstract void dieLeave(PlayerDeathEvent event);
    
    public abstract PlayerType getPlayerType();
    
    public void removeMoney(int amount) {

        money -= amount;
        QUtil.tell(player, amount + " dollar(s) have been substracted from your account balance.");
        tellMoney();

    }
    
    public void giveMoney(int amount) {

        money += amount;
        QUtil.tell(player, amount + " dollar(s) have been added to your account balance.");
        tellMoney();

    }
    
    public void tellKits() {
        
        QUtil.tell(player, "All available kits:");
        QUtil.tell(player, QUtil.toString(zone.getKitNames()));
        
    }
    
    public void tellKeys() {

        QUtil.tell(player, "Your keys: " + QUtil.toString(keys));

    }

    public void tellRank() {

        if (zone.getLeaderboards() == null) {

            QUtil.tell(player, "Leaderboards are not enabled.");
            return;

        }

        zone.getLeaderboards().addRankQuery(player);

    }

    public void tellScore() {

        QUtil.tell(player, "Score: " + score);

    }

    public void tellTopFive(int page) {

        if (zone.getLeaderboards() == null) {

            QUtil.tell(player, "Leaderboards are not enabled.");
            return;

        }

        zone.getLeaderboards().addTopQuery(player, page);
        
    }

    public void tellMoney() {

        QUtil.tell(player, "You currently have " + money + " dollar(s).");

    }
}
