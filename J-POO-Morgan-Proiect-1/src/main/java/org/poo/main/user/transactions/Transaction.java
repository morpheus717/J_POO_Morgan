package org.poo.main.user.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Transaction {
    private int timestamp;
    private String description;

    public Transaction(final int timestamp, final String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    public ObjectNode toJson() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("timestamp", timestamp);
        objectNode.put("description", description);
        return objectNode;
    }
    /**
     * The purpose of this method is to filter Spending type transactions by being overriden
     * @return always null
     */
    public CardPaymentTransaction filterSpendings() {
        return null;
    }
}
