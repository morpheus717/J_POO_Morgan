package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.Main;
import org.poo.main.user.Account;
import org.poo.main.user.Client;
import org.poo.main.user.transactions.Transaction;

import java.util.ArrayList;

@Getter
@Setter
public final class ChangeInterestRate extends Command {
    private String account;
    private double interestRate;

    public ChangeInterestRate(final String account,
                              final double interestRate, final int timestamp) {
        super(timestamp);
        this.account = account;
        this.interestRate = interestRate;
    }

    @Override
    public ObjectNode execute() {
        Account acc = Bank.findByIban(account);
        if (acc == null) {
            return Main.generateOutputEntry("changeInterestRate",
                    new Transaction(super.getTimestamp(), "Account not found").toJson(),
                    super.getTimestamp());
        }
        return acc.modifyInterestRate(this);
    }
}
