package org.poo.main.user;

import org.poo.main.command.payment.PayOnline;
import org.poo.main.user.transactions.CreateCardTransaction;
import org.poo.main.user.transactions.DeleteCardTransaction;
import org.poo.utils.Utils;

public class OneTimeCard extends Card {

    public OneTimeCard(final String cardNumber, final String status, final Account account) {
        super(cardNumber, status, account);
    }

    /**
     * The purpose of this method is to include the mechanism of removing the one time card
     * and adding a new one
     */
    @Override
    public boolean pay(final PayOnline command) {
        if (!super.pay(command))
            return false;
        super.getAccount().getCards().remove(this);
        super.getAccount().getTransactions().add(
                new DeleteCardTransaction(super.getCardNumber(), command.getEmail(),
                        super.getAccount().getIban(), command.getTimestamp()));
        String number = Utils.generateCardNumber();
        super.getAccount().getCards().add(new OneTimeCard(
                number, "active", super.getAccount()));
        super.getAccount().getTransactions().add(
                new CreateCardTransaction(super.getAccount().getIban(),
                        number, command.getEmail(), "New card created", command.getTimestamp()));
        return true;
    }
}
