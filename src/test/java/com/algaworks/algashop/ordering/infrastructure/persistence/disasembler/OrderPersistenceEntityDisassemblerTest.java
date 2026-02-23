package com.algaworks.algashop.ordering.infrastructure.persistence.disasembler;


import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistenceEntityDisassemblerTest {


    private final OrderPersistenceEntityDisassembler disassembler = new OrderPersistenceEntityDisassembler();

    @Test
    void shouldConvertFromPersistence() {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();
        Order domainOrder = disassembler.toDomainOrder(persistenceEntity);

        assertThat(domainOrder).satisfies(
                o -> assertThat(o.id()).isEqualTo(new OrderId(persistenceEntity.getId())),
                o -> assertThat(o.customerId()).isEqualTo(new CustomerId(persistenceEntity.getCustomerId())),
                o -> assertThat(o.totalAmount()).isEqualTo(new Money(persistenceEntity.getTotalAmount())),
                o -> assertThat(o.totalItems()).isEqualTo(new Quantity(persistenceEntity.getTotalItems())),
                o -> assertThat(o.placedAt()).isEqualTo(persistenceEntity.getPlacedAt()),
                o -> assertThat(o.paidAt()).isEqualTo(persistenceEntity.getPaidAt()),
                o -> assertThat(o.canceladAt()).isEqualTo(persistenceEntity.getCanceledAt()),
                o -> assertThat(o.readyAt()).isEqualTo(persistenceEntity.getReadyAt()),
                o -> assertThat(o.status()).isEqualTo(OrderStatus.valueOf(persistenceEntity.getStatus()))
        );
    }
}