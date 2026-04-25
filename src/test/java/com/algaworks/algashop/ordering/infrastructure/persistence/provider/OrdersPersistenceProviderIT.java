package com.algaworks.algashop.ordering.infrastructure.persistence.provider;


import com.algaworks.algashop.ordering.domain.model.entity.*;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.repository.Orders;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disasembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disasembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@Import({
        OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class
})
class OrdersPersistenceProviderIT {

    private final OrdersPersistenceProvider persistenceProvider;
    private final OrderPersistenceEntityRepository entityRepository;
    private final CustomersPersistenceProvider customersPersistenceProvider;

    @Autowired
    private Orders orders;
    @Autowired
    private Customers customers;
    @Autowired
    public OrdersPersistenceProviderIT(OrdersPersistenceProvider persistenceProvider, OrderPersistenceEntityRepository entityRepository, CustomersPersistenceProvider customersPersistenceProvider) {
        this.persistenceProvider = persistenceProvider;
        this.entityRepository = entityRepository;
        this.customersPersistenceProvider = customersPersistenceProvider;
    }

    @BeforeEach
    void setUp() {
        if(!customersPersistenceProvider.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)){
            customersPersistenceProvider.add(CustomerTestDataBuilder.brandNewCustomer().build());
        }
    }

    @Test
    void shouldUpdateAndPersistenceEntityState() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .status(OrderStatus.PLACED).build();
        long aLong = order.id().value().toLong();
        persistenceProvider.add(order);

        var persistenceEntity = entityRepository.findById(aLong).orElseThrow();

        assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PLACED.name());

        assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

        order = persistenceProvider.ofId(order.id()).orElseThrow();
        order.markAsPaid();
        persistenceProvider.add(order);

        persistenceEntity = entityRepository.findById(aLong).orElseThrow();

        assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PAID.name());

        assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

    }

    @Test
    void shouldCountExistingOrders() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        Order order1 = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .build();
        Order order2 = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .build();

        persistenceProvider.add(order1);
        persistenceProvider.add(order2);

        assertThat(persistenceProvider.count()).isEqualTo(3L);
    }

    @Test
    void shouldReturnOrderIfExists() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .build();
        persistenceProvider.add(order);

        assertThat(persistenceProvider.exists(order.id())).isTrue();
        assertThat(persistenceProvider.exists(new OrderId())).isFalse();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldAddFindAndNotFailWhenNoTransaction() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        Order order = OrderTestDataBuilder.anOrder()
                .customerId(customer.id())
                .build();
        persistenceProvider.add(order);

        assertThatNoException()
                .isThrownBy(() -> persistenceProvider.ofId(order.id()).orElseThrow());

    }
}