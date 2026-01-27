package com.algaworks.algashop.ordering.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        Objects.requireNonNull(value);
        value.setScale(2);
    }

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money multiply(Quantity multiplier) {
        return new Money(this.value.multiply(multiplier.toBigDecimal()));
    }

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money divide(Money other) {
        return new Money(this.value.divide(other.value));
    }


    @Override
    public int compareTo(Money money) {
        return this.value.compareTo(money.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
