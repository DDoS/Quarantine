package me.DDoS.Quarantine.zone;

import me.DDoS.Quarantine.zone.subzone.SubZoneData;
import me.DDoS.Quarantine.zone.subzone.SubZone;
import me.DDoS.Quarantine.zone.region.MainRegion;
import me.DDoS.Quarantine.zone.region.SubRegion;
import me.DDoS.Quarantine.zone.reward.Reward;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.util.QUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DDoS
 */
public class ZoneLoader {

    private World world;
    private int defaultMoney;
    private int maxNumOfPlayers;
    private int interval;
    private boolean clearDrops;
    private boolean oneTimeKeys;
    private boolean softRespawn;
    private Location lobby;
    private Location entrance;
    private final List<ItemStack> kit = new ArrayList<ItemStack>();
    private final Map<String, SubZoneData> subZoneData = new HashMap<String, SubZoneData>();
    private final Map<EntityType, Reward> mobRewards = new EnumMap<EntityType, Reward>(EntityType.class);

    private boolean loadZoneData(FileConfiguration config, String zoneName) {

        if (!new File("plugins/Quarantine/config.yml").exists()) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", no config.");
            return false;

        }

        try {

            config.load("plugins/Quarantine/config.yml");

        } catch (Exception ex) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", unable to load config.");
            return false;

        }

        ConfigurationSection configSec1 = config.getConfigurationSection("Zones." + zoneName);

        if (configSec1 == null) {

            Quarantine.log.info("[Quarantine] Couldn't load zone " + zoneName + ", not in config.");
            return false;

        }

        String worldName = configSec1.getString("world");
        world = Bukkit.getWorld(worldName);

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

        for (String kitItem : configSec1.getStringList("starting_kit")) {

            String[] s = kitItem.split("-");
            ItemStack item = QUtil.toItemStack(s[0], Integer.parseInt(s[1]));

            if (item != null) {

                kit.add(item);

            }
        }

        for (String rewardToParse : configSec1.getStringList("money_rewards")) {

            String[] s = rewardToParse.split(":");
            String[] s2 = s[1].split("-");

            mobRewards.put(EntityType.fromName(s[0]), new Reward(Integer.parseInt(s2[0]), Integer.parseInt(s2[1]), Integer.parseInt(s2[2])));

        }

        ConfigurationSection configSec2 = configSec1.getConfigurationSection("sub_zones");

        for (String subZone : configSec2.getKeys(false)) {

            ConfigurationSection configSec3 = configSec2.getConfigurationSection(subZone);

            int numOfMobs = configSec3.getInt("number_of_mobs");
            List<String> mobTypes = configSec3.getStringList("mob_types");

            subZoneData.put(subZone, new SubZoneData(numOfMobs, mobTypes));

        }

        return true;

    }

    public Zone loadZone(Quarantine plugin, String zoneName) {

        if (!loadZoneData(plugin.getConfigFile(), zoneName)) {

            return null;

        }

        final List<SubZone> subZones = new ArrayList<SubZone>();

        WorldGuardPlugin worldGuard = plugin.getWorldGuard();

        for (String subZoneName : subZoneData.keySet()) {

            ProtectedRegion subZoneRegion = worldGuard.getRegionManager(world).getRegion(subZoneName);

            if (subZoneRegion != null) {

                SubRegion subZone = new SubRegion(world, subZoneRegion.getMinimumPoint(), subZoneRegion.getMaximumPoint());
                SubZoneData sZData = subZoneData.get(subZoneName);

                subZones.add(new SubZone(subZone, sZData.getNumberOfMobs(), softRespawn, sZData.getMobTypes()));

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

        MainRegion region = new MainRegion(world, pRegion.getMinimumPoint(), pRegion.getMaximumPoint());

        return new Zone(plugin, region, zoneName, lobby, entrance, defaultMoney, maxNumOfPlayers, clearDrops, oneTimeKeys,
                subZones, kit, mobRewards, world, interval);

    }
}