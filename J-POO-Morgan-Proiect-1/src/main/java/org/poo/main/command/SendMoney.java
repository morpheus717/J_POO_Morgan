package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;
import org.poo.main.user.Exchange;

import java.util.ArrayList;

@Getter @Setter
public class SendMoney extends ExchangeCommand {
    private String senderAcc;
    private double amount;
    private String receiverAcc;
    private String description;
    private String senderEmail;
    private boolean insufficientFunds;


    public SendMoney(ArrayList<Exchange> rates, ArrayList<Client> clients, String senderAcc,
                     double amount, String receiverAcc, String description, String email,
                     int timestamp) {
        super(rates, clients, timestamp);
        this.senderAcc = senderAcc;
        this.amount = amount;
        this.receiverAcc = receiverAcc;
        this.description = description;
        this.senderEmail = email;
        this.insufficientFunds = false;
    }

    public ObjectNode accept(Visitor visitor) {
        return visitor.visitSendMoney(this);
    }
}
