package org.poo.utils;

import lombok.Data;

@Data
public final class Pair<K, V> {
    private final K value0;
    private final V value1;
}
