package me.DDoS.Quarantine.leaderboard.redis;

import me.DDoS.Quarantine.leaderboard.LeaderData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import me.DDoS.Quarantine.leaderboard.LeaderboardDB;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisLeaderboardDB implements LeaderboardDB {

    private final Jedis jedis;
    private final String lbName;
    private final int pageSize;

    public RedisLeaderboardDB(String lbName, int pageSize) {

        this.lbName = lbName;
        this.pageSize = pageSize;
        this.jedis = new Jedis(Leaderboard.HOST, Leaderboard.PORT);

        Quarantine.log.info("[Quarantine] Redis leaderboard for zone '" + lbName + "' was initialized.");

    }

    @Override
    public void disconnect() {

        try {

            jedis.disconnect();

        } catch (JedisConnectionException jce) {

            Quarantine.log.info("[Quarantine] Couldn't disconnect from Redis DB. Error: " + jce.getMessage());

        }
    }

    private long totalMembers() {

        long totalMembers = 0;

        try {

            totalMembers = jedis.zcard(lbName);

        } catch (JedisConnectionException jce) {

            Quarantine.log.info("[Quarantine] Couldn't get member total from Redis DB. Error: " + jce.getMessage());

        }

        return totalMembers;

    }

    private int totalPages() {

        return (int) Math.ceil((float) totalMembers() / (float) pageSize);

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
    public List<LeaderData> getLeaders(int pageNumber) {

        if (pageNumber > totalPages()) {

            pageNumber = totalPages();

        }

        int indexForRedis = pageNumber - 1;
        int startingOffset = indexForRedis * pageSize;

        if (startingOffset < 0) {

            startingOffset = 0;

        }

        int endingOffset = (startingOffset + pageSize) - 1;

        Set<Tuple> rawLeaderData = null;

        try {

            rawLeaderData = jedis.zrevrangeWithScores(lbName, startingOffset, endingOffset);

        } catch (JedisConnectionException jce) {

            Quarantine.log.info("[Quarantine] Couldn't get top players from Redis DB. Error: " + jce.getMessage());

        }

        return generateLeaderData(rawLeaderData);

    }

    private List<LeaderData> generateLeaderData(Set<Tuple> memberData) {

        List<LeaderData> leaderData = new ArrayList<LeaderData>();

        if (memberData == null) {

            return leaderData;

        }

        Iterator<Tuple> memberDataIterator = memberData.iterator();

        while (memberDataIterator.hasNext()) {

            Tuple memberDataTuple = memberDataIterator.next();
            LeaderData leaderDataItem = new LeaderData(memberDataTuple.getElement(), (int) memberDataTuple.getScore(),
                    getRank(memberDataTuple.getElement()));
            leaderData.add(leaderDataItem);

        }

        return leaderData;

    }
}
