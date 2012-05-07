package me.DDoS.Quarantine.zone;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.player.inventory.Kit;
import me.DDoS.Quarantine.zone.region.provider.RegionProvider;
import me.DDoS.Quarantine.zone.region.provider.ResidenceRegionProvider;
import me.DDoS.Quarantine.zone.subzone.SubZoneData;
import me.DDoS.Quarantine.zone.subzone.SubZone;
import me.DDoS.Quarantine.zone.region.Region;
import me.DDoS.Quarantine.zone.region.SpawnRegion;
import me.DDoS.Quarantine.zone.subzone.SoftRespawnSubZone;

/**
 *
 * @author DDoS
 */
public class ZoneLoader {

    private final Quarantine plugin;
    private final FileConfiguration config;
    //
    private World world;
    private ZoneProperties properties;
    private boolean softRespawn;
    private Location lobby;
    private Location entrance;
    private Map<String, Kit> kits;
    private final Map<String, SubZoneData> subZoneData = new HashMap<String, SubZoneData>();
    private final Map<EntityType, Reward> mobRewards = new EnumMap<EntityType, Reward>(EntityType.class);

    private ZoneLoader(Quarantine plugin, FileConfiguration config) {

        this.plugin = plugin;
        this.config = config;

    }

    public static ZoneLoader loadZoneLoader(Quarantine plugin, FileConfiguration config) {

        try {

            config.load("plugins/Quarantine/config.yml");

        } catch (Exception ex) {

            Quarantine.log.info("[Quarantine] Couldn't get the zone loader, unable to load config.");
            return null;

        }

        return new ZoneLoader(plugin, config);

    }

    private boolean loadZoneData(String zoneName) {

        final ConfigurationSection configSec1 = config.getConfigurationSection("Zones." + zoneName);

        if (configSec1 == null) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", not in config.");
            return false;

        }

        clearLastData();

        final String worldName = configSec1.getString("world");

        if (worldName == null) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", invalid world name.");
            return false;

        }

        world = Bukkit.getWorld(worldName);

        if (world == null) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", invalid world name.");
            return false;

        }

        final double x1 = configSec1.getDouble("entrance.x");
        final double y1 = configSec1.getDouble("entrance.y");
        final double z1 = configSec1.getDouble("entrance.z");
        final float yaw1 = (float) configSec1.getDouble("entrance.yaw");
        final float pitch1 = (float) configSec1.getDouble("entrance.pitch");

        final double x2 = configSec1.getDouble("lobby.x");
        final double y2 = configSec1.getDouble("lobby.y");
        final double z2 = configSec1.getDouble("lobby.z");
        final float yaw2 = (float) configSec1.getDouble("lobby.yaw");
        final float pitch2 = (float) configSec1.getDouble("lobby.pitch");

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

        final int defaultMoney = configSec1.getInt("starting_money");
        final int maxNumOfPlayers = configSec1.getInt("max_number_of_players");
        final long interval = configSec1.getLong("mob_check_task_interval") * 20;
        final boolean clearDrops = configSec1.getBoolean("clear_drops");
        final boolean oneTimeKeys = configSec1.getBoolean("one_time_use_keys");
        final boolean clearXP = configSec1.getBoolean("clear_mob_xp");
        softRespawn = configSec1.getBoolean("soft_mob_respawn");

        properties = new ZoneProperties(zoneName, maxNumOfPlayers, defaultMoney,
                clearDrops, oneTimeKeys, clearXP, interval);

        kits = Kit.loadKits(configSec1.getConfigurationSection("kits"));

        for (String rewardToParse : configSec1.getStringList("money_rewards")) {

            final String[] s = rewardToParse.split(":");
            final String[] s2 = s[1].split("-");
            mobRewards.put(EntityType.fromName(s[0]), new Reward(Integer.parseInt(s2[0]), Integer.parseInt(s2[1]), Integer.parseInt(s2[2])));

        }

        ConfigurationSection configSec2 = configSec1.getConfigurationSection("sub_zones");

        for (String subZone : configSec2.getKeys(false)) {

            ConfigurationSection configSec3 = configSec2.getConfigurationSection(subZone);

            final int numOfMobs = configSec3.getInt("number_of_mobs");
            final List<String> mobTypes = configSec3.getStringList("mob_types");
            subZoneData.put(subZone, new SubZoneData(numOfMobs, mobTypes));

        }

        return true;

    }

    public Zone loadZone(String zoneName) {

        if (!loadZoneData(zoneName)) {

            return null;

        }

        final List<SubZone> subZones = new ArrayList<SubZone>();

        final RegionProvider provider = plugin.getRegionProvider();

        for (String subZoneName : subZoneData.keySet()) {

            SpawnRegion spawnRegion;

            if (provider instanceof ResidenceRegionProvider) {

                spawnRegion = provider.getSpawnRegion(world, zoneName + ":" + subZoneName);

            } else {

                spawnRegion = provider.getSpawnRegion(world, subZoneName);

            }

            if (spawnRegion != null) {

                final SubZoneData sZData = subZoneData.get(subZoneName);

                SubZone subZone;

                if (!softRespawn) {

                    subZone = new SubZone(spawnRegion, sZData.getNumberOfMobs(), sZData.getMobTypes());

                } else {

                    subZone = new SoftRespawnSubZone(spawnRegion, sZData.getNumberOfMobs(), sZData.getMobTypes());

                }

                subZones.add(subZone);

            } else {

                Quarantine.log.info("[Quarantine] Couldn't load subzone: " + subZoneName);

            }
        }

        if (subZones.isEmpty()) {

            Quarantine.log.info("[Quarantine] Couldn't load any subzones for: " + zoneName);
            return null;

        }

        final Region mainRegion = provider.getRegion(world, zoneName);

        if (mainRegion == null) {

            Quarantine.log.info("[Quarantine] Couldn't load main region for: " + zoneName);
            return null;

        }

        return new Zone(plugin, mainRegion, subZones, properties, lobby, entrance, kits, mobRewards);

    }

    private void clearLastData() {

        world = null;
        properties = null;
        softRespawn = false;
        lobby = null;
        entrance = null;
        kits = null;
        subZoneData.clear();
        mobRewards.clear();

    }
}
