package me.DDoS.Quarantine.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.permission.Permission;
import me.DDoS.Quarantine.util.Messages;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;

/**
 *
 * @author DDoS
 */
public class AdminCommandExecutor implements CommandExecutor {

    private final Quarantine plugin;

    public AdminCommandExecutor(Quarantine plugin) {

        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage("This command can only be used in-game.");
            return true;

        }

        final Player player = (Player) sender;

        if (!plugin.getPermissions().hasPermission(player, Permission.ADMIN.getNodeString())) {

            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;

        }
        
        final String cmdName = cmd.getName();

        if (cmdName.equalsIgnoreCase("qload") && args.length >= 1) {

            if (!plugin.hasRegionProvider()) {

                QUtil.tell(player, Messages.get("NoRegionProvider"));
                return true;

            }

            if (plugin.hasZone(args[0])) {

                QUtil.tell(player, Messages.get("ZoneAlreadyLoaded"));
                return true;

            }

            if (!plugin.hasZoneLoader()) {
                
                QUtil.tell(player, Messages.get("NoZoneLoader"));
                return true;
                
            }
            
            Zone zone = plugin.getZoneLoader().loadZone(args[0]);

            if (zone == null) {

                QUtil.tell(player, Messages.get("ZoneLoadError"));
                return true;

            }

            plugin.addZone(args[0], zone);
            QUtil.tell(player, Messages.get("ZoneLoadSuccess"));
            return true;

        }

        if (cmdName.equalsIgnoreCase("qunload") && args.length >= 1) {

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, Messages.get("ZoneNotFound"));
                return true;

            }

            plugin.unloadZone(plugin.getZoneByName(args[0]));
            plugin.removeZone(args[0]);

            QUtil.tell(player, Messages.get("ZoneUnloadSuccess"));
            return true;

        }

        if (cmdName.equalsIgnoreCase("qrespawnmobs") && args.length >= 1) {

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, Messages.get("ZoneNotFound"));
                return true;

            }

            plugin.getZoneByName(args[0]).reloadMobs();

            QUtil.tell(player, Messages.get("MobRespawnSuccess"));
            return true;

        }

        return false;

    }
}
