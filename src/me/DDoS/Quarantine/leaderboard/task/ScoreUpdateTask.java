package me.DDoS.Quarantine.leaderboard.task;

import java.util.Queue;
import java.util.TimerTask;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import me.DDoS.Quarantine.leaderboard.LeaderboardDB;
import me.DDoS.Quarantine.leaderboard.ScoreUpdate;

/**
 *
 * @author DDoS
 */
public class ScoreUpdateTask extends TimerTask {

    private final Leaderboard leaderboard;

    public ScoreUpdateTask(Leaderboard leaderboard) {

        this.leaderboard = leaderboard;

    }

    @Override
    public void run() {

        final LeaderboardDB lb = leaderboard.getLeaderBoardDB();
        
        if (!lb.hasConnection()) {
            
            return;
            
        }
        
        leaderboard.setNumberOfPages(lb.getPageTotal());
        final Queue<ScoreUpdate> queue = leaderboard.getUpdates();

        if (queue.isEmpty()) {
            
            return;
            
        }
        
        while (!queue.isEmpty()) {

            ScoreUpdate update = queue.poll();
            lb.rank(update.getPlayer(), update.getScore());

        }

        lb.sort();
        
    }
}
