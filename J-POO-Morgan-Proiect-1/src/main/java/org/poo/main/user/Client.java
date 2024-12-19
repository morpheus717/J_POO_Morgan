package org.poo.main.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.user.transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public class Client {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;
    @JsonIgnore
    private HashMap<String, String> aliases;

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.aliases = new HashMap<>();
    }

    public ArrayNode transactionsToJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode transactions = mapper.createArrayNode();

        for (Account account : accounts) {
            for (Transaction transaction : account.getTransactions()) {
                transactions.add(mapper.valueToTree(transaction));
            }
        }
        return transactions;
    }
}
