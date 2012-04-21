package me.DDoS.Quarantine.util;

import me.DDoS.Quarantine.player.QPlayer;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

/**
 *
 * @author DDoS
 */
public class EconomyConverter {

    private final Economy economy;
    //
    private final float externalToInternalRate;
    private final float internalToExternalRate;

    public EconomyConverter(Economy economy, float externalToInternalRate, float internalToExternalRate) {

        this.economy = economy;
        this.externalToInternalRate = externalToInternalRate;
        this.internalToExternalRate = internalToExternalRate;

    }

    public void transfertExternalToInternal(QPlayer player, double externalAmount) {

        if (externalToInternalRate == -1f) {

            QUtil.tell(player.getPlayer(), "External to internal economy transfers aren't allowed.");
            return;

        }

        EconomyResponse response = economy.withdrawPlayer(player.getPlayer().getName(), externalAmount);

        if (response.transactionSuccess()) {

            int internalAmount = convertExternalToInternal(externalAmount);
            player.giveMoney(internalAmount);
            QUtil.tell(player.getPlayer(), "The amount has been withdrawned from your account and converted to "
                    + internalAmount + " Quarantine dollar(s).");

        } else {

            QUtil.tell(player.getPlayer(), "Transaction error: " + response.errorMessage);

        }
    }

    public void transfertInternalToExternal(QPlayer player, int internalAmount) {

        if (internalToExternalRate == -1f) {

            QUtil.tell(player.getPlayer(), "Internal to external economy transfers aren't allowed.");
            return;
            
        }

        if (player.getMoney() < internalAmount) {

            QUtil.tell(player.getPlayer(), "You don't have the requested amount.");
            return;

        }

        double externalAmount = convertInternalToExternal(internalAmount);
        EconomyResponse response = economy.depositPlayer(player.getPlayer().getName(), externalAmount);

        if (response.transactionSuccess()) {

            player.removeMoney(internalAmount);
            QUtil.tell(player.getPlayer(), "The amount has been converted to "
                    + externalAmount + " "
                    + (externalAmount > 1 ? economy.currencyNamePlural() : economy.currencyNameSingular())
                    + ", and deposited to your account.");

        } else {

            QUtil.tell(player.getPlayer(), "Transaction error: " + response.errorMessage);

        }
    }

    private int convertExternalToInternal(double external) {

        return (int) Math.round(external * externalToInternalRate);

    }

    private double convertInternalToExternal(int internal) {

        return internal * internalToExternalRate;

    }
}
