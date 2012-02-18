package me.DDoS.Quarantine.leaderboard.redis;

public class LeaderData {

    private final String member;
    private final Double score;
    private final Long rank;

    public LeaderData(String member, double score, long rank) {

        this.member = member;
        this.score = score;
        this.rank = rank;

    }

    public String getMember() {

        return member;

    }

    public double getScore() {

        return score;

    }

    public long getRank() {

        return rank;

    }
}
