package org.poo.main.command.management;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.main.Bank;
import org.poo.main.Main;
import org.poo.main.command.Command;
import org.poo.main.user.Card;
import org.poo.main.user.transactions.Transaction;

public class CheckCardStatus extends Command {
    @Getter
    private String cardNumber;

    public CheckCardStatus(final String cardNumber,
                           final int timestamp) {
        super(timestamp);
        this.cardNumber = cardNumber;
    }

    @Override
    public ObjectNode execute() {
        Card card = Bank.findByNumber(cardNumber);
        if (card == null) {
            return Main.generateOutputEntry(
              "checkCardStatus",
                    new Transaction(super.getTimestamp(), "Card not found").toJson(),
                    super.getTimestamp());
        }
        card.checkCardStatus(this);
        return null;
    }
}
