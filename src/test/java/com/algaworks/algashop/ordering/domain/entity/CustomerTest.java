package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;

import com.algaworks.algashop.ordering.domain.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


import java.time.*;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_thenThrowException() {

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> {
            new Customer(
                    new CustomerId(),
                    new FullName("John"," Doe"),
                    new BirthDate(LocalDate.of(1985, 5, 22)),
                    new Email("invalid"),
                    new Phone("11-4545-5454"),
                    new Document("123456"),
                    OffsetDateTime.now(),
                    false
            );
        });

    }

    @Test
    void given_invalidEmail_whenTryUpdateCustomer_thenThrowException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John"," Doe"),
                new BirthDate(LocalDate.of(1985, 5, 22)),
                new Email("john.doe@email.com"),
                new Phone("11-4545-5454"),
                new Document("123456"),
                OffsetDateTime.now(),
                false
        );
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> {
           customer.changeEmail(new Email("invalid"));
        });

    }

    @Test
    void given_unarchivedCustomer_whenArchive_shouldAnonimize() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John"," Doe"),
                new BirthDate(LocalDate.of(1985, 5, 22)),
                new Email("john.doe@email.com"),
                new Phone("11-4545-5454"),
                new Document("123456"),
                OffsetDateTime.now(),
                false
        );

        customer.archive();
        Assertions.assertWith(customer,
                c-> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous"," Anonymous")),
                c-> assertThat(c.phone().phone()).isEqualTo("00-0000-0000"),
                c-> assertThat(c.document().document()).isEqualTo("000-00-0000"),
                c-> assertThat(c.email().email()).isNotEqualTo("john.doe@email.com"),
                c-> assertThat(c.birthDate()).isNull(),
                c -> assertThat(c.isPrommotionNotificationsAllowed()).isFalse()
                );
    }

    @Test
    void given_archivedCustomer_whenTryUpdate_shouldGenerated_exception() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Anonymous"," Anonymous"),
                new BirthDate(LocalDate.of(1985, 5, 22)),
                new Email("john.doe@email.com"),
                new Phone("11-4545-5454"),
                new Document("123456"),
                false,
                true,
                OffsetDateTime.of(LocalDateTime.of(2026,1,14,5,25), ZoneOffset.of("-03:00")),
                OffsetDateTime.of(LocalDateTime.of(2026,1,14,5,25), ZoneOffset.of("-03:00")),
                new LoyaltyPoints()
        );

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeName(new FullName("Anonymous"," Anonymous")));

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("john.doe@email.com")));

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltPoints_shouldSumPoints() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John"," Doe"),
                new BirthDate(LocalDate.of(1985, 5, 22)),
                new Email("john.doe@email.com"),
                new Phone("11-4545-5454"),
                new Document("123456"),
                OffsetDateTime.now(),
                false
        );

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(30));

        assertThat(customer.loyaltyPoints().value()).isEqualTo(40);

    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltPoints_shouldGeneratedException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John"," Doe"),
                new BirthDate(LocalDate.of(1985, 5, 22)),
                new Email("john.doe@email.com"),
                new Phone("11-4545-5454"),
                new Document("123456"),
                OffsetDateTime.now(),
                false
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));

    }
}