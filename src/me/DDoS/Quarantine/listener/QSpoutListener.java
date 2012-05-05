package me.DDoS.Quarantine.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.gui.TextField;


import me.DDoS.Quarantine.gui.LeaderboardGUI;
import me.DDoS.Quarantine.gui.SpoutEnabledGUIHandler;

/**
 *
 * @author DDoS
 */
public class QSpoutListener implements Listener {

    private final SpoutEnabledGUIHandler guiHandler;

    public QSpoutListener(SpoutEnabledGUIHandler guiHandler) {

        this.guiHandler = guiHandler;

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onButtonClick(ButtonClickEvent event) {

        if (!guiHandler.hasLeaderboardGUI(event.getPlayer())) {

            return;

        }

        LeaderboardGUI lbGUI = guiHandler.getLeaderboardGUI(event.getPlayer());
        String text = event.getButton().getText();

        if (text.equals("/\\")) {

            lbGUI.scrollUp();

        } else if (text.equals("\\/")) {

            lbGUI.scrollDown();

        } else if (text.equals("Jump to page")) {

            try {

                lbGUI.jumpToPage(Integer.parseInt(
                        ((TextField) (event.getScreen().getWidget(
                        lbGUI.getTextFieldID()))).getText()));

            } catch (NumberFormatException nfe) {
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onScreenClose(ScreenCloseEvent event) {

        if (event.getScreenType() != ScreenType.CUSTOM_SCREEN) {

            return;

        }

        if (guiHandler.hasLeaderboardGUI(event.getPlayer())) {

            guiHandler.removeLeaderboardGUI(event.getPlayer());

        }
    }
}
