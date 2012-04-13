package me.DDoS.Quarantine.command;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.permission.Permission;
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

        if (!plugin.getPermissions().hasPermission(player, Permission.PLAY.getNodeString())) {

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

            for (Zone zone : plugin.getZones()) {

                if (zone.enterPlayer(player)) {

                    return true;


                }
            }

            QUtil.tell(player, "You need to join a zone first.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qleave")) {

            for (Zone zone : plugin.getZones()) {

                if (zone.leavePlayer(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't joined any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qmoney")) {

            for (Zone zone : plugin.getZones()) {

                if (zone.tellMoney(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qkeys")) {

            for (Zone zone : plugin.getZones()) {

                if (zone.tellKeys(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qrank")) {

            for (Zone zone : plugin.getZones()) {

                if (zone.tellRank(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qtop")) {

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

            if (args.length > 0) {

                if (plugin.hasZone(args[0])) {

                    plugin.getGUIHandler().handlePlayerList(player, plugin.getZone(args[0]));
                    return true;

                }
            }

            for (Zone zone : plugin.getZones()) {

                if (zone.hasPlayer(player.getName())) {

                    plugin.getGUIHandler().handlePlayerList(player, zone);
                    return true;

                }
            }

            QUtil.tell(player, "You have to provide a zone name if you aren't playing.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qkit")) {

            if (args.length < 1) {

                QUtil.tell(player, "You need to provide the name of the kit.");
                return true;

            }

            for (Zone zone : plugin.getZones()) {

                if (zone.giveKit(player, args[0])) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qkits")) {

            for (Zone zone : plugin.getZones()) {

                if (zone.tellKits(player)) {

                    return true;

                }
            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        return false;

    }
}
