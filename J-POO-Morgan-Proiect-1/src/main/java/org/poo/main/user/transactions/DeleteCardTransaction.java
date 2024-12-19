package org.poo.main.user.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteCardTransaction extends Transaction {
    @JsonProperty("card")
    private String cardNumber;
    @JsonProperty("cardHolder")
    private String cardHolder;
    @JsonProperty("account")
    private String account;

    public DeleteCardTransaction(String description, String cardNumber, String cardHolder,
                                 String account, int timestamp) {
        super(timestamp, description);
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.account = account;
    }
}
