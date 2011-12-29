package me.DDoS.Quarantine.gui;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;

/**
 *
 * @author DDoS
 */
public class QGUIHandler {

    private Quarantine plugin;

    public QGUIHandler(Quarantine plugin) {

        this.plugin = plugin;

    }

    public void handleZoneList(Player player) {

        if (SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {

            new QSpoutZoneList(plugin).display(player);

        } else {

            new QTextZoneList(plugin).display(player);

        }
    }
    
    public void handlePlayerList(Player player, QZone zone) {

        if (SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {

            new QSpoutPlayerList(plugin).display(player, zone);

        } else {

            new QTextPlayerList().display(player, zone);

        }
    }
}
