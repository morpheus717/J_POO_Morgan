package org.poo.main.user.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddAccountTransaction extends Transaction {

    public AddAccountTransaction(int timestamp) {
        super(timestamp, "New account created");
    }

}
