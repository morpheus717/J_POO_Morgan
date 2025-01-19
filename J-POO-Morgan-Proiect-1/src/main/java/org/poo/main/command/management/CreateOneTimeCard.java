package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.user.Account;
import org.poo.main.user.Client;
import org.poo.main.user.OneTimeCard;
import org.poo.main.user.transactions.CreateCardTransaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Getter
@Setter
public class CreateOneTimeCard extends CreateCard {

    public CreateOneTimeCard(final String account,
                             final String email, final int timestamp) {
        super(account, email, timestamp);
    }

    @Override
    public ObjectNode execute() {
        Client client = Bank.findByEmail(super.getEmail());
        if  (client != null) {
            Account acc = Bank.findByIban(super.getAccount(), client);
            if (acc != null) {
                String cardNum = Utils.generateCardNumber();
                acc.getCards().add(new OneTimeCard(cardNum, "active", acc));
                acc.getTransactions().add(
                        new CreateCardTransaction(super.getAccount(), cardNum, super.getEmail(),
                                "New card created", super.getTimestamp()));
            }
        }
        return null;
    }
}
