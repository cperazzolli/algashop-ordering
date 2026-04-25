package com.algaworks.algashop.ordering.domain.model.entity;


import com.algaworks.algashop.ordering.domain.model.exception.CustomerArchivedException;

import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_thenThrowException() {

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> {
             CustomerTestDataBuilder.brandNewCustomer().email(new Email("anonymize")).build();
        });

    }

    @Test
    void given_invalidEmail_whenTryUpdateCustomer_thenThrowException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> {
           customer.changeEmail(new Email("invalid"));
        });

    }

    @Test
    void given_unarchivedCustomer_whenArchive_shouldAnonymize() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        customer.archive();
        Assertions.assertWith(customer,
                c-> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous"," Anonymous")),
                c-> assertThat(c.phone()).isEqualTo("000-000-0000"),
                c-> assertThat(c.document()).isEqualTo("000-00-0000"),
                c-> assertThat(c.email()).isNotEqualTo("john.doe@email.com"),
                c-> assertThat(c.birthDate()).isNotNull(),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
                c-> assertThat(c.address()).isEqualTo(Address.builder()
                        .street("Bourbon Street")
                        .number("Anonymized")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement(null)
                        .build())
                );
    }

    @Test
    void given_archivedCustomer_whenTryUpdate_shouldGenerated_exception() {
        Customer customer = CustomerTestDataBuilder.existingAnonymizedCustomer().build();

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
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(30));
        assertThat(customer.loyaltyPoints().value()).isEqualTo(40);

    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltPoints_shouldGeneratedException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));

    }
}