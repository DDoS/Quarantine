package me.DDoS.Quarantine;

import me.DDoS.Quarantine.leaderboard.QLeaderboard;
import me.DDoS.Quarantine.zone.QZoneLoader;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.permissions.QPermissions;
import me.DDoS.Quarantine.listener.*;
import me.DDoS.Quarantine.zone.QZone;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import me.DDoS.Quarantine.gui.*;
import me.DDoS.Quarantine.permissions.Permissions;
import me.DDoS.Quarantine.permissions.PermissionsHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author DDoS
 */
public class Quarantine extends JavaPlugin {

    public static final Logger log = Logger.getLogger("Minecraft");
    //
    private boolean WGOn;
    //
    private final Map<String, QZone> zones = new HashMap<String, QZone>();
    //
    private FileConfiguration config;
    //
    private Permissions permissions;
    //
    private QGUIHandler guiHandler;

    public Quarantine() {

        checkForJedisLib();

    }

    @Override
    public void onEnable() {

        config = getConfig();

        setupLeadboards();

        checkForWorldGuard();

        if (checkForSpout()) {

            guiHandler = new QSpoutEnabledGUIHandler(this);

        } else {

            guiHandler = new QTextGUIHandler(this);

        }

        permissions = new PermissionsHandler(this).getPermissions();

        loadStartUpZones();

        getServer().getPluginManager().registerEvents(new QListener(this), this);

        log.info("[Quarantine] Plugin enabled. v" + getDescription().getVersion() + ", by DDoS");

    }

    @Override
    public void onDisable() {

        unLoadAllZones();
        zones.clear();
        log.info("[Quarantine] Plugin disabled. v" + getDescription().getVersion() + ", by DDoS");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage("This command can only be used in-game.");
            return true;

        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("qload") && args.length == 1) {

            if (!permissions.hasPermission(player, QPermissions.ADMIN.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!WGOn) {

                QUtil.tell(player, "No worldGuard detected, plugin will not work.");
                return true;

            }

            if (zones.containsKey(args[0])) {

                QUtil.tell(player, "This zone is already loaded.");
                return true;

            }

            QZoneLoader loader = new QZoneLoader();
            QZone zone = loader.loadZone(this, config, args[0]);

            if (zone == null) {

                QUtil.tell(player, "Couldn't load the zone. Please see the console.");
                return true;

            }

            zones.put(args[0], zone);
            QUtil.tell(player, "Zone loaded.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qunload") && args.length == 1) {

            if (!permissions.hasPermission(player, QPermissions.ADMIN.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!zones.containsKey(args[0])) {

                QUtil.tell(player, "This zone is not loaded or does not exist.");
                return true;

            }

            unloadZone(zones.get(args[0]));
            zones.remove(args[0]);

            QUtil.tell(player, "Zone successfuly unloaded.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qrespawnmobs") && args.length == 1) {

            if (!permissions.hasPermission(player, QPermissions.ADMIN.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!zones.containsKey(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            zones.get(args[0]).reloadMobs();

            QUtil.tell(player, "Mobs successfuly respawned.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qjoin") && args.length == 1) {

            if (!permissions.hasPermission(player, QPermissions.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!zones.containsKey(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            for (QZone zone : zones.values()) {

                if (zone.hasPlayer(player.getName())) {

                    QUtil.tell(player, "You can only be in one zone at a time.");
                    return true;

                }
            }

            zones.get(args[0]).joinPlayer(player);
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qenter")) {

            if (!permissions.hasPermission(player, QPermissions.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (zones.isEmpty()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (QZone zone : zones.values()) {

                if (zone.enterPlayer(player)) {

                    return true;


                }
            }

            QUtil.tell(player, "You need to join a zone first.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qleave")) {

            if (!permissions.hasPermission(player, QPermissions.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (zones.isEmpty()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (QZone zone : zones.values()) {

                if (zone.leavePlayer(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't joined any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qmoney")) {

            if (!permissions.hasPermission(player, QPermissions.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (zones.isEmpty()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (QZone zone : zones.values()) {

                if (zone.tellMoney(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qkeys")) {

            if (!permissions.hasPermission(player, QPermissions.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (zones.isEmpty()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (QZone zone : zones.values()) {

                if (zone.tellKeys(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qrank")) {

            if (!permissions.hasPermission(player, QPermissions.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (zones.isEmpty()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (QZone zone : zones.values()) {

                if (zone.tellRank(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qtop")) {

            if (!permissions.hasPermission(player, QPermissions.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (zones.isEmpty()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            int page;

            try {

                page = Integer.parseInt(args[0]);

            } catch (NumberFormatException nfe) {

                QUtil.tell(player, "The page number provided is not an actual number.");
                return true;

            } catch (ArrayIndexOutOfBoundsException aioobe) {

                page = 1;

            }

            if (page <= 0) {

                QUtil.tell(player, "The page number must be greater than zero.");
                return true;

            }

            for (QZone zone : zones.values()) {

                if (zone.tellTopFive(player, page)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qscore")) {

            if (!permissions.hasPermission(player, QPermissions.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (zones.isEmpty()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (QZone zone : zones.values()) {

                if (zone.tellScore(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qzones")) {

            if (!permissions.hasPermission(player, QPermissions.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");

            } else {

                guiHandler.handleZoneList(player);

            }

            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qplayers")) {

            if (!permissions.hasPermission(player, QPermissions.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            } else {

                for (QZone zone : zones.values()) {

                    if (zone.hasPlayer(player.getName())) {

                        guiHandler.handlePlayerList(player, zone);
                        return true;

                    }
                }

                QUtil.tell(player, "You haven't entered any zone yet.");
                return true;

            }
        }

        if (cmd.getName().equalsIgnoreCase("qsetlobby") && args.length == 1) {

            if (!permissions.hasPermission(player, QPermissions.SETUP.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!zones.containsKey(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            zones.get(args[0]).setLobby(player.getLocation());
            QUtil.tell(player, "Lobby set.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qsetentrance") && args.length == 1) {

            if (!permissions.hasPermission(player, QPermissions.SETUP.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!zones.containsKey(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            if (zones.get(args[0]).setEntrance(player.getLocation())) {

                QUtil.tell(player, "Entrance set.");

            } else {

                QUtil.tell(player, "The entrance needs to be inside the zone.");

            }

            return true;

        }

        return false;

    }

    public Collection<QZone> getZones() {

        return zones.values();

    }

    public QGUIHandler getGUIHandler() {

        return guiHandler;

    }

    private void checkForWorldGuard() {

        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin != null && plugin instanceof WorldGuardPlugin) {

            log.info("[Quarantine] WorldGuard detected.");
            WGOn = true;

        } else {

            log.info("[Quarantine] No WorldGuard detected. This plugin will not work.");
            WGOn = false;

        }
    }

    private boolean checkForSpout() {

        PluginManager pm = getServer().getPluginManager();
        Plugin plugin = pm.getPlugin("Spout");

        if (plugin != null) {

            log.info("[Quarantine] Spout detected. Spout GUI enabled.");
            return true;

        } else {

            log.info("[Quarantine] No Spout detected. Spout GUI disabled.");
            return false;

        }
    }

    public WorldGuardPlugin getWorldGuard() {

        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin != null && plugin instanceof WorldGuardPlugin) {

            return (WorldGuardPlugin) plugin;

        }

        return null;

    }

    private void unLoadAllZones() {

        for (QZone zone : zones.values()) {

            unloadZone(zone);

        }
    }

    private void unloadZone(QZone zone) {

        if (!WGOn) {
            return;
        }

        removePlayers(zone);
        saveZoneLocations(zone);
        stopMobCheckTask(zone);
        disconnectLB(zone);

    }

    private void saveZoneLocations(QZone zone) {

        zone.saveLocations(config);

    }

    private void stopMobCheckTask(QZone zone) {

        zone.stopMobCheckTask(getServer());

    }

    private void removePlayers(QZone zone) {

        zone.removeAllPlayers();

    }

    private void disconnectLB(QZone zone) {

        zone.disconnectLB();

    }

    private void loadStartUpZones() {

        if (!WGOn) {

            return;

        }

        for (String zoneToLoad : config.getStringList("Load_on_start")) {

            QZoneLoader loader = new QZoneLoader();
            QZone zone = loader.loadZone(this, config, zoneToLoad);

            if (zone == null) {

                log.info("[Quarantine] Couldn't load zone " + zoneToLoad + " on start up.");
                return;

            }

            zones.put(zoneToLoad, zone);

            log.info("[Quarantine] Loaded zone " + zoneToLoad + ".");

        }
    }

    private void setupLeadboards() {

        if (!config.getBoolean("Leaderboards.enabled")) {

            return;

        }

        QLeaderboard.HOST = config.getString("Leaderboards.redis_db_info.host");
        QLeaderboard.PORT = config.getInt("Leaderboards.redis_db_info.port");
        QLeaderboard.USE = true;

    }

    private void checkForJedisLib() {

        if (!new File("plugins/Quarantine/lib").exists()) {

            new File("plugins/Quarantine/lib").mkdir();

        }

        if (!new File("plugins/Quarantine/lib/jedis-2.0.0.jar").exists()) {

            log.info("[Quarantine] Downloading 'jedis-2.0.0.jar' library.");

            if (downloadJedisLib()) {

                log.info("[Quarantine] Downloading done.");

            }
        }
    }

    private boolean downloadJedisLib() {

        try {

            URL url = new URL("http://dl.dropbox.com/u/43006973/jedis-2.0.0.jar");
            URLConnection con = url.openConnection();
            DataInputStream dis = new DataInputStream(con.getInputStream());
            byte[] fileData = new byte[con.getContentLength()];

            for (int x = 0; x < fileData.length; x++) {

                fileData[x] = dis.readByte();

            }

            dis.close();
            FileOutputStream fos = new FileOutputStream(new File("plugins/Quarantine/lib/jedis-2.0.0.jar"));
            fos.write(fileData);
            fos.close();
            return true;

        } catch (MalformedURLException m) {

            log.info("[Quarantine] Couldn't download 'jedis-2.0.0.jar' library.");
            return false;

        } catch (IOException io) {

            log.info("[Quarantine] Couldn't download 'jedis-2.0.0.jar' library.");
            return false;

        }
    }
}
