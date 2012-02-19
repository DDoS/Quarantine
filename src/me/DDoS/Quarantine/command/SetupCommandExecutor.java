package me.DDoS.Quarantine.command;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.permissions.Permission;
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
        
        if (!plugin.getPermissions().hasPermission(player, Permission.PLAY.getPermissionString())) {

            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;

        }
        
        if (cmd.getName().equalsIgnoreCase("qsetlobby") && args.length >= 1) {

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            plugin.getZone(args[0]).setLobby(player.getLocation());
            QUtil.tell(player, "Lobby set.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qsetentrance") && args.length >= 1) {

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
