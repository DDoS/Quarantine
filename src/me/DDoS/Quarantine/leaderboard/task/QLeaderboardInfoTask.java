package me.DDoS.Quarantine.leaderboard.task;

import java.util.Queue;
import java.util.TimerTask;
import me.DDoS.Quarantine.leaderboard.QLeaderboard;
import me.DDoS.Quarantine.leaderboard.query.QQuery;

/**
 *
 * @author DDoS
 */
public class QLeaderboardInfoTask extends TimerTask {

    private final QLeaderboard leaderboard;

    public QLeaderboardInfoTask(QLeaderboard leaderboard) {

        this.leaderboard = leaderboard;

    }

    @Override
    public void run() {

        final Queue<QQuery> queue = leaderboard.getLeaderboardInfoQueries();

        while (!queue.isEmpty()) {

            queue.poll().execute();

        }
    }
}
