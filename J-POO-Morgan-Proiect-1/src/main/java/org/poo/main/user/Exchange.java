package org.poo.main.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Exchange {
    private String from;
    private String to;
    private double rate;
    public Exchange(String from, String to, double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }
}
