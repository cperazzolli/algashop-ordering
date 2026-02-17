package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceprovider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(OrdersPersistenceprovider.class)
class OrdersIT {

    private Orders orders;

    @Autowired
    public OrdersIT(Orders orders) {
        this.orders = orders;
    }

    @Test
    void shouldPersistentAndFind() {
        Order originalOrder = OrderTestDataBuilder.anOrder().build();
        OrderId orderId = originalOrder.id();
        orders.add(originalOrder);

        Optional<Order> possivleOrder = orders.ofId(orderId);

        assertThat(possivleOrder).isPresent();

        Order savedOrder = possivleOrder.get();
        assertThat(savedOrder).satisfies(
                o -> assertThat(o.id()).isEqualTo(orderId),
                o -> assertThat(o.customerId()).isEqualTo(originalOrder.customerId()),
                o -> assertThat(o.totalAmount()).isEqualTo(originalOrder.totalAmount()),
                o -> assertThat(o.totalItems()).isEqualTo(originalOrder.totalItems()),
                o -> assertThat(o.placedAt()).isEqualTo(originalOrder.placedAt()),
                o -> assertThat(o.items()).isEqualTo(originalOrder.items()),
                o -> assertThat(o.status()).isEqualTo(originalOrder.status()),
                o -> assertThat(o.paymentMethod()).isEqualTo(originalOrder.paymentMethod()),
                o -> assertThat(o.paidAt()).isEqualTo(originalOrder.paidAt()),
                o -> assertThat(o.canceladAt()).isEqualTo(originalOrder.canceladAt())
        );

    }
}