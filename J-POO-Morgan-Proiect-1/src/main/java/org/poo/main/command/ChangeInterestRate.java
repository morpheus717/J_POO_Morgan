package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter
@Setter
public final class ChangeInterestRate extends Command {
    private String account;
    private double interestRate;

    public ChangeInterestRate(final ArrayList<Client> clients, final String account,
                              final double interestRate, final int timestamp) {
        super(clients, timestamp);
        this.account = account;
        this.interestRate = interestRate;
    }

    /**
     * Accept method for the visitor pattern.
     * This method allows the visitor to process the ChangeInterestRate command.
     *
     * @param visitor The visitor that processes the ChangeInterestRate command.
     * @return A result after processing the ChangeInterestRate command.
     */
    @Override
    public ObjectNode accept(final Visitor visitor) {
        return visitor.visitChangeInterest(this);
    }
}
