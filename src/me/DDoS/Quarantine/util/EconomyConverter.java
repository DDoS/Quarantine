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

			QUtil.tell(player.getPlayer(), Messages.get("ExtToIntNotAllowed"));
			return;

		}

		EconomyResponse response = economy.withdrawPlayer(player.getPlayer().getName(), externalAmount);

		if (response.transactionSuccess()) {

			int internalAmount = convertExternalToInternal(externalAmount);
			player.giveMoney(internalAmount);
			QUtil.tell(player.getPlayer(), Messages.get("ExtWithdrawSuccess", internalAmount));

		} else {

			QUtil.tell(player.getPlayer(), Messages.get("TransactionError", response.errorMessage));

		}
	}

	public void transfertInternalToExternal(QPlayer player, int internalAmount) {

		if (internalToExternalRate == -1f) {

			QUtil.tell(player.getPlayer(), Messages.get("IntToExtNotAllowed"));
			return;

		}

		if (player.getMoney() < internalAmount) {

			QUtil.tell(player.getPlayer(), Messages.get("InsufficientFunds"));
			return;

		}

		double externalAmount = convertInternalToExternal(internalAmount);
		EconomyResponse response = economy.depositPlayer(player.getPlayer().getName(), externalAmount);

		if (response.transactionSuccess()) {

			player.removeMoney(internalAmount);
			QUtil.tell(player.getPlayer(), Messages.get("IntWithdrawSuccess",
					Double.toString(externalAmount), externalAmount > 1 ? economy.currencyNamePlural() : economy.currencyNameSingular()));

		} else {

			QUtil.tell(player.getPlayer(), Messages.get("TransactionError", response.errorMessage));

		}
	}

	private int convertExternalToInternal(double external) {

		return (int) Math.round(external * externalToInternalRate);

	}

	private double convertInternalToExternal(int internal) {

		return internal * internalToExternalRate;

	}
}
