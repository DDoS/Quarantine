package me.DDoS.Quarantine.command;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.permission.Permission;
import me.DDoS.Quarantine.util.Messages;
import me.DDoS.Quarantine.util.QUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author DDoS
 */
public class SetupCommandExecutor implements CommandExecutor {

    private final Quarantine plugin;
    
    public SetupCommandExecutor(Quarantine plugin) {
        
        this.plugin = plugin;
        
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        
        if (!(sender instanceof Player)) {

            sender.sendMessage("This command can only be used in-game.");
            return true;

        }
        
        final Player player = (Player) sender;
        
        if (!plugin.getPermissions().hasPermission(player, Permission.SETUP.getNodeString())) {

            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;

        }
        
        final String cmdName = cmd.getName();
        
        if (cmdName.equalsIgnoreCase("qsetlobby") && args.length >= 1) {

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, Messages.get("ZoneNotFound"));
                return true;

            }

            plugin.getZoneByName(args[0]).setLobby(player.getLocation());
            QUtil.tell(player, Messages.get("LobbySetSuccess"));
            return true;

        }

        if (cmdName.equalsIgnoreCase("qsetentrance") && args.length >= 1) {

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, Messages.get("ZoneNotFound"));
                return true;

            }

            if (plugin.getZoneByName(args[0]).setEntrance(player.getLocation())) {

                QUtil.tell(player, Messages.get("EntranceSetSuccess"));

            } else {

                QUtil.tell(player, Messages.get("EntranceOutOfBounds"));

            }

            return true;

        }
        
        return false;
        
    } 
}
