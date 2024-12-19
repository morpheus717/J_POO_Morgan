package org.poo.main.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Commerciant {
    private String commerciant;
    private double total;

    public Commerciant(String commerciant, double total) {
        this.commerciant = commerciant;
        this.total = total;
    }
}
