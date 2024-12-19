package org.poo.main.user;

import org.poo.main.command.PayOnline;
import org.poo.utils.Utils;

public class OneTimeCard extends Card {

    public OneTimeCard(String cardNumber, String status) {
        super(cardNumber, status);
    }

    @Override
    public void pay(Account myAcc, PayOnline command) {
        super.pay(myAcc, command);
        myAcc.getCards().remove(this);
        myAcc.getCards().add(new OneTimeCard(Utils.generateCardNumber(), "active"));
    }
}
