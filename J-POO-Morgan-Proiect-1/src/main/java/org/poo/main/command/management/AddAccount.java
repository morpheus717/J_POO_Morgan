package org.poo.main.command.management;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.Main;
import org.poo.main.command.Command;
import org.poo.main.user.Account;
import org.poo.main.user.Client;
import org.poo.main.user.SavingsAccount;
import org.poo.main.user.transactions.AddAccountTransaction;
import org.poo.main.user.transactions.Transaction;
import org.poo.utils.Utils;

@Getter @Setter
public class AddAccount extends Command {
    private String email;
    private String currency;
    private String accountType;
    private double interestRate;
    private String generatedIban;

    public AddAccount(final String email, final String currency,
                      final String accountType, final double interestRate, final int timestamp) {
        super(timestamp);
        this.email = email;
        this.currency = currency;
        this.accountType = accountType;
        this.interestRate = interestRate;
    }

    private void saveSuccess(Account acc) {
        AddAccountTransaction transaction = new AddAccountTransaction(super.getTimestamp());
        acc.getTransactions().add(transaction);
    }

    public Account makeAccount(Client client) {
        if (client != null) {
            if (accountType.equals("classic")) {
                return new Account(generatedIban, 0.0,
                        currency, accountType, client);
            } else if (accountType.equals("savings")) {
                return new SavingsAccount(generatedIban, 0.0,
                        currency, accountType, client);
            }
        }
        return null;
    }

    @Override
    public ObjectNode execute() {
        Client client = Bank.findByEmail(email);
        if (client == null) {
            return Main.generateOutputEntry(
                    "addAccount",
                    new Transaction(super.getTimestamp(), "User not found").toJson(),
                    super.getTimestamp());
        }
        generatedIban = Utils.generateIBAN();
        Account acc = makeAccount(client);
        saveSuccess(acc);
        client.getAccounts().add(acc);
        return null;
    }
}
