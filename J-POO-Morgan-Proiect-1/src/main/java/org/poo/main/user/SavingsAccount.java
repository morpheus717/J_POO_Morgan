package org.poo.main.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.command.management.AddInterest;
import org.poo.main.command.management.ChangeInterestRate;
import org.poo.main.patterns.AccountVisitor;
import org.poo.main.user.transactions.Transaction;

public class SavingsAccount extends Account {

    public SavingsAccount(final String iban, final double balance,
                          final String currency, final String type, Client owner) {
        super(iban, balance, currency, type, owner);
    }

    /**
     * Modifies the interest rate of the account.
     *
     * @param command the command containing the new interest rate
     * @return an ObjectNode (can be null or used to represent a response)
     */
    public ObjectNode modifyInterestRate(final ChangeInterestRate command) {
        this.setInterestRate(command.getInterestRate());
        super.getTransactions().add(new Transaction(
                command.getTimestamp(),
                String.format("Interest rate of the account changed to %.2f",
                        command.getInterestRate())));
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

    @Override
    public ObjectNode accept(AccountVisitor visitor, int timestamp,
                             int startTimestamp, int endTimestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        ObjectNode data = mapper.createObjectNode();
        data.put("error", "This kind of report is not supported for a saving account");
        result.put("command", "spendingsReport");
        result.put("output", data);
        result.put("timestamp", timestamp);
        return result;
    }
}
