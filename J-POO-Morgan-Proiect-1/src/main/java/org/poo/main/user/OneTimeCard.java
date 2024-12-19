package org.poo.main.user;

import org.poo.main.command.PayOnline;
import org.poo.main.user.transactions.CreateCardTransaction;
import org.poo.main.user.transactions.DeleteCardTransaction;
import org.poo.utils.Utils;

public class OneTimeCard extends Card {

    public OneTimeCard(final String cardNumber, final String status) {
        super(cardNumber, status);
    }

    /**
     * The purpose of this method is to include the mechanism of removing the one time card
     * and adding a new one
     * @param myAcc Account to pay from
     */
    @Override
    public void pay(final Account myAcc, final PayOnline command) {
        super.pay(myAcc, command);
        myAcc.getCards().remove(this);
        String cardNumber = Utils.generateCardNumber();
        myAcc.getCards().add(new OneTimeCard(cardNumber, "active"));
        DeleteCardTransaction transaction = new DeleteCardTransaction("The card has been destroyed",
                this.getCardNumber(), command.getEmail(), myAcc.getIban(), command.getTimestamp());
        CreateCardTransaction transaction1 = new CreateCardTransaction(myAcc.getIban(), cardNumber,
                command.getEmail(), "New card created", command.getTimestamp());
        myAcc.getTransactions().add(transaction);
        myAcc.getTransactions().add(transaction1);
    }
}
