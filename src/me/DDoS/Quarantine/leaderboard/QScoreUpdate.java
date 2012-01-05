package me.DDoS.Quarantine.leaderboard;

/**
 *
 * @author DDoS
 */
public class QScoreUpdate {
    
    private String player;
    private int score;

    public QScoreUpdate(String player, int score) {
        
        this.player = player;       
        this.score = score;
    
    }
    
    public String getPlayer() {
        
        return player;
        
    }
    
    public int getScore() {
        
        return score;
        
    }
}
