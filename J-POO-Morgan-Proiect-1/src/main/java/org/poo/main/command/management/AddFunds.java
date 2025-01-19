package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.user.Account;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter
@Setter
public class AddFunds extends Command {

    private String account;
    private double amount;

    public AddFunds(final String account, final double amount,
                    final int timestamp) {
        super(timestamp);
        this.account = account;
        this.amount = amount;
    }

    public ObjectNode execute() {
        Account acc = Bank.findByIban(account);
        if (acc != null) {
            acc.deposit(amount);
        }
        return null;
    }
}
