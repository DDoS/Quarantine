package me.DDoS.Quarantine.gui;

import java.util.List;

import org.bukkit.entity.Player;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.util.Messages;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;

/**
 *
 * @author DDoS
 */
public class GUIHandler {

    protected final Quarantine plugin;

    public GUIHandler(Quarantine plugin) {

        this.plugin = plugin;

    }

    public void handleZoneList(Player player) {

        QUtil.tell(player, Messages.get("ZoneListHeader"));

        for (Zone zone : plugin.getZones()) {

            QUtil.tell(player, zone.getProperties().getZoneName()
                    + ": " + zone.getNumberOfPlayers()
                    + "/" + zone.getProperties().getMaxNumberOfPlayers());

        }
    }

    public void handlePlayerList(Player player, Zone zone) {

        QUtil.tell(player, Messages.get("PlayerListHeader"));
        QUtil.tell(player, QUtil.toString(zone.getPlayers()));

    }

    public void handleTopResults(Player player, List<String> results) {

        for (String result : results) {

            QUtil.tell(player, result);

        }
    }
}
