package me.DDoS.Quarantine.gui;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.player.QPlayer;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.ChatColor;
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
public class QGUIHandler {

    private final Quarantine plugin;

    public QGUIHandler(Quarantine plugin) {

        this.plugin = plugin;

    }

    public void handleZoneList(Player player) {

        if (SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
            
            displaySpoutZoneList(player);
            
        } else {

            displayTextZoneList(player);

        }
    }

    public void handlePlayerList(Player player, QZone zone) {

        if (SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {

            displaySpoutPlayerList(player, zone);

        } else {

            displayTextPlayerList(player, zone);

        }
    }
    
    private void displayTextZoneList(Player player) {
        
        QUtil.tell(player, "Zones:");
        
        for (QZone zone : plugin.getZones()) {
            
            QUtil.tell(player, zone.getName()
                    + ": " + zone.getNumOfPlayers()
                    + "/" + zone.getMaxNumOfPlayers());
            
        }    
    }

    private void displaySpoutZoneList(Player player) {

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

    private void displayTextPlayerList(Player player, QZone zone) {

        QUtil.tell(player, "Players:");

        for (QPlayer p : zone.getPlayers()) {

            QUtil.tell(player, p.getPlayer().getDisplayName() + ChatColor.GRAY + ", score: " + p.getScore());

        }
    }

    private void displaySpoutPlayerList(Player player, QZone zone) {

        PopupScreen popup = new GenericPopup();

        Label top = new GenericLabel();
        top.setAnchor(WidgetAnchor.SCALE);
        top.setWidth(100).setHeight(10);
        top.setText("Players");
        top.setX(100).setY(10);
        popup.attachWidget(plugin, top);

        Label notice = new GenericLabel();
        notice.setAnchor(WidgetAnchor.SCALE);
        notice.setWidth(100).setHeight(10);
        notice.setScale(0.50F);
        notice.setText("Use the escape key to close this popup.");
        notice.setX(100).setY(21);
        popup.attachWidget(plugin, notice);

        Label legend = new GenericLabel();
        legend.setAnchor(WidgetAnchor.SCALE);
        legend.setWidth(100).setHeight(10);
        legend.setText("Name | Score");
        legend.setX(100).setY(32);
        popup.attachWidget(plugin, legend);

        int i = 43;

        for (QPlayer p : zone.getPlayers()) {

            Label zoneLabel = new GenericLabel();
            zoneLabel.setAnchor(WidgetAnchor.SCALE);
            zoneLabel.setWidth(100).setHeight(10);
            zoneLabel.setText(p.getPlayer().getDisplayName() + ", " + p.getScore());
            zoneLabel.setX(100).setY(i);
            popup.attachWidget(plugin, zoneLabel);
            i += 11;

        }

        popup.setTransparent(false);
        SpoutManager.getPlayer(player).getMainScreen().attachPopupScreen(popup);

    }
}
