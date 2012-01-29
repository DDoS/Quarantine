package me.DDoS.Quarantine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import me.DDoS.Quarantine.Quarantine;

/**
 *
 * @author DDoS
 */
public class QInventoryConvertor {

    public static void convert(String zoneName) {

        File dir = new File("plugins/Quarantine/" + zoneName + "/PlayerInventories");

        File[] invs = dir.listFiles();

        if (invs == null) {

            Quarantine.log.info("[Quarantine] Error during conversion of inventories: could not find 'PlayerInventories' file.");
            return;

        }

        for (File inv : invs) {

            convertInv(inv);

        }
    }

    private static void convertInv(File invFile) {

        if (!invFile.getName().endsWith(".inv")) {

            return;

        }

        QInventoryItem[] oldInv;

        try {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(invFile));
            oldInv = (QInventoryItem[]) ois.readObject();
            ois.close();

        } catch (IOException ioe) {

            Quarantine.log.info("[Quarantine] Error during reading of inventory file '"
                    + invFile.getName() + "': "
                    + ioe.getMessage());
            return;

        } catch (ClassCastException cce) {

            Quarantine.log.info("[Quarantine] Inventory file '"
                    + invFile.getName() + "' is already converted.");
            return;

        } catch (ClassNotFoundException cnfe) {

            Quarantine.log.info("[Quarantine] Invalid inventory file '"
                    + invFile.getName() + "'.");
            return;

        }

        me.DDoS.Quarantine.player.inventory.QInventoryItem[] newInv =
                new me.DDoS.Quarantine.player.inventory.QInventoryItem[oldInv.length];

        for (int i = 0; i < newInv.length; i++) {

            newInv[i] =
                    new me.DDoS.Quarantine.player.inventory.QInventoryItem(oldInv[i].getItem());

        }

        try {

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(invFile));
            oos.writeObject(newInv);
            oos.flush();
            oos.close();

            Quarantine.log.info("[Quarantine] Converted inventory file '"
                    + invFile.getName() + "'.");
            return;

        } catch (IOException ioe) {

            Quarantine.log.info("[Quarantine] Error during saving of new inventory file '"
                    + invFile.getName() + "': "
                    + ioe.getMessage());

        }
    }
}
