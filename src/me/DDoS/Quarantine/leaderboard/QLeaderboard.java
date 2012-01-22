package me.DDoS.Quarantine.leaderboard;

import me.DDoS.Quarantine.leaderboard.query.QQuery;
import me.DDoS.Quarantine.leaderboard.query.QTopQuery;
import me.DDoS.Quarantine.leaderboard.query.QRankQuery;
import me.DDoS.Quarantine.leaderboard.task.QScoreUpdateTask;
import me.DDoS.Quarantine.leaderboard.task.QLeaderboardInfoTask;
import com.agoragames.leaderboard.Leaderboard;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.leaderboard.result.QResult;
import me.DDoS.Quarantine.leaderboard.task.QDisplayInfoTask;
import me.DDoS.Quarantine.player.QPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QLeaderboard {

    public static String HOST;
    public static int PORT;
    public static boolean USE = false;
    //
    private final Quarantine plugin;
    //
    private final Leaderboard lb;
    //
    private final Timer timer = new Timer();
    private final int displayInfoTaskID;
    //
    private final Map<QPlayer, QScoreUpdate> updates = new ConcurrentHashMap<QPlayer, QScoreUpdate>();
    private final Queue<QQuery> queries = new ConcurrentLinkedQueue<QQuery>();
    //
    private final Queue<QResult> results = new ConcurrentLinkedQueue<QResult>();

    public QLeaderboard(Quarantine plugin, String zoneName) {

        this.plugin = plugin;
        lb = new Leaderboard(zoneName, HOST, PORT, 5);
        timer.scheduleAtFixedRate(new QScoreUpdateTask(this), 10000L, 10000L);
        timer.scheduleAtFixedRate(new QLeaderboardInfoTask(this), 500L, 500L);
        displayInfoTaskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new QDisplayInfoTask(this), 15L, 10L);

    }

    public Leaderboard getLeaderBoard() {

        return lb;

    }

    public void queueScoreUpdate(QPlayer player) {

        updates.put(player, new QScoreUpdate(player.getPlayer().getName(), player.getScore()));

    }

    public Queue<QScoreUpdate> getUpdates() {

        final Queue<QScoreUpdate> queue = new ConcurrentLinkedQueue<QScoreUpdate>();
        queue.addAll(updates.values());
        updates.clear();
        return queue;

    }

    public Queue<QQuery> getInfoQueries() {

        return queries;

    }

    public void addRankQuery(Player player) {

        queries.add(new QRankQuery(this, player));

    }

    public void addTopQuery(Player player) {

        queries.add(new QTopQuery(this, player));

    }

    public void addResult(QResult result) {

        results.add(result);

    }

    public Queue<QResult> getResults() {

        return results;

    }

    public void disconnect() {

        timer.cancel();
        lb.disconnect();
        plugin.getServer().getScheduler().cancelTask(displayInfoTaskID);

    }
}
