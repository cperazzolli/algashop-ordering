package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.entity.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerLoyatyPointsServiceTest {

    CustomerLoyatyPointsService customerLoyatyPoints = new CustomerLoyatyPointsService();

    @Test
    void givenValidCustomerAndOrder_WhenAddingPoints_thenShouldAccumulate() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.READY).build();

        customerLoyatyPoints.addPoints(customer,order);

        assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    void givenValidCustomerAndOrderWithLowTotalAmount_WhenAddingPoints_thenShouldNotAccumulate() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Product product = ProductTestDataBuilder.aProductAltRamMemory().build();
        Order order = OrderTestDataBuilder.anOrder()
                .withItems(false)
                .status(OrderStatus.DRAFT).build();
        order.addItem(product, new Quantity(1));
        order.place();
        order.markAsPaid();
        order.markAsReady();

        customerLoyatyPoints.addPoints(customer,order);

        assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(0));
    }
}