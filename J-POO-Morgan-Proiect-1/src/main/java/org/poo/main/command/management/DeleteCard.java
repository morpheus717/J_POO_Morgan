package org.poo.main.command.management;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.Main;
import org.poo.main.command.Command;
import org.poo.main.user.Card;
import org.poo.main.user.Client;
import org.poo.main.user.transactions.Transaction;

@Getter @Setter
public class DeleteCard extends Command {
    private String cardNumber;
    private String email;
    private String account;

    public DeleteCard(final String cardNumber,
                      final String email, final int timestamp) {
        super(timestamp);
        this.cardNumber = cardNumber;
        this.email = email;
    }

    public ObjectNode execute() {
        Client client = Bank.findByEmail(email);
        if (client == null) {
           return Main.generateOutputEntry("deleteCard",
                   new Transaction(super.getTimestamp(), "User not found").toJson(),
                   super.getTimestamp());
        }
        Card card = Bank.findByNumber(cardNumber);
        if (card != null) {
            card.destroy(this);
        }
        return null;
    }
}
