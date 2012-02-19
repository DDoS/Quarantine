package me.DDoS.Quarantine;

import me.DDoS.Quarantine.permissions.Permission;
import me.DDoS.Quarantine.permissions.Permissions;
import me.DDoS.Quarantine.util.InventoryConvertor;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;
import me.DDoS.Quarantine.zone.ZoneLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class QCommandExecutor implements CommandExecutor {

    private final Quarantine plugin;
    
    public QCommandExecutor(Quarantine plugin) {
        
        this.plugin = plugin;
        
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        
        if (!(sender instanceof Player)) {

            sender.sendMessage("This command can only be used in-game.");
            return true;

        }

        final Player player = (Player) sender;
        final Permissions permissions = plugin.getPermissions();
        
        if (cmd.getName().equalsIgnoreCase("qload") && args.length == 1) {

            if (!permissions.hasPermission(player, Permission.ADMIN.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!plugin.isWGOn()) {

                QUtil.tell(player, "No worldGuard detected, plugin will not work.");
                return true;

            }

            if (plugin.hasZone(args[0])) {

                QUtil.tell(player, "This zone is already loaded.");
                return true;

            }

            ZoneLoader loader = new ZoneLoader();
            Zone zone = loader.loadZone(plugin, args[0]);

            if (zone == null) {

                QUtil.tell(player, "Couldn't load the zone. Please see the console.");
                return true;

            }

            plugin.addZone(args[0], zone);
            QUtil.tell(player, "Zone loaded.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qunload") && args.length == 1) {

            if (!permissions.hasPermission(player, Permission.ADMIN.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, "This zone is not loaded or does not exist.");
                return true;

            }

            plugin.unloadZone(plugin.getZone(args[0]));
            plugin.removeZone(args[0]);

            QUtil.tell(player, "Zone successfuly unloaded.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qrespawnmobs") && args.length == 1) {

            if (!permissions.hasPermission(player, Permission.ADMIN.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            plugin.getZone(args[0]).reloadMobs();

            QUtil.tell(player, "Mobs successfuly respawned.");
            return true;

        }
        
        if (cmd.getName().equalsIgnoreCase("qconvertinv") && args.length == 1) {

            if (!permissions.hasPermission(player, Permission.ADMIN.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            InventoryConvertor.convert(args[0]);

            QUtil.tell(player, "Inventories converted.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qjoin") && args.length == 1) {

            if (!permissions.hasPermission(player, Permission.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            for (Zone zone : plugin.getZones()) {

                if (zone.hasPlayer(player.getName())) {

                    QUtil.tell(player, "You can only be in one zone at a time.");
                    return true;

                }
            }

            plugin.getZone(args[0]).joinPlayer(player);
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qenter")) {

            if (!permissions.hasPermission(player, Permission.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (plugin.hasZones()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (Zone zone : plugin.getZones()) {

                if (zone.enterPlayer(player)) {

                    return true;


                }
            }

            QUtil.tell(player, "You need to join a zone first.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qleave")) {

            if (!permissions.hasPermission(player, Permission.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (plugin.hasZones()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (Zone zone : plugin.getZones()) {

                if (zone.leavePlayer(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't joined any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qmoney")) {

            if (!permissions.hasPermission(player, Permission.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (plugin.hasZones()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (Zone zone : plugin.getZones()) {

                if (zone.tellMoney(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qkeys")) {

            if (!permissions.hasPermission(player, Permission.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (plugin.hasZones()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (Zone zone : plugin.getZones()) {

                if (zone.tellKeys(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qrank")) {

            if (!permissions.hasPermission(player, Permission.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (plugin.hasZones()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (Zone zone : plugin.getZones()) {

                if (zone.tellRank(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qtop")) {

            if (!permissions.hasPermission(player, Permission.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (plugin.hasZones()) {

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

            if (page < 1) {

                QUtil.tell(player, "The page number must be greater than zero.");
                return true;

            }

            for (Zone zone : plugin.getZones()) {

                if (zone.tellTopFive(player, page)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qscore")) {

            if (!permissions.hasPermission(player, Permission.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (plugin.hasZones()) {

                QUtil.tell(player, "No zones loaded.");
                return true;

            }

            for (Zone zone : plugin.getZones()) {

                if (zone.tellScore(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qzones")) {

            if (!permissions.hasPermission(player, Permission.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");

            } else {

                plugin.getGUIHandler().handleZoneList(player);

            }

            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qplayers")) {

            if (!permissions.hasPermission(player, Permission.PLAY.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            } else {

                for (Zone zone : plugin.getZones()) {

                    if (zone.hasPlayer(player.getName())) {

                        plugin.getGUIHandler().handlePlayerList(player, zone);
                        return true;

                    }
                }

                QUtil.tell(player, "You haven't entered any zone yet.");
                return true;

            }
        }

        if (cmd.getName().equalsIgnoreCase("qsetlobby") && args.length == 1) {

            if (!permissions.hasPermission(player, Permission.SETUP.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            plugin.getZone(args[0]).setLobby(player.getLocation());
            QUtil.tell(player, "Lobby set.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qsetentrance") && args.length == 1) {

            if (!permissions.hasPermission(player, Permission.SETUP.getPermissionsString())) {

                player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;

            }

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            if (plugin.getZone(args[0]).setEntrance(player.getLocation())) {

                QUtil.tell(player, "Entrance set.");

            } else {

                QUtil.tell(player, "The entrance needs to be inside the zone.");

            }

            return true;

        }

        return false;
        
    }
}
