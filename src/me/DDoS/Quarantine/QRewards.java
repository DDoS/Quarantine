package me.DDoS.Quarantine;

import java.util.Random;

/**
 *
 * @author DDoS
 */
public class QRewards {
    
    private final int min;
    private final int max;
    private final int score;
    
    public QRewards(int min, int max, int score) {
        
        this.min = min;
        this.max = max;
        this.score = score;
        
    }
    
    public int getRandomMoneyAmount() {

        return ((new Random()).nextInt(max - min + 1) + min);
        
    }
    
    public int getScoreReward() {
        
        return score;
        
    }
}
