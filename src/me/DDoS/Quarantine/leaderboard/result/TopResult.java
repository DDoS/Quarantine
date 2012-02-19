package me.DDoS.Quarantine.leaderboard.result;

import java.util.List;
import me.DDoS.Quarantine.gui.GUIHandler;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class TopResult implements Result {

    private final Player player;
    private final GUIHandler guiHandler;
    private final List<String> results;

    public TopResult(Player player, GUIHandler guiHandler, List<String> results) {

        this.player = player;
        this.guiHandler = guiHandler;
        this.results = results;

    }

    @Override
    public void display() {

        guiHandler.handleTopResults(player, results);

    }
}
