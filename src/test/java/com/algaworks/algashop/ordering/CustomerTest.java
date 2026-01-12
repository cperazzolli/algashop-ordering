package com.algaworks.algashop.ordering;

import com.algaworks.algashop.ordering.domain.entity.Customer;
import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

class CustomerTest {


    @Test
    void testCustomer() {
        Customer customer = new Customer(
                IdGenerator.generateTimeBasesUUID(),
                "Carlos Gomer",
                LocalDate.of(1978,5,10),
                "carlos.gomes@email.com",
                "111-4545-5454",
                "123456",
                OffsetDateTime.now(),
                true

        );

        System.out.println(customer.id());
        System.out.println(IdGenerator.generateTimeBasesUUID());
        customer.addLoyaltyPoints(10);


    }
}
