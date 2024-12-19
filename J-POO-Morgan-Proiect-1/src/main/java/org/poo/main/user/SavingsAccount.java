package org.poo.main.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.command.AddInterest;
import org.poo.main.command.ChangeInterestRate;

public class SavingsAccount extends Account {

    public SavingsAccount(final String iban, final double balance,
                          final String currency, final String type) {
        super(iban, balance, currency, type);
    }

    /**
     * Modifies the interest rate of the account.
     *
     * @param command the command containing the new interest rate
     * @return an ObjectNode (can be null or used to represent a response)
     */
    public ObjectNode modifyInterestRate(final ChangeInterestRate command) {
        this.setInterestRate(command.getInterestRate());
        return null;
    }

    /**
     * Adds interest to the account balance based on the current interest rate.
     *
     * @param command the command to trigger interest addition
     * @return an ObjectNode (can be null or used to represent a response)
     */
    public ObjectNode addInterest(final AddInterest command) {
        this.setBalance(this.getBalance() + this.getBalance() * this.getInterestRate());
        return null;
    }
}
