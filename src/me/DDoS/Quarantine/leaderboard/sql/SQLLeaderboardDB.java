package me.DDoS.Quarantine.leaderboard.sql;

import java.util.List;
import me.DDoS.Quarantine.leaderboard.LeaderData;
import me.DDoS.Quarantine.leaderboard.LeaderboardDB;

/**
 *
 * @author DDoS
 */
public class SQLLeaderboardDB implements LeaderboardDB {

    @Override
    public void disconnect() {
        
        throw new UnsupportedOperationException("Not supported yet.");
    
    }

    @Override
    public int rank(String member, double score) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    
    }

    @Override
    public int getScore(String member) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    
    }

    @Override
    public boolean isMember(String member) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    
    }

    @Override
    public int getRank(String member) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    
    }

    @Override
    public List<LeaderData> getLeaders(int currentPage) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    
    }   
}
