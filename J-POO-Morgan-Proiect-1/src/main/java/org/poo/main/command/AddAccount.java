package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public class AddAccount extends Command {
    private String email;
    private String currency;
    private String accountType;
    private double interestRate;
    private String generatedIban;

    public AddAccount(final ArrayList<Client> clients, final String email, final String currency,
                      final String accountType, final double interestRate, final int timestamp) {
        super(clients, timestamp);
        this.email = email;
        this.currency = currency;
        this.accountType = accountType;
        this.interestRate = interestRate;
    }

    /**
     * Accept method for the visitor pattern.
     * This method allows the visitor to process the AddAccount command.
     *
     * @param visitor The visitor that processes the AddAccount command.
     * @return A result after processing the AddAccount command.
     */
    @Override
    public ObjectNode accept(final Visitor visitor) {
        visitor.visitAddAccount(this);
        return null;
    }
}
