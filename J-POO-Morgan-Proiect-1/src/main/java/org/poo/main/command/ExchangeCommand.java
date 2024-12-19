package org.poo.main.command;

import org.poo.main.user.Client;
import org.poo.main.user.Exchange;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class ExchangeCommand extends Command {
    private ArrayList<Exchange> rates;

    public ExchangeCommand(final ArrayList<Exchange> exchanges, final ArrayList<Client> clients,
                           final int timestamp) {
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

    /**
     * Finds the exchange rate for the given currency pair.
     *
     * @param from The currency being exchanged from.
     * @param to The currency being exchanged to.
     * @param amount The amount to be exchanged.
     * @param visited A set of visited currencies to prevent infinite loops.
     * @return The exchange rate or -1 if no valid exchange path is found.
     */
    private double findRate(final String from, final String to, final double amount,
                            final Set<String> visited) {
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
                if (auxRate != -1) {
                    return auxRate;
                }
            }
        }
        visited.remove(from);
        return -1;
    }

    /**
     * Exchanges the given amount of money from one currency to another.
     *
     * @param from The currency being exchanged from.
     * @param to The currency being exchanged to.
     * @param amount The amount to be exchanged.
     * @return The exchanged amount in the target currency, or the original amount
     * if the exchange is not possible.
     */
    public double exchangeMoney(final String from, final String to, final double amount) {
        if (from.equals(to)) {
            return amount;
        }
        Set<String> visited = new HashSet<>();
        return findRate(from, to, amount, visited);
    }
}
