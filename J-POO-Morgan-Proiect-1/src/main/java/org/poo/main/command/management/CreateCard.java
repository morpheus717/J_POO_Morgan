package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.user.Account;
import org.poo.main.user.Card;
import org.poo.main.user.Client;
import org.poo.main.user.transactions.CreateCardTransaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Getter
@Setter
public class CreateCard extends Command {
    private String account;
    private String email;
    private String generatedNumber;

    public CreateCard(final String account,
                      final String email, final int timestamp) {
        super(timestamp);
        this.account = account;
        this.email = email;
    }

    @Override
    public ObjectNode execute() {
        Client client = Bank.findByEmail(email);
        if  (client != null) {
            Account acc = Bank.findByIban(account);
            if (acc != null) {
                if (acc.getOwner() != client) {
                    return null; // contul nu apartine clientului
                }
                String cardNum = Utils.generateCardNumber();
                acc.getCards().add(new Card(cardNum, "active", acc));
                acc.getTransactions().add(
                        new CreateCardTransaction(account, cardNum, email,
                                "New card created", super.getTimestamp()));
            }
        }
        return null;
    }
}
