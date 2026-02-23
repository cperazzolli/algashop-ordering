package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;


import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


class OrderPersistenceEntityAssemblerTest {

    private final OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

    @Test
    void shouldConvertToDomain() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);

        assertThat(orderPersistenceEntity).satisfies(
                o -> assertThat(o.getId()).isEqualTo(order.id().value().toLong()),
                o -> assertThat(o.getCustomerId()).isEqualTo(order.customerId().value()),
                o -> assertThat(o.getTotalAmount()).isEqualTo(order.totalAmount().value()),
                o -> assertThat(o.getTotalItems()).isEqualTo(order.totalItems().value()),
                o -> assertThat(o.getPaymentMethod()).isEqualTo(order.paymentMethod().name()),
                o -> assertThat(o.getPlacedAt()).isEqualTo(order.placedAt()),
                o -> assertThat(o.getPlacedAt()).isEqualTo(order.placedAt()),
                o -> assertThat(o.getCanceledAt()).isEqualTo(order.canceladAt()),
                o -> assertThat(o.getReadyAt()).isEqualTo(order.readyAt())
        );
    }

    @Test
    void givenOrderWithNotItems_shouldRemovePersistenceEntityItems() {
        Order order = OrderTestDataBuilder.anOrder().withItems(false).build();
        OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

        assertThat(order.items()).isEmpty();
        assertThat(orderPersistenceEntity.getItems()).isNotEmpty();

        assembler.merge(orderPersistenceEntity, order);

        assertThat(orderPersistenceEntity.getItems()).isEmpty();
    }

    @Test
    void givenOrderWithItems_shouldAddToPersistenceEntity() {
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().items(new HashSet<>()).build();

        assertThat(order.items()).isNotEmpty();
        assertThat(persistenceEntity.getItems()).isEmpty();

        assembler.merge(persistenceEntity, order);

        assertThat(persistenceEntity.getItems()).isNotEmpty();
        assertThat(persistenceEntity.getItems().size()).isEqualTo(order.items().size());
    }

    @Test
    void givenOrderWithItems_whenMerge_shouldMergeCollecty() {
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();
        var orderItemsPersistenceEntities = order.items().stream()
                .map(assembler::fromDomain)
                .collect(Collectors.toSet());

        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder()
                .items(orderItemsPersistenceEntities)
                .build();

        OrderItem orderItem = order.items().iterator().next();
        order.removeItem(orderItem.id());

        assembler.merge(persistenceEntity, order);

        assertThat(persistenceEntity.getItems()).isNotEmpty();
        assertThat(persistenceEntity.getItems().size()).isEqualTo(order.items().size());
    }
}