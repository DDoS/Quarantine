package me.DDoS.Quarantine.player;

import java.util.concurrent.Callable;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class CallablePlayer implements Callable {

    private final Player player;
    
    public CallablePlayer(Player player) {
        
        this.player = player;
        
    }
    
    @Override
    public Player call() {
        
        return player;
        
    }   
}
