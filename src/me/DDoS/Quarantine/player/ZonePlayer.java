package me.DDoS.Quarantine.player;

import me.DDoS.Quarantine.util.QUtil;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DDoS
 */
public class ZonePlayer extends QPlayer {
    
    public ZonePlayer(PlayerData player) {

        super(player);

    }

    public void addKey(String key, int cost) {

        if (!keys.contains(key)) {

            if (cost > money) {

                QUtil.tell(player, "You don't have enough money for this key.");
                return;

            }

            removeMoney(cost);
            keys.add(key);
            QUtil.tell(player, "Key '" + key + "' was added to your key chain.");
            return;

        }

        QUtil.tell(player, "You already own this key.");
        return;

    }

    public boolean useKey(String key, boolean oneTime) {

        boolean success = keys.contains(key);

        if (success && oneTime) {

            keys.remove(key);
            QUtil.tell(player, "The key broke as you unlocked the door.");

        }

        return success;

    }

    public void addScore(int scoreToAdd) {

        score += scoreToAdd;
        
        if (zone.getLB() != null) {
            
            zone.getLB().queueScoreUpdate(this);
            
        }
    }

    @Override
    public boolean isZonePlayer() {

        return true;

    }

    public void addMoney(int amount) {

        money += amount;
        QUtil.tell(player, "You received " + amount + " dollar(s).");

    }
    
    public void buyItem(ItemStack item, int cost) {

        if (cost > money) {

            QUtil.tell(player, "You don't have enough money to buy this item.");
            return;

        }

        QUtil.tell(player, "The item was added to your inventory.");
        removeMoney(cost);
        player.getInventory().addItem(item);
        player.updateInventory();

    }

    public void sellItem(ItemStack item, int cost) {

        if (!player.getInventory().contains(item.getType())) {

            QUtil.tell(player, "You don't have any items of this type to sell: " + item.getType().name().toLowerCase());
            return;

        }

        QUtil.tell(player, "The item was removed from your inventory.");
        addMoney(cost);
        player.getInventory().removeItem(item);
        player.updateInventory();

    }
    
    public void addEnchantment(int ID, int level, int cost) {
        
        if (Enchantment.getById(ID) == null) {
            
            QUtil.tell(player, "Invalid enchantment ID");
            return;
            
        }
        
        if (cost > money) {

            QUtil.tell(player, "You don't have enough money to buy this enchantment.");
            return;

        }
        
        ItemStack item = player.getItemInHand();
        Enchantment enchantment = new EnchantmentWrapper(ID);
        
        if (!enchantment.canEnchantItem(item)) {
            
            QUtil.tell(player, "This enchantment can not be applied to this item.");
            return;
            
        }
        
        if (item.containsEnchantment(enchantment)) {
            
            QUtil.tell(player, "This enchantment has already been applied.");
            return;
            
        }
        
        item.addEnchantment(enchantment, level);
        removeMoney(cost);
        QUtil.tell(player, "Enchantment added.");
        
    }

    private void removeMoney(int amount) {

        money -= amount;
        QUtil.tell(player, amount + " dollar(s) have been substracted from your account balance.");
        tellMoney();

    }

    @Override
    public boolean join() {

        QUtil.tell(player, "You are already playing.");
        return false;

    }

    @Override
    public boolean enter() {

        return join();

    }

    @Override
    public boolean commandLeave() {

        if (zone.getLB() != null) {
            
            zone.getLB().queueScoreUpdate(this);
            
        }
        
        if (!save(true)) {

            QUtil.tell(player, ChatColor.RED + "Couldn't save your data.");

        }
        
        if (!saveInventory()) {

            QUtil.tell(player, ChatColor.RED + "Couldn't save your inventory.");

        }

        clearInventory();
        player.teleport(zone.getLobby());
        player.setHealth(preGameHealth);
        player.setFoodLevel(preGameFoodLevel);
        QUtil.tell(player, "Thank you for playing.");
        return true;

    }
    
    @Override
    public void forceLeave() {
        
        QUtil.tell(player, ChatColor.RED + "This zone is being unloaded.");
        commandLeave();
        
    }
    
    @Override
    public void quitLeave() {

        if (zone.getLB() != null) {
            
            zone.getLB().queueScoreUpdate(this);
            
        }
        
        save(true);
        saveInventory();
        clearInventory();
        player.teleport(zone.getLobby());
        player.setHealth(preGameHealth);
        player.setFoodLevel(preGameFoodLevel);

    }
    
    @Override
    public boolean teleportLeave(PlayerTeleportEvent event) {
        
        if (event.getTo().equals(zone.getLobby())) {
            
            return false;
            
        }
        
        if (zone.isInZone(event.getTo())) {
            
            return false;
            
        }
        
        event.setCancelled(true);
        QUtil.tell(player, "Use '/qleave' to leave the zone.");
        return false;
        
    }
    
    @Override
    public void dieLeave(EntityDeathEvent event) {
        
        score = 0;
        
        if (zone.getLB() != null) {
            
            zone.getLB().queueScoreUpdate(this);
            
        }
        
        deleteInventory();
        deletePlayerDataFile();
        event.getDrops().clear();
        zone.registerDeadPlayer(player.getName(), player.getTotalExperience());
        event.setDroppedExp(0);
        
    }
}