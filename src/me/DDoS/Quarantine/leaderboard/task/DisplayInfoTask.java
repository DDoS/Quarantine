package me.DDoS.Quarantine.leaderboard.task;

import java.util.Queue;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import me.DDoS.Quarantine.leaderboard.result.Result;

/**
 *
 * @author DDoS
 */
public class DisplayInfoTask implements Runnable {

    private final Leaderboard leaderboard;

    public DisplayInfoTask(Leaderboard leaderboard) {
        
        this.leaderboard = leaderboard;
    
    }
    
    @Override
    public void run() {
        
        final Queue<Result> queue = leaderboard.getResults();
        
        while (!queue.isEmpty()) {

            queue.poll().display();

        }
    }
}
