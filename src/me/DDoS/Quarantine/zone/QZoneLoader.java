package me.DDoS.Quarantine.zone;

import me.DDoS.Quarantine.zone.subzone.QSubZoneData;
import me.DDoS.Quarantine.zone.subzone.QSubZone;
import me.DDoS.Quarantine.zone.region.QMainRegion;
import me.DDoS.Quarantine.zone.region.QSubRegion;
import me.DDoS.Quarantine.QRewards;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.DDoS.Quarantine.Quarantine;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.CreatureType;

/**
 *
 * @author DDoS
 */
public class QZoneLoader {

    private World world;
    private int defaultMoney;
    private int maxNumOfPlayers;
    private int interval;
    private boolean clearDrops;
    private boolean oneTimeKeys;
    private boolean softRespawn;
    private Location lobby;
    private Location entrance;
    private final Map<Integer, Integer> kit = new HashMap<Integer, Integer>();
    private final Map<String, QSubZoneData> subZoneData = new HashMap<String, QSubZoneData>();
    private final Map<CreatureType, QRewards> mobRewards = new EnumMap<CreatureType, QRewards>(CreatureType.class);

    private boolean loadZoneData(FileConfiguration config, Server server, String zoneName) {

        if (!new File("plugins/Quarantine/config.yml").exists()) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", no config.");
            return false;

        }

        try {

            config.load("plugins/Quarantine/config.yml");

        } catch (IOException ex) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", unable to load config.");
            return false;

        } catch (InvalidConfigurationException ex) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", unable to load config.");
            return false;

        }

        ConfigurationSection configSec1 = config.getConfigurationSection("Zones." + zoneName);

        if (configSec1 == null) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", not in config.");
            return false;

        }

        String worldName = configSec1.getString("world");
        world = server.getWorld(worldName);

        if (world == null) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", invalid world name.");
            return false;

        }

        double x1 = configSec1.getDouble("entrance.x");
        double y1 = configSec1.getDouble("entrance.y");
        double z1 = configSec1.getDouble("entrance.z");
        float yaw1 = (float) configSec1.getDouble("entrance.yaw");
        float pitch1 = (float) configSec1.getDouble("entrance.pitch");

        double x2 = configSec1.getDouble("lobby.x");
        double y2 = configSec1.getDouble("lobby.y");
        double z2 = configSec1.getDouble("lobby.z");
        float yaw2 = (float) configSec1.getDouble("lobby.yaw");
        float pitch2 = (float) configSec1.getDouble("lobby.pitch");

        if (x1 != 0 && y1 != 0 && z1 != 0 && pitch1 != 0 && yaw1 != 0) {

            entrance = new Location(world, x1, y1, z1, yaw1, pitch1);

        } else {

            entrance = null;

        }

        if (x2 != 0 && y2 != 0 && z2 != 0 && pitch2 != 0 && yaw2 != 0) {

            lobby = new Location(world, x2, y2, z2, yaw2, pitch2);

        } else {

            lobby = null;

        }

        defaultMoney = configSec1.getInt("starting_money");
        maxNumOfPlayers = configSec1.getInt("max_number_of_players");
        interval = configSec1.getInt("mob_check_task_interval") * 20;
        clearDrops = configSec1.getBoolean("clear_drops");
        oneTimeKeys = configSec1.getBoolean("one_time_use_keys");
        softRespawn = configSec1.getBoolean("soft_mob_respawn");

        for (String kitItem : (List<String>) configSec1.getList("starting_kit")) {

            String[] s = kitItem.split("-");

            kit.put(Integer.parseInt(s[0]), Integer.parseInt(s[1]));

        }

        for (String rewardToParse : (List<String>) configSec1.getList("money_rewards")) {

            String[] s = rewardToParse.split(":");
            String[] s2 = s[1].split("-");

            mobRewards.put(CreatureType.fromName(s[0]), new QRewards(Integer.parseInt(s2[0]), Integer.parseInt(s2[1]), Integer.parseInt(s2[2])));

        }

        ConfigurationSection configSec2 = configSec1.getConfigurationSection("sub_zones");

        for (String subZone : configSec2.getKeys(false)) {

            ConfigurationSection configSec3 = configSec2.getConfigurationSection(subZone);

            int numOfMobs = configSec3.getInt("number_of_mobs");
            List<String> mobTypes = configSec3.getList("mob_types");

            subZoneData.put(subZone, new QSubZoneData(numOfMobs, mobTypes));

        }

        return true;

    }

    public QZone loadZone(Quarantine plugin, FileConfiguration config, String zoneName) {

        if (!loadZoneData(config, plugin.getServer(), zoneName)) {
            
            return null;
        
        }
        
        final List<QSubZone> subZones = new ArrayList<QSubZone>();

        WorldGuardPlugin worldGuard = plugin.getWorldGuard();

        for (String subZoneName : subZoneData.keySet()) {

            ProtectedRegion subZoneRegion = worldGuard.getRegionManager(world).getRegion(subZoneName);

            if (subZoneRegion != null) {

                QSubRegion subZone = new QSubRegion(subZoneRegion.getMinimumPoint(), subZoneRegion.getMaximumPoint(), world);
                QSubZoneData sZData = subZoneData.get(subZoneName);

                subZones.add(new QSubZone(subZone, sZData.getNumberOfMobs(), softRespawn, sZData.getMobTypes()));

            } else {

                Quarantine.log.info("[Quarantine] Couldn't load subzone: " + subZoneName);

            }
        }

        ProtectedRegion pRegion = worldGuard.getRegionManager(world).getRegion(zoneName);

        if (pRegion == null) {

            Quarantine.log.info("[Quarantine] Couldn't load main zone: " + zoneName);
            return null;

        }

        if (subZones.isEmpty()) {

            Quarantine.log.info("[Quarantine] Couldn't load any subzones for: " + zoneName);
            return null;

        }

        QMainRegion region = new QMainRegion(pRegion.getMinimumPoint(), pRegion.getMaximumPoint(), world);

        return new QZone(plugin, region, zoneName, lobby, entrance, defaultMoney, maxNumOfPlayers, clearDrops, oneTimeKeys,
                subZones, kit, mobRewards, world, interval);

    }
}