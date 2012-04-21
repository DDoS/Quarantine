package me.DDoS.Quarantine.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DDoS
 */
public class QUtil {

    public static void tell(Player player, String msg) {

        player.sendMessage(ChatColor.DARK_RED + "[Quarantine] " + ChatColor.WHITE + msg);

    }

    public static boolean checkForSign(Block block) {

        switch (block.getType()) {

            case WALL_SIGN:
                return true;

            case SIGN_POST:
                return true;

            default:
                return false;

        }
    }

    public static String toString(Collection objects) {
        
        String string = "";

        for (Object object : objects) {
            
            string = string + object.toString() + ", ";

        }

        try {

            string = string.substring(0, string.length() - 2);

        } catch (StringIndexOutOfBoundsException sioobe) {

            return "";

        }

        return string;
        
    }
    
    public static ItemStack toItemStack(String string, int amount) {

        String[] splits = string.split(":");

        ItemStack item = null;

        try {

            int ID = Integer.parseInt(splits[0]);

            if (ID == 0 || Material.getMaterial(ID) == null) {

                return item;

            }

            item = new ItemStack(ID, amount, splits.length > 1 ? Short.parseShort(splits[1]) : (short) 0);

        } catch (NumberFormatException nfe) {
        }

        return item;

    }

    public static List<ItemStack> parseItemList(String[] lines, int amount) {

        final List<ItemStack> items = new ArrayList<ItemStack>();

        for (String line : lines) {

            String[] splits = line.split("-");

            for (String split : splits) {

                try {

                    ItemStack item = toItemStack(split, amount);

                    if (item != null) {

                        items.add(item);

                    }

                } catch (NumberFormatException nfe) {

                    continue;

                }
            }
        }

        return items;

    }

    public static boolean acceptsMobs(Block block) {

        Material mat = block.getType();

        switch (mat) {
            case HUGE_MUSHROOM_1:
                return true;

            case HUGE_MUSHROOM_2:
                return true;

            case ENDER_PORTAL_FRAME:
                return true;

            case ENDER_STONE:
                return true;

            case PUMPKIN:
                return true;

            case MYCEL:
                return true;

            case NETHER_BRICK:
                return true;

            case NETHER_BRICK_STAIRS:
                return true;

            case BEDROCK:
                return true;

            case BOOKSHELF:
                return true;

            case BRICK:
                return true;

            case BRICK_STAIRS:
                return true;

            case BURNING_FURNACE:
                return true;

            case CHEST:
                return true;

            case CLAY:
                return true;

            case COAL_ORE:
                return true;

            case COBBLESTONE:
                return true;

            case COBBLESTONE_STAIRS:
                return true;

            case DIAMOND_BLOCK:
                return true;

            case DIAMOND_ORE:
                return true;

            case DIRT:
                return true;

            case DISPENSER:
                return true;

            case DOUBLE_STEP:
                return true;

            case FURNACE:
                return true;

            case GLASS:
                return true;

            case GLOWSTONE:
                return true;

            case GOLD_BLOCK:
                return true;

            case GOLD_ORE:
                return true;

            case GRASS:
                return true;

            case GRAVEL:
                return true;

            case ICE:
                return true;

            case IRON_BLOCK:
                return true;

            case IRON_ORE:
                return true;

            case JACK_O_LANTERN:
                return true;

            case JUKEBOX:
                return true;

            case LAPIS_BLOCK:
                return true;

            case LAPIS_ORE:
                return true;

            case LEAVES:
                return true;

            case LOG:
                return true;

            case MELON_BLOCK:
                return true;

            case MOB_SPAWNER:
                return true;

            case MONSTER_EGGS:
                return true;

            case MOSSY_COBBLESTONE:
                return true;

            case NETHERRACK:
                return true;

            case NOTE_BLOCK:
                return true;

            case OBSIDIAN:
                return true;

            case REDSTONE_ORE:
                return true;

            case SAND:
                return true;

            case SANDSTONE:
                return true;

            case SMOOTH_BRICK:
                return true;

            case SMOOTH_STAIRS:
                return true;

            case SNOW_BLOCK:
                return true;

            case SOIL:
                return true;

            case SOUL_SAND:
                return true;

            case SPONGE:
                return true;

            case STEP:
                return true;

            case STONE:
                return true;

            case TNT:
                return true;

            case TRAP_DOOR:
                return true;

            case WOOD:
                return true;

            case WOOD_STAIRS:
                return true;

            case WOOL:
                return true;

            case WORKBENCH:
                return true;

            default:
                return false;

        }
    }
}
