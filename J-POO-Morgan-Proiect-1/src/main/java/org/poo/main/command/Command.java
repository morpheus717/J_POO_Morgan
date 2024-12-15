package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Account;
import org.poo.main.user.Card;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public abstract class Command {
    private int timestamp;
    private ArrayList<Client> clients;

    public Command(ArrayList<Client> clients, int timestamp) {
        this.timestamp = timestamp;
        this.clients = clients;
    }

    public Client findByEmail(String email) {
        return clients.stream().filter(c -> c.getEmail().equals(email)).findFirst().orElse(null);
    }

    public Account findByIban(String iban, Client client) {
        if (client == null)
            return null;
        return client.getAccounts().stream().filter(a -> a.getIban().equals(iban))
                .findFirst().orElse(null);
    }

    public Account findByIban(String iban) {
        for (Client client : clients) {
            Account found = findByIban(iban, client);
            if (found != null)
                return found;
        }
        return null;
    }

    public Card findByNumber(String number, Client client) {
        for (Account account : client.getAccounts()) {
            for (Card card : account.getCards()) {
                if (card.getCardNumber().equals(number)) {
                    return card;
                }
            }
        }
        return null;
    }

    public abstract ObjectNode accept(Visitor visitor);
}
