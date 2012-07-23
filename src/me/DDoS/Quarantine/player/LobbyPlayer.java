package me.DDoS.Quarantine.player;

import me.DDoS.Quarantine.player.inventory.Kit;
import me.DDoS.Quarantine.util.Messages;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.zone.Zone;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DDoS
 */
public class LobbyPlayer extends QPlayer {

    private boolean hasInventory = false;

    public LobbyPlayer(Player player, Zone zone) {

        super(player, zone);

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

            QUtil.tell(player, Messages.get("NoValidLobby"));
            return false;

        }

        if (zone.getEntrance() == null) {

            QUtil.tell(player, Messages.get("NoValidEntrance"));
            return false;

        }

        if (!areInvContentsEmpty(player.getInventory().getContents(),
                player.getInventory().getArmorContents())) {

            QUtil.tell(player, Messages.get("InventoryNotEmpty"));
            return false;

        }

        if (!loadData()) {

            QUtil.tell(player, Messages.get("DataLoadError"));
            return false;

        }

        if (!hasInventory()) {

            QUtil.tell(player, Messages.get("SelectAKitNotice"));

        } else {

            if (!loadInventory()) {

                QUtil.tell(player, Messages.get("InventoryLoadError"));
                return false;

            } else {

                hasInventory = true;

            }
        }

        QUtil.tell(player, Messages.get("LobbyWelcome"));
        player.teleport(zone.getLobby());
        QUtil.tell(player, Messages.get("EnterHelp"));
        return true;

    }

    @Override
    public boolean enter() {

        if (!hasInventory) {

            QUtil.tell(player, Messages.get("SelectAKitNotice"));
            return false;

        }

        player.setHealth(health);
        player.setFoodLevel(foodLevel);
        player.teleport(lastLoc);
        QUtil.tell(player, Messages.get("Entry"));
        return true;

    }

    @Override
    public boolean commandLeave() {

        QUtil.tell(player, Messages.get("LeaveLobby"));
        return false;

    }

    @Override
    public void forceLeave() {

        QUtil.tell(player, Messages.get("ZoneUnloadNotice"));

        if (!saveData(false)) {

            QUtil.tell(player, Messages.get("DataSaveError"));

        }

        if (hasInventory) {

            if (!saveInventory()) {

                QUtil.tell(player, Messages.get("InventorySaveError"));

            }
        }

        clearInventory();
        player.setHealth(preGameHealth);
        player.setFoodLevel(preGameFoodLevel);
        QUtil.tell(player, Messages.get("Thanks"));

    }

    @Override
    public void quitLeave() {

        saveData(false);

        if (hasInventory) {

            saveInventory();

        }

        clearInventory();
        player.setHealth(preGameHealth);
        player.setFoodLevel(preGameFoodLevel);

    }

    @Override
    public boolean teleportLeave(PlayerTeleportEvent event) {

        if (zone.isInZone(event.getTo())) {

            return false;

        }

        if (event.getTo().equals(zone.getLobby())) {

            return false;

        }

        if (!saveData(false)) {

            QUtil.tell(player, Messages.get("DataSaveError"));

        }

        if (hasInventory) {

            if (!saveInventory()) {

                QUtil.tell(player, Messages.get("InventorySaveError"));

            }
        }

        clearInventory();
        player.setHealth(preGameHealth);
        player.setFoodLevel(preGameFoodLevel);
        QUtil.tell(player, Messages.get("Thanks"));
        return true;

    }

    @Override
    public void dieLeave(PlayerDeathEvent event) {

        event.getDrops().clear();
        event.setDroppedExp(0);
        event.setKeepLevel(true);

    }

    public void giveKit(Kit kit) {

        if (!hasInventory) {

            kit.giveKit(player);
            hasInventory = true;
            QUtil.tell(player, Messages.get("KitReceived"));

        } else {

            QUtil.tell(player, Messages.get("InventoryAlreadyPresent"));

        }
    }
    
    @Override
    public PlayerType getType() {
        
        return PlayerType.LOBBY_PLAYER;
        
    }
}