package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.utility.IdGenerator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_thenThrowException() {

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> {
            new Customer(
                    IdGenerator.generateTimeBasesUUID(),
                    "John Doe",
                    LocalDate.of(1985, 5, 22),
                    "invalid",
                    "11-4545-5454",
                    "123456",
                    OffsetDateTime.now(),
                    false
            );
        });

    }

    @Test
    void given_invalidEmail_whenTryUpdateCustomer_thenThrowException() {
        Customer customer = new Customer(
                IdGenerator.generateTimeBasesUUID(),
                "John Doe",
                LocalDate.of(1985, 5, 22),
                "john.doe@email.com",
                "11-4545-5454",
                "123456",
                OffsetDateTime.now(),
                false
        );
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> {
           customer.changeEmail("invalid");
        });

    }
}