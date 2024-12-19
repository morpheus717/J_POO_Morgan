package org.poo.main.user.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateCardTransaction extends Transaction {
    @JsonProperty("account")
    private String accountNumber;
    @JsonProperty("card")
    private String cardNumber;
    private String cardHolder;

    public CreateCardTransaction(String accountNumber, String cardNumber,
                                 String cardHolder, String description, int timestamp) {
        super(timestamp, description);
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }
}
