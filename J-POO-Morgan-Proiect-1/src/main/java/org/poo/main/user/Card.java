package org.poo.main.user;

import lombok.Getter;
import lombok.Setter;
import org.poo.main.command.PayOnline;

@Getter @Setter
public class Card {
    private String cardNumber;
    private String status;

    public Card(String cardNumber, String status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }
    
    public void pay(Account myAcc, PayOnline command) {
        if (myAcc.getCurrency().equals(command.getCurrency())) {
            if (myAcc.getBalance() < command.getAmount()) {
                return;
            }
            myAcc.setBalance(myAcc.getBalance() - command.getAmount());
            return;
        }
        double amount = command.exchangeMoney(command.getCurrency(), myAcc.getCurrency(),
                command.getAmount());

        if (myAcc.getBalance() < amount) {
            return;
        }
        myAcc.setBalance(myAcc.getBalance() - amount);
    }
}
