package org.poo.main.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Exchange {
    private final String from;
    private final String to;
    private final double rate;

    public Exchange(final String from, final String to, final double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    public Exchange(final Exchange exchange) {
        this.from = exchange.getFrom();
        this.to = exchange.getTo();
        this.rate = exchange.getRate();
    }
}
