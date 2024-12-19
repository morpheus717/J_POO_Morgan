package org.poo.main.command;

import org.poo.main.user.Client;
import org.poo.main.user.Exchange;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class ExchangeCommand extends Command {
    private ArrayList<Exchange> rates;

    public ExchangeCommand(ArrayList<Exchange> exchanges, ArrayList<Client> clients,
                           int timestamp) {
        super(clients, timestamp);
        this.rates = new ArrayList<>();
        int i = 0;
        for (Exchange current : exchanges) {
            Exchange inverse = new Exchange(current.getTo(), current.getFrom(),
                    1 / current.getRate());
            rates.add(new Exchange(exchanges.get(i)));
            i++;
            rates.add(inverse);
        }
    }

    private double findRate(String from, String to, double amount, Set<String> visited) {
        if (visited.contains(from)) {
            return -1;
        }
        visited.add(from);
        for (Exchange exchange : rates) {
            if (exchange.getFrom().equals(from)) {
                if (exchange.getTo().equals(to)) {
                    return exchange.getRate() * amount;
                }
                double auxRate = findRate(exchange.getTo(), to,
                        amount * exchange.getRate(), visited);
                if (auxRate != -1)
                    return auxRate;
            }
        }
        visited.remove(from);
        return -1;
    }

    public double exchangeMoney(String from, String to, double amount) {
        if (from.equals(to)) {
            return amount;
        }
        Set<String> visited = new HashSet<>();
        return findRate(from, to, amount, visited);
    }
}
