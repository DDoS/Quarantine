package me.DDoS.Quarantine.gui;

import java.util.List;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.player.QPlayer;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class TextGUIHandler implements GUIHandler {

    protected final Quarantine plugin;

    public TextGUIHandler(Quarantine plugin) {

        this.plugin = plugin;

    }

    @Override
    public void handleZoneList(Player player) {

        QUtil.tell(player, "Zones:");

        for (Zone zone : plugin.getZones()) {

            QUtil.tell(player, zone.getProperties().getZoneName()
                    + ": " + zone.getNumberOfPlayers()
                    + "/" + zone.getProperties().getMaxNumberOfPlayers());

        }
    }

    @Override
    public void handlePlayerList(Player player, Zone zone) {

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
    
    @Override
    public void handleTopResults(Player player, List<String> results) {
       
        for (String result : results) {

            QUtil.tell(player, result);

        }        
    }   
}
