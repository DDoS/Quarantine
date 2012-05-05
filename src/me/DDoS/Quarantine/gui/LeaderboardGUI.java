package me.DDoS.Quarantine.gui;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;
import org.bukkit.entity.Player;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.TextField;

/**
 *
 * @author DDoS
 */
public class LeaderboardGUI {

    private final Player player;
    private final Quarantine plugin;
    private final Leaderboard leaderboard;
    private final PopupScreen popup = new GenericPopup();
    //
    private int currentPage;
    private final List<String> currentLeaders = new ArrayList<String>();
    private final Label pageNumber = new GenericLabel();
    private UUID textFieldID;

    public LeaderboardGUI(Player player, Quarantine plugin, Leaderboard leaderboard, int currentPage) {

        this.player = player;
        this.plugin = plugin;
        this.leaderboard = leaderboard;
        this.currentPage = currentPage;

    }

    public void open() {

        Label top = new GenericLabel();
        top.setAnchor(WidgetAnchor.SCALE);
        top.setWidth(100).setHeight(10);
        top.setText("Leaderboard");
        top.setX(150).setY(10);
        popup.attachWidget(plugin, top);

        Label legend = new GenericLabel();
        legend.setAnchor(WidgetAnchor.SCALE);
        legend.setWidth(100).setHeight(10);
        legend.setText("Rank | Name | Score");
        legend.setX(150).setY(32);
        popup.attachWidget(plugin, legend);

        Label notice = new GenericLabel();
        notice.setAnchor(WidgetAnchor.SCALE);
        notice.setWidth(100).setHeight(10);
        notice.setScale(0.50F);
        notice.setText("Use the escape key to close this popup.");
        notice.setX(150).setY(21);
        popup.attachWidget(plugin, notice);

        pageNumber.setAnchor(WidgetAnchor.SCALE);
        pageNumber.setWidth(100).setHeight(10);
        updatePageNumber();
        pageNumber.setX(150).setY(200);
        popup.attachWidget(plugin, pageNumber);

        Button up = new GenericButton("/\\");
        up.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        up.setHoverColor(new Color(1.0F, 1.0F, 0, 1.0F));
        up.setX(80).setY(20);
        up.setWidth(20).setHeight(20);
        popup.attachWidget(plugin, up);

        Button down = new GenericButton("\\/");
        down.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        down.setHoverColor(new Color(1.0F, 1.0F, 0, 1.0F));
        down.setX(80).setY(45);
        down.setWidth(20).setHeight(20);
        popup.attachWidget(plugin, down);

        TextField field = new GenericTextField();
        field.setMaximumCharacters(12);
        field.setFieldColor(new Color(0, 0, 0, 1.0F));
        field.setBorderColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        field.setX(49).setY(70);
        field.setWidth(76).setHeight(20);
        textFieldID = field.getId();
        popup.attachWidget(plugin, field);

        Button page = new GenericButton("Jump to page");
        page.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        page.setHoverColor(new Color(1.0F, 1.0F, 0, 1.0F));
        page.setX(49).setY(95);
        page.setWidth(76).setHeight(20);
        popup.attachWidget(plugin, page);

        popup.setTransparent(false);

        SpoutManager.getPlayer(player).getMainScreen().attachPopupScreen(popup);

    }

    private void displayLeaders() {

        for (Widget widget : popup.getAttachedWidgets()) {

            if (widget instanceof GenericLabel) {

                if ((widget.getY() - 43) % 15 == 0) {

                    popup.removeWidget(widget);

                }
            }
        }

        int index = 43;

        for (String leader : currentLeaders) {

            Label leaderLabel = new GenericLabel();
            leaderLabel.setAnchor(WidgetAnchor.SCALE);
            leaderLabel.setWidth(100).setHeight(10);
            leaderLabel.setText(leader);
            leaderLabel.setX(150).setY(index);
            popup.attachWidget(plugin, leaderLabel);
            index += 15;

        }

        popup.setDirty(true);

    }

    public void addLeaders(List<String> leaders) {

        if (leaders.isEmpty()) {

            return;

        }

        currentLeaders.clear();
        currentLeaders.addAll(leaders);
        updatePageNumber();
        displayLeaders();

    }

    public void scrollUp() {

        if (currentPage < 2) {

            return;

        }

        leaderboard.addTopQuery(player, plugin.getGUIHandler(), --currentPage, 2);

    }

    public void scrollDown() {

        leaderboard.addTopQuery(player, plugin.getGUIHandler(), ++currentPage, 2);

    }

    public void jumpToPage(int page) {

        page = page * 2 - 1;

        if (page > leaderboard.getNumberOfPages()) {

            page = leaderboard.getNumberOfPages();

        }

        leaderboard.addTopQuery(player, plugin.getGUIHandler(), (currentPage = page), 2);

    }

    public UUID getTextFieldID() {

        return textFieldID;

    }

    private void updatePageNumber() {

        pageNumber.setText("page " + (currentPage / 2 + 1) + " of "
                + (leaderboard.getNumberOfPages() / 2 + 1));
        pageNumber.setDirty(true);

    }
}
