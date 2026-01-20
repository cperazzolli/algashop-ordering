package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;

import com.algaworks.algashop.ordering.domain.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_thenThrowException() {

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> {
             CustomerTestDataBuilder.brandNewCustomerBuild().email(new Email("anonymize")).build();
        });

    }

    @Test
    void given_invalidEmail_whenTryUpdateCustomer_thenThrowException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy( () -> {
           customer.changeEmail(new Email("invalid"));
        });

    }

    @Test
    void given_unarchivedCustomer_whenArchive_shouldAnonymize() {
        Customer customer = CustomerTestDataBuilder.existingCustomerBuild().build();

        customer.archive();
        Assertions.assertWith(customer,
                c-> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous"," Anonymous")),
                c-> assertThat(c.phone().phone()).isEqualTo("00-0000-0000"),
                c-> assertThat(c.document().document()).isEqualTo("000-00-0000"),
                c-> assertThat(c.email().email()).isNotEqualTo("john.doe@email.com"),
                c-> assertThat(c.birthDate()).isNull(),
                c -> assertThat(c.isPrommotionNotificationsAllowed()).isFalse(),
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
        Customer customer = CustomerTestDataBuilder.existingCustomerAnonymizedBuild().build();

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
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(30));
        assertThat(customer.loyaltyPoints().value()).isEqualTo(40);

    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltPoints_shouldGeneratedException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));

    }
}