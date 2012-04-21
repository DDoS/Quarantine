package me.DDoS.Quarantine.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.permission.Permission;
import me.DDoS.Quarantine.player.QPlayer;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;

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

        final String cmdName = cmd.getName();

        if (cmdName.equalsIgnoreCase("qjoin") && args.length >= 1) {

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            if (plugin.isQuarantinePlayer(player.getName())) {

                QUtil.tell(player, "You can only be in one zone at a time.");
                return true;

            }

            plugin.getZone(args[0]).joinPlayer(player);
            return true;

        }

        if (cmdName.equalsIgnoreCase("qenter")) {

            Zone zone = plugin.getZoneByPlayer(player.getName());

            if (zone != null) {

                zone.enterPlayer(player);
                return true;

            }

            QUtil.tell(player, "You need to join a zone first.");
            return true;

        }

        if (cmdName.equalsIgnoreCase("qleave")) {

            Zone zone = plugin.getZoneByPlayer(player.getName());

            if (zone != null) {

                zone.leavePlayer(player);
                return true;

            }

            QUtil.tell(player, "You haven't joined any zone yet.");
            return true;

        }

        if (cmdName.equalsIgnoreCase("qmoney")) {

            QPlayer qPlayer = plugin.getQuarantinePlayer(player.getName());

            if (qPlayer != null) {

                qPlayer.tellMoney();
                return true;

            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmdName.equalsIgnoreCase("qkeys")) {

            QPlayer qPlayer = plugin.getQuarantinePlayer(player.getName());

            if (qPlayer != null) {

                qPlayer.tellKeys();
                return true;

            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmdName.equalsIgnoreCase("qrank")) {

            QPlayer qPlayer = plugin.getQuarantinePlayer(player.getName());

            if (qPlayer != null) {

                qPlayer.tellRank();
                return true;

            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmdName.equalsIgnoreCase("qtop")) {

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

            QPlayer qPlayer = plugin.getQuarantinePlayer(player.getName());

            if (qPlayer != null) {

                qPlayer.tellTopFive(page);
                return true;

            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmdName.equalsIgnoreCase("qscore")) {

            QPlayer qPlayer = plugin.getQuarantinePlayer(player.getName());

            if (qPlayer != null) {

                qPlayer.tellScore();
                return true;

            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmdName.equalsIgnoreCase("qzones")) {

            plugin.getGUIHandler().handleZoneList(player);
            return true;

        }

        if (cmdName.equalsIgnoreCase("qplayers")) {

            if (args.length > 0) {

                if (plugin.hasZone(args[0])) {

                    plugin.getGUIHandler().handlePlayerList(player, plugin.getZone(args[0]));
                    return true;

                }
            }

            Zone zone = plugin.getZoneByPlayer(player.getName());

            if (zone != null) {

                plugin.getGUIHandler().handlePlayerList(player, zone);
                return true;

            }

            QUtil.tell(player, "You have to provide a zone name if you aren't playing.");
            return true;

        }

        if (cmdName.equalsIgnoreCase("qkit")) {

            if (args.length < 1) {

                QUtil.tell(player, "You need to provide the name of the kit.");
                return true;

            }

            Zone zone = plugin.getZoneByPlayer(player.getName());

            if (zone != null) {

                zone.giveKit(player, args[0]);
                return true;

            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmdName.equalsIgnoreCase("qkits")) {

            QPlayer qPlayer = plugin.getQuarantinePlayer(player.getName());

            if (qPlayer != null) {

                qPlayer.tellKits();
                return true;

            }

            QUtil.tell(player, "You haven't entered any zone yet.");
            return true;

        }

        if (cmdName.equalsIgnoreCase("qconvertmoney")) {

            if (!plugin.hasEconomyConverter()) {

                QUtil.tell(player, "Money conversion is disabled.");
                return true;

            }

            if (args.length < 2) {

                QUtil.tell(player, "You need to provide more arguments.");
                return true;

            }

            if (args[0].equalsIgnoreCase("IntToExt")) {

                int amount = 0;

                try {

                    amount = Integer.parseInt(args[1]);

                } catch (NumberFormatException nfe) {

                    QUtil.tell(player, "The provided amount isn't a valid number.");
                    return true;

                }

                Zone zone = plugin.getZoneByPlayer(player.getName());

                if (zone != null) {

                    if (plugin.getPermissions().hasPermission(player, "quarantine.convertmoney."
                            + zone.getProperties().getZoneName() + ".inttoext")) {

                        plugin.getEconomyConverter().transfertInternalToExternal(zone.getPlayer(player.getName()), amount);

                    } else {

                        QUtil.tell(player, "You don't have permission for this type of money conversion in this zone.");

                    }

                    return true;

                } else {

                    QUtil.tell(player, "You need to join the desired zone first.");

                }

                return true;

            } else if (args[0].equalsIgnoreCase("ExtToInt")) {

                double amount = 0;

                try {

                    amount = Double.parseDouble(args[1]);

                } catch (NumberFormatException nfe) {

                    QUtil.tell(player, "The provided amount isn't a valid number.");
                    return true;

                }

                Zone zone = plugin.getZoneByPlayer(player.getName());

                if (zone != null) {

                    if (plugin.getPermissions().hasPermission(player, "quarantine.convertmoney."
                            + zone.getProperties().getZoneName() + ".exttoint")) {

                        plugin.getEconomyConverter().transfertExternalToInternal(zone.getPlayer(player.getName()), amount);

                    } else {

                        QUtil.tell(player, "You don't have permission for this type of money conversion in this zone.");

                    }

                    return true;

                } else {

                    QUtil.tell(player, "You need to join the desired zone first.");

                }

            } else {

                QUtil.tell(player, "The first argument must be either 'IntToExt' or 'ExtToInt'.");
                return true;

            }
        }

        return false;

    }
}
