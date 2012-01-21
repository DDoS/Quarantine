package me.DDoS.Quarantine.listener;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

/**
 *
 * @author DDoS
 */
public class QWorldListener extends WorldListener {

    private final Quarantine plugin;

    public QWorldListener(Quarantine plugin) {

        this.plugin = plugin;

    }

    @Override
    public void onChunkUnload(ChunkUnloadEvent event) {

        for (QZone zone : plugin.getZones()) {

            if (zone.passChunkUnloadEvent(event)) {

                return;

            }
        }
    }
}