package me.DDoS.Quarantine.gui;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.player.QPlayer;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QTextGUIHandler implements QGUIHandler {

    private Quarantine plugin;

    public QTextGUIHandler(Quarantine plugin) {

        this.plugin = plugin;

    }

    @Override
    public void handleZoneList(Player player) {

        QUtil.tell(player, "Zones:");

        for (QZone zone : plugin.getZones()) {

            QUtil.tell(player, zone.getName()
                    + ": " + zone.getNumOfPlayers()
                    + "/" + zone.getMaxNumOfPlayers());

        }
    }

    @Override
    public void handlePlayerList(Player player, QZone zone) {

        String playerList = "";
        QUtil.tell(player, "Players:");

        for (QPlayer p : zone.getPlayers()) {

            playerList = playerList + p.getPlayer().getDisplayName() + ", ";

        }

        try {

            playerList = playerList.substring(0, playerList.length() - 2);

        } catch (StringIndexOutOfBoundsException sioobe) {

            return;

        }

        QUtil.tell(player, playerList);

    }
}
