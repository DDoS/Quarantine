package me.DDoS.Quarantine.player.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.DDoS.Quarantine.util.QUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DDoS
 */
public class Kit {

    private final List<ItemStack> kit = new ArrayList<ItemStack>();

    private Kit(List<String> itemStrings) {
        
        for (String itemString : itemStrings) {

            try {

                String[] s1 = itemString.split("\\Q|\\E");
                String[] s2 = s1[0].split("-");
                ItemStack item = QUtil.toItemStack(s2[0], Integer.parseInt(s2[1]));

                if (item != null) {

                    for (int i = 1; i < s1.length; i++) {

                        applyEnchantment(item, s1[i]);

                    }

                    kit.add(item);

                }

            } catch (Exception ex) {
            }
        }
    }

    private void applyEnchantment(ItemStack item, String enchantmentString) {

        try {

            String[] s = enchantmentString.split("-");
            Enchantment enchantment = Enchantment.getById(Integer.parseInt(s[0]));

            if (enchantment != null
                    && enchantment.canEnchantItem(item)
                    && !item.containsEnchantment(enchantment)) {

                item.addEnchantment(enchantment, Integer.parseInt(s[1]));

            }

        } catch (Exception ex) {
        }
    }

    public void giveKit(Player player) {
        
        Inventory inventory = player.getInventory();
        
        for (ItemStack item : kit) {
            
            inventory.addItem(item);
            
        }
        
        player.updateInventory();
        
    }
    
    public static Map<String, Kit> loadKits(ConfigurationSection config) {

        Map<String, Kit> kits = new HashMap<String, Kit>();
        
        for (String kitName : config.getKeys(false)) {

            kits.put(kitName, new Kit(config.getStringList(kitName)));

        }

        return kits;

    }
}
