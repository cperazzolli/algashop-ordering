package com.algaworks.algashop.ordering.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public record Quantity(Integer value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        Objects.requireNonNull(value);
        if(value < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value + other.value);
    }


    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(this.value);
    }

    @Override
    public int compareTo(Quantity quantity) {
        return this.value.compareTo(quantity.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
