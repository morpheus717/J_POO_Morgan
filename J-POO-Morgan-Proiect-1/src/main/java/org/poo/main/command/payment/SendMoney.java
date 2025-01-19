package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.Main;
import org.poo.main.user.Account;
import org.poo.main.user.Client;
import org.poo.main.user.transactions.Transaction;
import org.poo.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public final class SendMoney extends Command {
    private String senderAcc;
    private double amount;
    private String receiverAcc;
    private String description;
    private String senderEmail;
    private boolean insufficientFunds;

    public SendMoney(
            final String senderAcc, final double amount,
            final String receiverAcc, final String description,
            final String email, final int timestamp) {
        super(timestamp);
        this.senderAcc = senderAcc;
        this.amount = amount;
        this.receiverAcc = receiverAcc;
        this.description = description;
        this.senderEmail = email;
        this.insufficientFunds = false;
    }

    @Override
    public ObjectNode execute() {
        Client client = Bank.findByEmail(senderEmail);
        if (client == null) {
            return Main.generateOutputEntry("sendMoney",
                    new Transaction(super.getTimestamp(), "User not found").toJson(),
                    super.getTimestamp());
        }
            Account sender = Bank.findByIban(senderAcc, client);
            Account receiver;
            if (client.getAliases().get(receiverAcc) != null) {
                receiver = Bank.findByIban(client.getAliases().get(receiverAcc));
            } else {
                receiver = Bank.findByIban(receiverAcc);
            }
            if (sender != null && receiver != null && sender.getOwner() == client) {
                sender.sendMoney(this, receiver);
                return null;
            }
        return null;
    }
}
