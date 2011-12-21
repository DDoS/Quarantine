package me.DDoS.Quarantine.listener;

import java.util.Collection;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

/**
 *
 * @author DDoS
 */
public class QWorldListener extends WorldListener {

    private Quarantine plugin;

    public QWorldListener(Quarantine plugin) {

        this.plugin = plugin;

    }

    @Override
    public void onChunkUnload(ChunkUnloadEvent event) {

        Collection<QZone> zones = plugin.getZones();

        for (QZone zone : zones) {

            if (zone.passChunkUnloadEvent(event)) {

                return;

            }
        }
    }
}