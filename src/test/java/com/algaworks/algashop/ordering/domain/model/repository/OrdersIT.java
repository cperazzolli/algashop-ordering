package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.model.entity.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disasembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disasembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.provider.CustomersPersistenceProvider;
import com.algaworks.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({
        OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class
})
class OrdersIT {

    private Orders orders;
    private Customers customers;


    @Autowired
    public OrdersIT(Orders orders, Customers customers) {
        this.orders = orders;
        this.customers = customers;
    }

    @BeforeEach
    void setUp() {
        if(!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)){
            customers.add(CustomerTestDataBuilder.brandNewCustomer().build());
        }
    }

    @Test
    void shouldPersistentAndFind() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Order originalOrder = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .build();
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
                o -> assertThat(o.canceledAt()).isEqualTo(originalOrder.canceledAt())
        );

    }

    @Test
    void shouldUpdateExistentOrder() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.PLACED).build();
        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();

        order.markAsPaid();

        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();

        assertThat(order.isPaid()).isTrue();

    }

    @Test
    void shouldNotAllowStaleUpdate() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.PLACED).build();
        orders.add(order);

        Order orderT1 = orders.ofId(order.id()).orElseThrow();
        Order orderT2 = orders.ofId(order.id()).orElseThrow();

        orderT1.markAsPaid();
        orders.add(orderT1);

        orderT2.canceledAt();

        assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> orders.add(orderT2));

        Order savedOrder = orders.ofId(order.id()).orElseThrow();

        assertThat(savedOrder.canceledAt()).isNull();
        assertThat(savedOrder.paidAt()).isNotNull();
    }

    @Test
    void shouldListExistingOrderByYear() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        orders.add(OrderTestDataBuilder.anOrder().customerId(customer.id()).status(OrderStatus.PLACED).build());
        orders.add(OrderTestDataBuilder.anOrder().customerId(customer.id()).status(OrderStatus.PLACED).build());

        List<Order> listOrders = this.orders.placedByCustomerInYear(customer.id(), Year.now());

        assertThat(listOrders).isNotEmpty();
        assertThat(listOrders).hasSize(2);

        listOrders = this.orders.placedByCustomerInYear(customer.id(), Year.now().minusYears(1));

        assertThat(listOrders).isEmpty();
    }

    @Test
    void shouldReturnSalesMetricsByCustomer() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        Order order1 = OrderTestDataBuilder.anOrder().customerId(customer.id()).status(OrderStatus.PAID).build();
        Order order2 = OrderTestDataBuilder.anOrder().customerId(customer.id()).status(OrderStatus.PAID).build();
        orders.add(order1);
        orders.add(order2);

        orders.add(OrderTestDataBuilder.anOrder().customerId(customer.id()).status(OrderStatus.CANCELED).build());
        orders.add(OrderTestDataBuilder.anOrder().customerId(customer.id()).status(OrderStatus.PLACED).build());

        Money totalAmount = order1.totalAmount().add(order2.totalAmount());
        Money actualValue = orders.totalSoldForCustomer(customer.id());
        assertThat(actualValue).isEqualTo(totalAmount);
        assertThat(orders.totalSoldForCustomer(new CustomerId())).isEqualTo(Money.ZERO);
    }

    @Test
    void shouldReturnSalesQuantityByCustomer() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        Order order1 = OrderTestDataBuilder.anOrder().customerId(customer.id()).status(OrderStatus.PAID).build();
        Order order2 = OrderTestDataBuilder.anOrder().customerId(customer.id()).status(OrderStatus.PAID).build();
        orders.add(order1);
        orders.add(order2);

        orders.add(OrderTestDataBuilder.anOrder().customerId(customer.id()).status(OrderStatus.CANCELED).build());
        orders.add(OrderTestDataBuilder.anOrder().customerId(customer.id()).status(OrderStatus.PLACED).build());

        assertThat(orders.salesQuantityByCustomerInYear(customer.id(), Year.now())).isEqualTo(2);
        assertThat(orders.salesQuantityByCustomerInYear(customer.id(), Year.now().minusYears(1))).isZero();
    }
}