package me.DDoS.Quarantine.gui;

import java.util.List;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.player.QPlayer;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;
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
public class SpoutEnabledGUIHandler extends TextGUIHandler implements GUIHandler {

    public SpoutEnabledGUIHandler(Quarantine plugin) {
        
        super(plugin);
    
    }
    
    @Override
    public void handleZoneList(Player player) {

        if (SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
            
            displaySpoutZoneList(player);
            
        } else {

            super.handleZoneList(player);

        }
    }

    @Override
    public void handlePlayerList(Player player, Zone zone) {

        if (SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {

            displaySpoutPlayerList(player, zone);

        } else {

            super.handlePlayerList(player, zone);

        }
    }
    
    @Override
    public void handleTopResults(Player player, List<String> results) {
        
        if (SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {

            displaySpoutTopResults(player, results);

        } else {

            super.handleTopResults(player, results);

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

        attachNotice(popup);

        int i = 32;

        for (Zone zone : plugin.getZones()) {

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

    private void displaySpoutPlayerList(Player player, Zone zone) {

        PopupScreen popup = new GenericPopup();

        Label top = new GenericLabel();
        top.setAnchor(WidgetAnchor.SCALE);
        top.setWidth(100).setHeight(10);
        top.setText("Players");
        top.setX(100).setY(10);
        popup.attachWidget(plugin, top);

        attachNotice(popup);

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
    
    private void displaySpoutTopResults(Player player, List<String> results) {
       
        PopupScreen popup = new GenericPopup();

        Label top = new GenericLabel();
        top.setAnchor(WidgetAnchor.SCALE);
        top.setWidth(100).setHeight(10);
        top.setText("Leaderboard");
        top.setX(100).setY(10);
        popup.attachWidget(plugin, top);

        attachNotice(popup);

        Label legend = new GenericLabel();
        legend.setAnchor(WidgetAnchor.SCALE);
        legend.setWidth(100).setHeight(10);
        legend.setText("Rank | Name | Score");
        legend.setX(100).setY(32);
        popup.attachWidget(plugin, legend);

        int i = 43;

        for (String result : results) {

            Label zoneLabel = new GenericLabel();
            zoneLabel.setAnchor(WidgetAnchor.SCALE);
            zoneLabel.setWidth(100).setHeight(10);
            zoneLabel.setText(result);
            zoneLabel.setX(100).setY(i);
            popup.attachWidget(plugin, zoneLabel);
            i += 11;

        }

        popup.setTransparent(false);
        SpoutManager.getPlayer(player).getMainScreen().attachPopupScreen(popup);
        
    }
    
    private void attachNotice(PopupScreen popup) {
        
        Label notice = new GenericLabel();
        notice.setAnchor(WidgetAnchor.SCALE);
        notice.setWidth(100).setHeight(10);
        notice.setScale(0.50F);
        notice.setText("Use the escape key to close this popup.");
        notice.setX(100).setY(21);
        popup.attachWidget(plugin, notice);
        
    }
}
