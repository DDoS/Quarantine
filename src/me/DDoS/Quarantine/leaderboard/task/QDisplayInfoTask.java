package me.DDoS.Quarantine.leaderboard.task;

import java.util.Queue;
import me.DDoS.Quarantine.leaderboard.QLeaderboard;
import me.DDoS.Quarantine.leaderboard.result.QResult;

/**
 *
 * @author DDoS
 */
public class QDisplayInfoTask implements Runnable {

    private final QLeaderboard leaderboard;

    public QDisplayInfoTask(QLeaderboard leaderboard) {
        
        this.leaderboard = leaderboard;
    
    }
    
    @Override
    public void run() {
        
        final Queue<QResult> queue = leaderboard.getResults();
        
        while (!queue.isEmpty()) {

            queue.poll().display();

        }
    }
}
