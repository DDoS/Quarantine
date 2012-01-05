package me.DDoS.Quarantine.player;

import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.QZone;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DDoS
 */
public class QLobbyPlayer extends QPlayer {

    public QLobbyPlayer(Player player, QZone zone) {

        super(player, zone);

    }

    @Override
    public boolean isZonePlayer() {

        return false;

    }

    private boolean areInvContentsEmpty(ItemStack[] contents, ItemStack[] armor) {

        for (ItemStack content : contents) {

            if (content != null) {

                return false;

            }
        }

        for (ItemStack armorPiece : armor) {

            if (armorPiece.getType() != Material.AIR) {

                return false;

            }
        }

        return true;
    }

    @Override
    public boolean join() {

        if (zone.getLobby() == null) {

            QUtil.tell(player, "No valid lobby.");
            return false;

        }

        if (zone.getEntrance() == null) {

            QUtil.tell(player, "No valid entrance.");
            return false;

        }

        if (!areInvContentsEmpty(player.getInventory().getContents(),
                player.getInventory().getArmorContents())) {

            QUtil.tell(player, "You need to empty your inventory first.");
            return false;

        }

        if (!load()) {

            QUtil.tell(player, ChatColor.RED + "Couldn't load your data.");
            return false;

        }

        if (!restoreInventory()) {

            QUtil.tell(player, ChatColor.RED + "Couldn't restore your inventory.");
            return false;

        }

        player.teleport(zone.getLobby());
        QUtil.tell(player, "Welcome to the lobby. To leave, teleport out.");
        QUtil.tell(player, "To enter the zone, use /qenter.");

        return true;

    }

    @Override
    public boolean enter() {

        player.setHealth(health);
        player.teleport(lastLoc);
        QUtil.tell(player, "Have a nice stay!");
        return true;

    }

    @Override
    public boolean commandLeave() {

        QUtil.tell(player, "To leave, please teleport away.");
        return false;

    }
    
    @Override
    public void forceLeave() {
        
        QUtil.tell(player, ChatColor.RED + "This zone is being unloaded.");
        
        if (!save(false)) {

            QUtil.tell(player, ChatColor.RED + "Couldn't save your data.");

        }

        if (!storeInventory()) {

            QUtil.tell(player, ChatColor.RED + "Couldn't save your inventory.");

        }

        clearInventory();
        player.setHealth(preGameHealth);
        QUtil.tell(player, "Thank you for playing.");
        
    }
    
    @Override
    public void quitLeave() {

        save(false);
        storeInventory();
        clearInventory();
        player.setHealth(preGameHealth);

    }

    @Override
    public boolean teleportLeave(PlayerTeleportEvent event) {

        if (zone.isInZone(event.getTo())) {

            return false;

        }

        if (event.getTo().equals(zone.getLobby())) {

            return false;

        }

        if (!save(false)) {

            QUtil.tell(player, ChatColor.RED + "Couldn't save your data.");

        }

        if (!storeInventory()) {

            QUtil.tell(player, ChatColor.RED + "Couldn't save your inventory.");

        }

        clearInventory();
        player.setHealth(preGameHealth);
        QUtil.tell(player, "Thank you for playing.");
        return true;

    }

    @Override
    public void dieLeave(EntityDeathEvent event) {

        event.getDrops().clear();
        zone.registerDeadPlayer(player.getName(), event.getDroppedExp());
        event.setDroppedExp(0);

    }
}