package me.DDoS.Quarantine.gui;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.WidgetAnchor;

/**
 *
 * @author DDoS
 */
public class QSpoutZoneList {

    private Quarantine plugin;

    public QSpoutZoneList(Quarantine plugin) {

        this.plugin = plugin;

    }

    public void display(Player player) {

        PopupScreen popup = new GenericPopup();
        
        Label top = new GenericLabel();
        top.setAnchor(WidgetAnchor.SCALE);
        top.setWidth(100).setHeight(10);
        top.setText("Zones");
        top.setX(100).setY(10);
        popup.attachWidget(plugin, top);
        
        Label notice = new GenericLabel();
        notice.setAnchor(WidgetAnchor.SCALE);
        notice.setWidth(100).setHeight(10);
        notice.setScale(0.50F);
        notice.setText("Use the escape key to close this popup.");
        notice.setX(100).setY(21);
        popup.attachWidget(plugin, notice);
        
        int i = 32;

        for (QZone zone : plugin.getZones()) {

            Label zoneLabel = new GenericLabel();
            zoneLabel.setAnchor(WidgetAnchor.SCALE);
            zoneLabel.setWidth(100).setHeight(10);
            zoneLabel.setText(zone.getName() + ": " + zone.getNumOfPlayers() + "/" + zone.getMaxNumOfPlayers());
            zoneLabel.setX(100).setY(i);
            popup.attachWidget(plugin, zoneLabel);
            i += 11;

        }

        popup.setTransparent(false);
        SpoutManager.getPlayer(player).getMainScreen().attachPopupScreen(popup);

    }
}
