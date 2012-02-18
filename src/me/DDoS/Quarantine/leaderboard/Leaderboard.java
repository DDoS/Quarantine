package me.DDoS.Quarantine.leaderboard;

import me.DDoS.Quarantine.leaderboard.query.Query;
import me.DDoS.Quarantine.leaderboard.query.TopQuery;
import me.DDoS.Quarantine.leaderboard.query.RankQuery;
import me.DDoS.Quarantine.leaderboard.task.ScoreUpdateTask;
import me.DDoS.Quarantine.leaderboard.task.LeaderboardInfoTask;
import me.DDoS.Quarantine.leaderboard.redis.RedisLeaderboardDB;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.leaderboard.result.Result;
import me.DDoS.Quarantine.leaderboard.task.DisplayInfoTask;
import me.DDoS.Quarantine.player.QPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class Leaderboard {

    public static String HOST;
    public static int PORT;
    public static boolean USE = false;
    //
    private final Quarantine plugin;
    //
    private final LeaderboardDB lb;
    //
    private final Timer timer = new Timer();
    private final int displayInfoTaskID;
    //
    private final Map<QPlayer, ScoreUpdate> updates = new ConcurrentHashMap<QPlayer, ScoreUpdate>();
    private final Queue<Query> queries = new ConcurrentLinkedQueue<Query>();
    //
    private final Queue<Result> results = new ConcurrentLinkedQueue<Result>();

    public Leaderboard(Quarantine plugin, String zoneName) {

        this.plugin = plugin;
        lb = new RedisLeaderboardDB(zoneName, HOST, PORT, 5);
        timer.scheduleAtFixedRate(new ScoreUpdateTask(this), 10000L, 10000L);
        timer.scheduleAtFixedRate(new LeaderboardInfoTask(this), 500L, 500L);
        displayInfoTaskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new DisplayInfoTask(this), 15L, 10L);

    }

    public LeaderboardDB getLeaderBoard() {

        return lb;

    }

    public void queueScoreUpdate(QPlayer player) {

        updates.put(player, new ScoreUpdate(player.getPlayer().getName(), player.getScore()));

    }

    public Queue<ScoreUpdate> getUpdates() {

        final Queue<ScoreUpdate> queue = new ConcurrentLinkedQueue<ScoreUpdate>();
        queue.addAll(updates.values());
        updates.clear();
        return queue;

    }

    public Queue<Query> getInfoQueries() {

        return queries;

    }

    public void addRankQuery(Player player) {

        queries.add(new RankQuery(this, player));

    }

    public void addTopQuery(Player player, int page) {

        queries.add(new TopQuery(this, player, page));

    }

    public void addResult(Result result) {

        results.add(result);

    }

    public Queue<Result> getResults() {

        return results;

    }

    public void disconnect() {

        timer.cancel();
        lb.disconnect();
        plugin.getServer().getScheduler().cancelTask(displayInfoTaskID);

    }
}
