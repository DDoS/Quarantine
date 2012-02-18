package me.DDoS.Quarantine.player.inventory;

/**
 *
 * @author DDoS
 */
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class InventoryItem implements Serializable {

    private static final long serialVersionUID = -6698956186490861187L;
    //
    private final int typeId;
    private final int amount;
    private final short durability;
    private final Map<Integer, Integer> enchantments = new HashMap<Integer, Integer>();

    public InventoryItem(ItemStack item) {

        if (item == null) {

            this.typeId = -1;
            this.amount = 0;
            this.durability = 0;

        } else {

            this.typeId = item.getTypeId();
            this.amount = item.getAmount();
            this.durability = item.getDurability();

            for (Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {

                enchantments.put(entry.getKey().getId(), entry.getValue());

            }
        }
    }

    public ItemStack getItem() {

        if (typeId == -1) {

            return null;

        }

        ItemStack item = new ItemStack(typeId, amount, durability);

        if (enchantments == null) {

            return item;

        }

        for (Entry<Integer, Integer> entry : enchantments.entrySet()) {

            Enchantment enchantment = Enchantment.getById(entry.getKey());

            if (enchantment == null) {

                continue;

            }

            try {

                item.addEnchantment(enchantment, entry.getValue());

            } catch (IllegalArgumentException iae) {

                continue;

            }
        }

        return item;

    }
}
