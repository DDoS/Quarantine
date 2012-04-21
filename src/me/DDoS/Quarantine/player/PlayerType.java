package me.DDoS.Quarantine.player;

import java.util.concurrent.Callable;

/**
 *
 * @author DDoS
 */
public enum PlayerType implements Callable {

    ZONE_PLAYER, LOBBY_PLAYER, DEFAULT_PLAYER;

    @Override
    public PlayerType call() {

        return this;

    }
}
