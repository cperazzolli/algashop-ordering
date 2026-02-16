package com.algaworks.algashop.ordering.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class LoyaltyPointsTest {

    @Test
    void shouldGeneratedWhitValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

    @Test
    void shouldAddValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        LoyaltyPoints loyaltPointsUpdate = loyaltyPoints.add(5);
        assertThat(loyaltPointsUpdate.value()).isEqualTo(15);
    }

    @Test
    void shouldNotAddValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->loyaltyPoints.add(-5));
        assertThat(loyaltyPoints.value()).isEqualTo(10);
    }
}