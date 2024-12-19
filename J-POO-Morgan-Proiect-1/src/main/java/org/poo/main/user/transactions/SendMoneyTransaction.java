package org.poo.main.user.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transaction for sending money, including sender and receiver details,
 * amount, transfer type, and currency.
 */
@Getter
@Setter
public class SendMoneyTransaction extends Transaction {

    @JsonProperty("senderIBAN")
    private String senderIban;

    @JsonProperty("receiverIBAN")
    private String receiverIban;
    private String amount;
    private String transferType;

    public SendMoneyTransaction(final int timestamp, final String description,
                                final String senderIban, final String receiverIban,
                                final double amount, final String transferType,
                                final String currency) {
        super(timestamp, description);
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount + " " + currency;
        this.transferType = transferType;
    }
}
