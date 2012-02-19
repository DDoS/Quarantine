package me.DDoS.Quarantine.command;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.permissions.Permission;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class PlayerCommandExecutor implements CommandExecutor {

    private final Quarantine plugin;

    public PlayerCommandExecutor(Quarantine plugin) {

        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage("This command can only be used in-game.");
            return true;

        }

        final Player player = (Player) sender;

        if (!plugin.getPermissions().hasPermission(player, Permission.PLAY.getPermissionString())) {

            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qjoin") && args.length >= 1) {

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

            plugin.getGUIHandler().handleZoneList(player);
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qplayers")) {

            for (Zone zone : plugin.getZones()) {

                if (zone.hasPlayer(player.getName())) {

                    plugin.getGUIHandler().handlePlayerList(player, zone);
                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        return false;

    }
}
