package org.poo.main.patterns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.user.Account;
import org.poo.main.user.transactions.CardPaymentTransaction;
import org.poo.main.user.transactions.Transaction;

import java.util.ArrayList;

public class SpendingsReportVisitor implements AccountVisitor {
    @Override
    public ObjectNode visit(Account account, int timestamp,
                                   int startTimestamp, int endTimestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        out.put("command", "spendingsReport");
        ObjectNode data = mapper.createObjectNode();
        data.put("IBAN", account.getIban());
        data.put("balance", account.getBalance());
        data.put("currency", account.getCurrency());
        ArrayList<CardPaymentTransaction> transactions = new ArrayList<>();
        for (Transaction transaction : account.getTransactions()) {
            if (transaction.getTimestamp() >= startTimestamp
            && transaction.getTimestamp() <= endTimestamp
            && transaction.filterSpendings() != null) {
                transactions.add(transaction.filterSpendings());
            }
        }
        data.put("transactions", mapper.valueToTree(transactions));
        data.put("commerciants", account.commerciantsToJson(transactions));
        out.put("output", data);
        out.put("timestamp", timestamp);
        return out;
    }
}
