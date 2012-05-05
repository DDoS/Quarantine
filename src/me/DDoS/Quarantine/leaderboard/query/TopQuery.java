package me.DDoS.Quarantine.leaderboard.query;

import me.DDoS.Quarantine.leaderboard.LeaderData;
import java.util.ArrayList;
import java.util.List;
import me.DDoS.Quarantine.gui.GUIHandler;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import me.DDoS.Quarantine.leaderboard.LeaderboardDB;
import me.DDoS.Quarantine.leaderboard.result.TopResult;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class TopQuery implements Query {

    private final Leaderboard lb;
    private final GUIHandler guiHandler;
    private final Player player;
    private final int page;
    private final int numberOfPages;

    public TopQuery(Leaderboard lb, GUIHandler guiHandler, Player player, int page, int numberOfPages) {

        this.lb = lb;
        this.guiHandler = guiHandler;
        this.player = player;
        this.page = page;
        this.numberOfPages = numberOfPages;

    }

    @Override
    public void execute() {

        List<String> top = new ArrayList();
        List<LeaderData> lds = lb.getLeaderBoardDB().getLeaders(page, numberOfPages);

        if (lds.isEmpty()) {

            top.add(ChatColor.YELLOW + "No results to list.");

        } else {

            for (LeaderData ld : lds) {

                top.add(ChatColor.AQUA.toString() + ld.getRank()
                        + ChatColor.YELLOW + " | "
                        + ChatColor.RESET + ld.getMember()
                        + ChatColor.YELLOW + " - " 
                        + ChatColor.AQUA + ld.getScore());

            }
        }

        lb.addResult(new TopResult(player, guiHandler, top));

    }
}
