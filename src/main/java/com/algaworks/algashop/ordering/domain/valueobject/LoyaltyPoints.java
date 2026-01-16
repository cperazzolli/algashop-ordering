package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record LoyaltyPoints(Integer value) implements Comparable<LoyaltyPoints>{

    public static final LoyaltyPoints ZERO = new LoyaltyPoints() ;

    public LoyaltyPoints() {
        this(0);
    }

    public LoyaltyPoints(Integer value) {
        Objects.requireNonNull(value);
        if(value < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public LoyaltyPoints add(Integer points) {
      return add(new LoyaltyPoints(points));
    }

    public LoyaltyPoints add(LoyaltyPoints loyaltyPoints) {
      Objects.requireNonNull(loyaltyPoints);
        if(loyaltyPoints.value <= 0) {
            throw new IllegalArgumentException();
        }

        return new LoyaltyPoints(this.value + loyaltyPoints.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public int compareTo(LoyaltyPoints loyaltyPoints) {
        return this.value.compareTo(loyaltyPoints.value);
    }
}
