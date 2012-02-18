package me.DDoS.Quarantine.leaderboard;

public class LeaderData {

    private final String member;
    private final int score;
    private final int rank;

    public LeaderData(String member, int score, int rank) {

        this.member = member;
        this.score = score;
        this.rank = rank;

    }

    public String getMember() {

        return member;

    }

    public int getScore() {

        return score;

    }

    public int getRank() {

        return rank;

    }
}
