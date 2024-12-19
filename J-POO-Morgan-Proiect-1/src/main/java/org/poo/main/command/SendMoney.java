package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;
import org.poo.main.user.Exchange;

import java.util.ArrayList;

@Getter
@Setter
public final class SendMoney extends ExchangeCommand {
    private String senderAcc;
    private double amount;
    private String receiverAcc;
    private String description;
    private String senderEmail;
    private boolean insufficientFunds;

    public SendMoney(final ArrayList<Exchange> rates, final ArrayList<Client> clients,
                     final String senderAcc, final double amount, final String receiverAcc,
                     final String description, final String email, final int timestamp) {
        super(rates, clients, timestamp);
        this.senderAcc = senderAcc;
        this.amount = amount;
        this.receiverAcc = receiverAcc;
        this.description = description;
        this.senderEmail = email;
        this.insufficientFunds = false;
    }

    /**
     * Accepts a visitor to perform the 'SendMoney' operation.
     *
     * @param visitor The visitor that will handle this command.
     * @return The result of the visitor's operation, typically an ObjectNode.
     */
    @Override
    public ObjectNode accept(final Visitor visitor) {
        return visitor.visitSendMoney(this);
    }
}
