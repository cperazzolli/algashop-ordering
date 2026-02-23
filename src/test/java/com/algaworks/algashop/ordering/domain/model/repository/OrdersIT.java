package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disasembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class})
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
                o -> assertThat(o.status()).isEqualTo(originalOrder.status()),
                o -> assertThat(o.paymentMethod()).isEqualTo(originalOrder.paymentMethod()),
                o -> assertThat(o.paidAt()).isEqualTo(originalOrder.paidAt()),
                o -> assertThat(o.canceladAt()).isEqualTo(originalOrder.canceladAt())
        );

    }

    @Test
    void shouldUpdateExistentOrder() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();

        order.markAsPaid();

        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();

        assertThat(order.isPaid()).isTrue();

    }

    @Test
    void shouldNotAllowStaleUpdate() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        Order orderT1 = orders.ofId(order.id()).orElseThrow();
        Order orderT2 = orders.ofId(order.id()).orElseThrow();

        orderT1.markAsPaid();
        orders.add(orderT1);

        orderT2.canceled();

        assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> orders.add(orderT2));

        Order savedOrder = orders.ofId(order.id()).orElseThrow();

        assertThat(savedOrder.canceladAt()).isNull();
        assertThat(savedOrder.paidAt()).isNotNull();
    }
}