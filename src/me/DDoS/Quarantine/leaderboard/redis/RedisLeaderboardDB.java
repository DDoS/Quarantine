package me.DDoS.Quarantine.leaderboard.redis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisConnectionException;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import me.DDoS.Quarantine.leaderboard.LeaderboardDB;
import me.DDoS.Quarantine.leaderboard.LeaderData;

public class RedisLeaderboardDB implements LeaderboardDB {

    private final Jedis jedis;
    private final boolean hasConnection;
    private final String lbName;
    private final int pageSize;

    public RedisLeaderboardDB(String lbName, int pageSize) {

        this.lbName = lbName;
        this.pageSize = pageSize;
        this.jedis = new Jedis(Leaderboard.HOST, Leaderboard.PORT);

        boolean connected;

        try {

            jedis.connect();
            connected = true;
            Quarantine.log.info("[Quarantine] Leaderboard connection to Redis "
                    + "server for zone '" + lbName + "' was established.");

        } catch (JedisConnectionException jce) {

            connected = false;
            Quarantine.log.info("[Quarantine] Couldn't connect to Redis DB. Error: " + jce.getMessage());

        }

        hasConnection = connected;

    }

    @Override
    public boolean hasConnection() {

        return hasConnection;

    }

    @Override
    public void disconnect() {

        try {

            jedis.disconnect();

        } catch (JedisConnectionException jce) {

            Quarantine.log.info("[Quarantine] Couldn't disconnect from Redis DB. Error: " + jce.getMessage());

        }
    }

    @Override
    public int getPlayerTotal() {

        int totalMembers = 0;

        try {

            totalMembers = jedis.zcard(lbName).intValue();

        } catch (JedisConnectionException jce) {

            Quarantine.log.info("[Quarantine] Couldn't get player total from Redis DB. Error: " + jce.getMessage());

        }

        return totalMembers;

    }

    @Override
    public int getPageTotal() {

        return (int) Math.ceil((float) getPlayerTotal() / (float) pageSize);

    }

    @Override
    public void rank(String member, int score) {

        try {

            jedis.zadd(lbName, score, member);

        } catch (JedisConnectionException jce) {

            Quarantine.log.info("[Quarantine] Couldn't rank player in Redis DB. Error: " + jce.getMessage());

        }
    }

    @Override
    public void sort() {
    }

    @Override
    public int getScore(String member) {

        int score = 0;

        try {

            score = jedis.zscore(lbName, member).intValue();

        } catch (JedisConnectionException jce) {

            Quarantine.log.info("[Quarantine] Couldn't get player score from Redis DB. Error: " + jce.getMessage());

        }

        return score;

    }

    @Override
    public boolean isMember(String member) {

        boolean isMember = false;

        try {

            isMember = jedis.zscore(lbName, member) != null;

        } catch (JedisConnectionException jce) {

            Quarantine.log.info("[Quarantine] Couldn't verify for player presence in Redis DB. Error: " + jce.getMessage());

        }

        return isMember;

    }

    @Override
    public int getRank(String member) {

        int rank = 0;

        try {

            rank = jedis.zrevrank(lbName, member).intValue() + 1;

        } catch (JedisConnectionException jce) {

            Quarantine.log.info("[Quarantine] Couldn't get player rank from Redis DB. Error: " + jce.getMessage());

        }

        return rank;

    }

    @Override
    public List<LeaderData> getLeaders(int startingPage, int numberOfPages) {

        if (startingPage + numberOfPages > getPageTotal()) {

            return new LinkedList<LeaderData>();

        }

        int start = ((startingPage - 1) * pageSize);

        if (start < 0) {

            start = 0;

        }

        int end = (startingPage + numberOfPages - 1) * pageSize - 1;

        Set<Tuple> rawLeaderData = null;

        try {

            rawLeaderData = jedis.zrevrangeWithScores(lbName, start, end);

        } catch (JedisConnectionException jce) {

            Quarantine.log.info("[Quarantine] Couldn't get top players from Redis DB. Error: " + jce.getMessage());

        }

        return generateLeaderData(rawLeaderData);

    }

    private List<LeaderData> generateLeaderData(Set<Tuple> memberData) {

        final List<LeaderData> leaderData = new ArrayList<LeaderData>();

        if (memberData == null) {

            return leaderData;

        }

        for (Tuple data : memberData) {

            LeaderData ld = new LeaderData(data.getElement(), (int) data.getScore(),
                    getRank(data.getElement()));
            leaderData.add(ld);

        }


        return leaderData;

    }
}
