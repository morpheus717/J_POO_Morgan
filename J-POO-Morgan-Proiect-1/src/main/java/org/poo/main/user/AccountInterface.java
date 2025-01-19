package org.poo.main.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.patterns.AccountVisitor;

interface AccountInterface {
    ObjectNode accept(AccountVisitor visitor, int timestamp,
                      int startTimestamp, int endTimestamp);
}
