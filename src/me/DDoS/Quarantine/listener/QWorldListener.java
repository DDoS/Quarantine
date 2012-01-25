package me.DDoS.Quarantine.listener;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 *
 * @author DDoS
 */
public class QWorldListener implements Listener {

    private final Quarantine plugin;

    public QWorldListener(Quarantine plugin) {

        this.plugin = plugin;

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkUnload(ChunkUnloadEvent event) {

        for (QZone zone : plugin.getZones()) {

            if (zone.passChunkUnloadEvent(event)) {

                return;

            }
        }
    }
}
