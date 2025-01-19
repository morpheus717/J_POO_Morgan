package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.user.Account;
import org.poo.main.user.Client;
import org.poo.main.user.transactions.FailedSplitPayment;
import org.poo.main.user.transactions.SplitPaymentTransaction;
import org.poo.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public final class SplitPayment extends Command {
    private ArrayList<String> accounts;
    private double amount;
    private String currency;
    private boolean success;

    public SplitPayment(
            final ArrayList<String> accounts,
            final double amount, final String currency,
            final int timestamp) {
        super(timestamp);
        this.accounts = accounts;
        this.amount = amount;
        this.currency = currency;
        this.success = false;
    }

    public String checkIfPossible() {
        String poor = null;
        double indAmount = amount / accounts.size();
        for (String s : accounts) {
            Account currAccount = Bank.findByIban(s);
            if (currAccount == null) {
                return "Doesn't exist";
            }
            double persAmount = Bank.getExchangeRate(currency, currAccount.getCurrency())
                    * indAmount;
            if (persAmount > currAccount.getBalance()) {
                poor = s;
            }
        }
        return poor;
    }

    @Override
    public ObjectNode execute() {
        String poor = checkIfPossible();
        double indAmount = amount / accounts.size();
        if (poor == null) {
            for (String s : accounts) {
                Account currAccount = Bank.findByIban(s);
                double persAmount = Bank.getExchangeRate(currency, currAccount.getCurrency())
                        * indAmount;
                currAccount.withdraw(persAmount);
                currAccount.getTransactions().add(new SplitPaymentTransaction(
                        String.format("Split payment of %.2f %s", amount, currency),
                        super.getTimestamp(), currency, indAmount, accounts));
            }
        } else {
            for (String s : accounts) {
                Account currAccount = Bank.findByIban(s);
                currAccount.getTransactions().add(new FailedSplitPayment(
                        String.format("Account %s has insufficient funds for a split payment.", poor),
                        String.format("Split payment of %.2f %s", amount, currency),
                        super.getTimestamp(),
                        currency, indAmount, accounts));
            }
        }
        return null;
    }
}
