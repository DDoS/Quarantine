package me.DDoS.Quarantine.command;

import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.permission.Permission;
import me.DDoS.Quarantine.permission.Permissions;
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

        if (!plugin.getPermissions().hasPermission(player, Permission.ADMIN.getPermissionString())) {

            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qload") && args.length >= 1) {

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

        if (cmd.getName().equalsIgnoreCase("qunload") && args.length >= 1) {

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, "This zone is not loaded or does not exist.");
                return true;

            }

            plugin.unloadZone(plugin.getZone(args[0]));
            plugin.removeZone(args[0]);

            QUtil.tell(player, "Zone successfuly unloaded.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qrespawnmobs") && args.length >= 1) {

            if (!plugin.hasZone(args[0])) {

                QUtil.tell(player, "This zone is not loaded or doesn't exist.");
                return true;

            }

            plugin.getZone(args[0]).reloadMobs();

            QUtil.tell(player, "Mobs successfuly respawned.");
            return true;

        }

        if (cmd.getName().equalsIgnoreCase("qconvertinv") && args.length >= 1) {

            InventoryConvertor.convert(args[0]);
            
            QUtil.tell(player, "Inventories converted.");
            return true;

        }

        return false;

    }
}
