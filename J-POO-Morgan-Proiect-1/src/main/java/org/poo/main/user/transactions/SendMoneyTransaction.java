package org.poo.main.user.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SendMoneyTransaction extends Transaction {
    @JsonProperty("senderIBAN")
    private String senderIban;
    @JsonProperty("receiverIBAN")
    private String receiverIban;
    private String amount;
    private String transferType;

    public SendMoneyTransaction(int timestamp, String description, String senderIban,
                                String receiverIban, double amount, String transferType,
                                String currency) {
        super(timestamp, description);
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount + " " + currency;
        this.transferType = transferType;
    }
}
